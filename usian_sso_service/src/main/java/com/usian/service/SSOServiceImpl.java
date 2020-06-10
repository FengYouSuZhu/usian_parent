package com.usian.service;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.pojo.TbUserExample;
import com.usian.redis.RedisClient;
import com.usian.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/9 11:43
 */
@Service
@Transactional
public class SSOServiceImpl implements  SSOService{

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private  Long SESSION_EXPIRE;

    @Override
    public Boolean checkUserInfo(String checkValue, int checkFlag) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        // 1、查询条件根据参数动态生成：1、2分别代表username、phone
        if (checkFlag == 1){
            criteria.andUsernameEqualTo(checkValue);
        }else if(checkFlag== 2){
            criteria.andPhoneEqualTo(checkValue);
        }
        // 2、从tb_user表中查询数据
        List<TbUser> tbUserList = tbUserMapper.selectByExample(tbUserExample);
        // 3、判断查询结果，如果查询到数据返回false。

        if (tbUserList == null || tbUserList.size()== 0){
            // 4、如果没有返回true。
            return true;
        }
        return false;
    }

    @Override
    public Integer userRegister(TbUser user) {
        //将密码做加密处理。
        String pwd = MD5Utils.digest(user.getPassword());
        user.setPassword(pwd);
        //补齐数据
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        //保存数据库
        int insertSelective = tbUserMapper.insertSelective(user);
        return insertSelective;
    }

    @Override
    public Map userLogin(String username, String password) {
        //密码加密处理
        String pwd = MD5Utils.digest(password);
        // 判断用户名密码是否正确。
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);
        List<TbUser> tbUserList = tbUserMapper.selectByExample(tbUserExample);
        if(tbUserList==null || tbUserList.size()<=0){
            return  null;
        }
        TbUser tbUser = tbUserList.get(0);
        // 2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
        String token = UUID.randomUUID().toString();
        //把用户信息保存到redis 设置key的过期时间
        tbUser.setPassword(null);
        redisClient.set(USER_INFO+":"+token,tbUser);
        redisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userid",tbUser.getId().toString());
        map.put("username",tbUser.getUsername());
        return map;
    }

    /**
     * 查询用户登录是否过期
     * @param token
     * @return
     */
    @Override
    public TbUser getUserByToken(String token) {
        TbUser tbUser = (TbUser) redisClient.get(USER_INFO + ":" + token);
        if (tbUser!=null){
            //需要重置key的过期时间。
            redisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
            return tbUser;
        }
        return null;
    }

    @Override
    public Boolean logOut(String token) {
        return redisClient.del(USER_INFO + ":" + token);
    }
}

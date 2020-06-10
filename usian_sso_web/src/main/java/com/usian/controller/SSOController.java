package com.usian.controller;

import com.usian.feign.SSOServiceFeignClient;
import com.usian.pojo.TbUser;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/9 11:35
 */

/**
 * 用户注册与登录
 */
@RestController
@RequestMapping("/frontend/sso")
public class SSOController {

    @Autowired
    private SSOServiceFeignClient ssoServiceFeignClient;


    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable String checkValue,@PathVariable int checkFlag){
      Boolean checkUserInfoBoolean=  ssoServiceFeignClient.checkUserInfo(checkValue,checkFlag);
        if (checkUserInfoBoolean){
            return  Result.ok();
        }
        return  Result.error("查无结果");
    }

    @RequestMapping("/userRegister")
    public  Result userRegister(TbUser user){
        Integer userRegister=ssoServiceFeignClient.userRegister(user);
        if (userRegister ==1){
            return Result.ok();
        }
        return Result.error("注册失败！");
    }

    @RequestMapping("/userLogin")
    public Result userRegister(String username,String password){
        Map map =ssoServiceFeignClient.userLogin(username,password);
        if (map!=null){
            return  Result.ok(map);
        }
        return  Result.error("登录失败！");
    }

    @RequestMapping("/getUserByToken/{token}")
    public Result getUserByToken(@PathVariable String token){
        TbUser tbUser=ssoServiceFeignClient.getUserByToken(token);
        if(tbUser!=null){
            return Result.ok();
        }
        return Result.error("查无结果");
    }

    @RequestMapping("/logOut")
    public  Result logOut(String token){
        Boolean logOut=ssoServiceFeignClient.logOut(token);
        if (logOut){
            return  Result.ok();
        }
        return  Result.error("退出失败！");

    }

}

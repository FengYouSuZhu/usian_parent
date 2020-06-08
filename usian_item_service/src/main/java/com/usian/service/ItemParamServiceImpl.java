package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${PARAM}")
    private String PARAM;

    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    @Autowired
    private RedisClient redisClient;
    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return
     */
    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId){
        //System.out.println(itemCatId);
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list=tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size()>0){
            return  list.get(0);
        }
        return null;
    }

    /**
     * 查询所有商品规格模板
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        tbItemParamExample.setOrderByClause("updated DESC");
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(tbItemParamList);
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(pageInfo.getTotal());
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setResult(pageInfo.getList());
        return pageResult;
    }

    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {
        //1、判断该类别的商品是否有规格模板
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExample(tbItemParamExample);
        if (tbItemParamList.size()>0){
            return 0;
        }
        //2、保存规格模板
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        Date date = new Date();
        tbItemParam.setUpdated(date);
        tbItemParam.setCreated(date);
        //存入数据库
        return tbItemParamMapper.insertSelective(tbItemParam);
    }

    /**
     * 商品规格模板删除
     * @param id
     * @return
     */
    @Override
    public Integer deleteItemParamById(Long id) {
        return tbItemParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        //查询缓存
        TbItemParamItem tbItemParamItem = (TbItemParamItem) redisClient.get(ITEM_INFO + ":" + itemId + ":" + PARAM);
        if (tbItemParamItem!=null){
            return tbItemParamItem;
        }
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItemList =
                tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if(tbItemParamItemList!=null && tbItemParamItemList.size()>0){
            //把数据保存到缓存
            redisClient.set(ITEM_INFO + ":" + itemId + ":"+
                    PARAM,tbItemParamItemList.get(0));
            //设置缓存的有效期
            redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ PARAM,ITEM_INFO_EXPIRE);
            return tbItemParamItemList.get(0);
        }
        /********************解决缓存穿透************************/
        //把空对象保存到缓存
        redisClient.set(ITEM_INFO + ":" + itemId + ":"+ PARAM,null);
        //设置缓存的有效期
        redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ PARAM,30);
        return null;
    }
}

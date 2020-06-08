package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:42
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisClient redisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${BASE}")
    private String BASE;

    @Value("${DESC}")
    private String DESC;

    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;
    /**
     * 查询商品信息
     * @param itemId
     * @return
     */
    @Override
    public TbItem selectItemInfo(Long itemId) {
        //查询缓存,如果有返回
        TbItem tbItem = (TbItem) redisClient.get(ITEM_INFO + ":" + itemId + ":" + BASE);
        if (tbItem!=null){
            return tbItem;
        }
        //将查询的数据保存到redis缓存中，并设置有效期
         tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        if (tbItem ==null){
            redisClient.set(ITEM_INFO + ":" + itemId + ":"+ BASE,tbItem);
            redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ BASE,ITEM_INFO_EXPIRE);
            return tbItem;
        }
        //把数据保存到缓存
        redisClient.set(ITEM_INFO + ":" + itemId + ":"+ BASE,null);
        //设置缓存的有效期
        redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ BASE,ITEM_INFO_EXPIRE);
        return tbItem;


    }
    /**
     * 根据商品 ID 查询商品描述
     * @param
     * @return
     */


    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);

        TbItemExample example = new TbItemExample();
        example.setOrderByClause("updated DESC");
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> list = tbItemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        PageResult result = new PageResult();
        result.setTotalPage(pageInfo.getTotal());
        result.setPageIndex(pageInfo.getPageNum());
        result.setResult(list);
        return result;
    }
    /**
     * 添加商品
     */
    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        //补齐 Tbitem 数据
        long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        int insertTbItemNum = tbItemMapper.insert(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setCreated(date);
        Integer tbItemDescNum=itemDescMapper.insert(tbItemDesc);
        
        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        int itemParamItmeNum = itemParamItemMapper.insert(tbItemParamItem);

       amqpTemplate.convertAndSend("item_exchage","item.add", itemId);
        return insertTbItemNum+tbItemDescNum+itemParamItmeNum;
    }


    @Override
    public Integer deleteItemById(Long itemId) {
        return tbItemMapper.deleteByPrimaryKey(itemId);
    }

    /**
     * 修改前，数据回显
     *      根据商品 ID 查询商品，商品分类，商品描述，商品规格参数
     * @param itemId
     * @return
     */
    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        Map<String, Object> map = new HashMap<>();
        //查询商品
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item",tbItem);
        //商品分类
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
        map.put("itemCat",tbItemCat.getName());
        //商品描述
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc",tbItemDesc.getItemDesc());
        //商品规格参数
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> itemParamItemList = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if (itemParamItemList !=null && itemParamItemList .size()>0){
            map.put("itemParamItem",itemParamItemList.get(0).getParamData());
        }

        return map;
    }

    @Override
    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        //redis缓存同步
        redisClient.del(ITEM_INFO + ":" + tbItem.getId() + ":" + BASE);


        //补齐 Tbitem 数据
        //long itemId = IDUtils.genItemId();
        Date date = new Date();
        //tbItem.setId(itemId);
        Long id = tbItem.getId();
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCid(560L);
        tbItem.setCreated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        int insertTbItemNum = tbItemMapper.updateByPrimaryKey(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        //tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setCreated(date);
        Integer tbItemDescNum=itemDescMapper.updateByPrimaryKey(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        //tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        int itemParamItmeNum = itemParamItemMapper.updateByPrimaryKey(tbItemParamItem);

        return insertTbItemNum+tbItemDescNum+itemParamItmeNum;

    }

    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        TbItemDesc tbItemDesc = (TbItemDesc) redisClient.get(ITEM_INFO + ":" + itemId + ":" + DESC);
        if (tbItemDesc !=null){
            return tbItemDesc;
        }
        TbItemDescExample tbItemDescExample = new TbItemDescExample();
        TbItemDescExample.Criteria criteria = tbItemDescExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemDesc> tbItemDescs = itemDescMapper.selectByExampleWithBLOBs(tbItemDescExample);
        if(tbItemDescs!=null && tbItemDescs.size()>0){
            //把数据保存到缓存
            redisClient.set(ITEM_INFO + ":" + itemId + ":"+ DESC,tbItemDescs.get(0));
            //设置缓存的有效期
            redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ DESC,ITEM_INFO_EXPIRE);
            return tbItemDescs.get(0);
        }
        //把空对象保存到缓存
        redisClient.set(ITEM_INFO + ":" + itemId + ":"+ DESC,null);
        //设置缓存的有效期
        redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ DESC,30);
        return null;
    }


}

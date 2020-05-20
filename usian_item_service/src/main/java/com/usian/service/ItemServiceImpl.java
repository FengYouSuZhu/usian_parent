package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemExample;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Override
    public TbItem selectItemInfo(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

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
        return insertTbItemNum+tbItemDescNum+itemParamItmeNum;
    }

}

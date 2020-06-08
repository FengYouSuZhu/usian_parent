package com.usian.service;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.utils.PageResult;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:42
 */
public interface ItemService {
    TbItem selectItemInfo(Long itemId);

    PageResult selectTbItemAllByPage(Integer page, Integer rows);

    Integer insertTbItem(TbItem tbItem, String desc, String itemParams);
    //删除商品
    Integer deleteItemById(Long itemId);
    //修改前，数据回显
    Map<String, Object> preUpdateItem(Long itemId);
    //修改
    Integer updateTbItem(TbItem tbItem, String desc, String itemParams);

    TbItemDesc selectItemDescByItemId(Long itemId);
}

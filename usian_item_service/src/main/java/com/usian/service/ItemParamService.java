package com.usian.service;

import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.PageResult;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/17 19:08
 */
public interface ItemParamService {
    TbItemParam selectItemParamByItemCatId(Long itemCatId);

    PageResult selectItemParamAll(Integer page, Integer rows);

    Integer insertItemParam(Long itemCatId, String paramData);

    Integer deleteItemParamById(Long id);

    TbItemParamItem selectTbItemParamItemByItemId(Long itemId);
}

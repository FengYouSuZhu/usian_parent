package com.usian.service;

import com.usian.pojo.TbItemParam;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/17 19:08
 */
public interface ItemParamService {
    TbItemParam selectItemParamByItemCatId(Long itemCatId);
}

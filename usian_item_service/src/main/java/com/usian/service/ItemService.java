package com.usian.service;

import com.usian.pojo.TbItem;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:42
 */
public interface ItemService {
    TbItem selectItemInfo(Long itemId);
}

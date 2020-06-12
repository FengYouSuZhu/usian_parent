package com.usian.service;

import com.usian.pojo.TbItem;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/12 12:06
 */
public interface CartService {
    Map<String, TbItem> selectCartByUserId(String userId);

    Boolean insertCart(Map<String, TbItem> cart, String userId);
}

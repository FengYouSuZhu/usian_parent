package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/12 12:05
 */
@RequestMapping("/service/cart")
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/selectCartByUserId")
    public Map<String, TbItem> selectCartByUserId(String userId){
    return  cartService.selectCartByUserId(userId);
    }

    @RequestMapping("insertCart")
    public Boolean insertCart(@RequestBody Map<String, TbItem> cart,  String userId){
        return  cartService.insertCart(cart,userId);
    }
}

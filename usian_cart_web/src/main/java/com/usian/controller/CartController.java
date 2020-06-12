package com.usian.controller;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/12 7:45
 */

import com.usian.feign.CartServiceFeignClient;
import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItem;
import com.usian.utils.CookieUtils;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 购物车 Controller
 */
@RestController
@RequestMapping("/frontend/cart")
public class CartController {
    @Value("${CART_COOKIE_KEY}")
    private String CART_COOKIE_KEY;
    @Value("${CART_COOKIE_EXPIRE}")
    private  Long CART_COOKIE_EXPIRE;

    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    @Autowired
    private CartServiceFeignClient cartServiceFeignClient;
    @RequestMapping("/addItem")
    public Result addItem(Long itemId, String userId, @RequestParam(defaultValue = "1") Integer
                         num, HttpServletRequest request, HttpServletResponse response){
        try {
            if (StringUtils.isBlank(userId)){
                /***********在用户未登录的状态下**********/
                //1、从cookie中查询商品列表
                Map<String, TbItem> cart=getCartFromCookie(request);
                //2、添加商品到购物车
                addItemToCart(cart,itemId,num);
                //3、把购物车写入cookie中
                addClientCookie(request,response,cart);
            }else{
                // 在用户已登录的状态
                //1、从cookie中查询商品列表
                Map<String, TbItem> cart= getcartFromRedis(userId);
                //2、添加商品到购物车
                addItemToCart(cart,itemId,num);
                //3、把购物车写入cookie中
                Boolean addCartToRedis=addCartToRedis(cart,userId);
                if (!addCartToRedis){
                    return Result.error("添加购物车失败！");
                }
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
        }

        return Result.error("添加购物车失败！");
    }

    private Boolean addCartToRedis(Map<String, TbItem> cart, String userId) {
       return cartServiceFeignClient.insertCart(cart,userId);
    }

    /**
     * cookie查看购物车
     */
    @RequestMapping("/showCart")
    public  Result showCart(String userId,HttpServletRequest
            request,HttpServletResponse response){
        try {
            if (StringUtils.isBlank(userId)){
                //在用户未登录的状态下
                List<TbItem> list = new ArrayList<>();
                Map<String, TbItem> cart = getCartFromCookie(request);
                Set<String> keys = cart.keySet();
                for (String key:keys){
                    list.add(cart.get(key));
                }
                return Result.ok(list);
            }else {
                // 在用户已登录的状态
            }

        }catch (Exception e){
            e.printStackTrace();
        }
       return  Result.error("查看购物车错误！");

    }
    @RequestMapping("/updateItemNum")
    public  Result updateItemNum(String userId,Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response){
       try {
           if (StringUtils.isBlank(userId)){
               //获取购物车
               Map<String, TbItem> cart = getCartFromCookie(request);
               //修改购物车的商品
               TbItem tbItem = cart.get(itemId.toString());
               tbItem.setNum(num);
               cart.put(itemId.toString(),tbItem);
               //将购物车放回cookie 中
               addClientCookie(request,response,cart);
           }else {
               //登录
           }
           return  Result.ok();
       }catch (Exception e){
        e.printStackTrace();
       }
       return  Result.error("购物车修改失败！");
    }
    /**
     * 删除购物车中的商品
     */
    @RequestMapping("/deleteItemFromCart")
    public  Result deleteItemFromCart(Long itemId, String userId, HttpServletRequest
            request, HttpServletResponse response){
        try {
            if (StringUtils.isBlank(userId)){
                //在用户未登录的状态下
                Map<String, TbItem> cart = getCartFromCookie(request);
                cart.remove(itemId.toString());
                addClientCookie(request,response,cart);
            }else {
                //登录
            }
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  Result.error("购物车删除失败！");

    }
    /**
     * 获取购物车
     * @param request
     * @return
     */
    private Map<String, TbItem> getCartFromCookie(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, CART_COOKIE_KEY, true);
        if (StringUtils.isNotBlank(cookieValue)){
            //购物车存在
            Map<String, TbItem> map = JsonUtils.jsonToMap(cookieValue,TbItem.class);
            return  map;
        }
            //购物车不存在
            return new HashMap<String, TbItem>();
    }

    /**
     * 将商品添加到购物车中
     * @param cart
     * @param itemId
     * @param num
     */
    private void addItemToCart(Map<String, TbItem> cart, Long itemId, Integer num) {
        //从购物车中取商品
        TbItem tbItem = cart.get(itemId.toString());
        if (tbItem !=null){
            //商品列表中存在该商品，商品数量相加。
            tbItem.setNum(tbItem.getNum()+num);
        }else{
            //商品列表中不存在该商品，根据商品id查询商品信息并添加到购车列表
             tbItem = itemServiceFeignClient.selectItemInfo(itemId);
             tbItem.setNum(num);
        }
        cart.put(itemId.toString(),tbItem);
    }
    /**
     * 把购车商品列表写入cookie
     * @param request
     * @param response
     * @param cart
     */
    private void addClientCookie(HttpServletRequest request, HttpServletResponse response, Map<String, TbItem> cart) {
        String cartJson = JsonUtils.objectToJson(cart);
        CookieUtils.setCookie(request,response,CART_COOKIE_KEY,cartJson, Math.toIntExact(CART_COOKIE_EXPIRE),true);

    }

    /**
     * redis查看购物车
     */
    private Map<String, TbItem> getcartFromRedis(String userId) {
        Map<String, TbItem> cart = cartServiceFeignClient.selectCartByUserId(userId);
        if (cart!=null && cart.size()>0){
            return  cart;
        }
        return  new HashMap<String, TbItem>();
    }
}

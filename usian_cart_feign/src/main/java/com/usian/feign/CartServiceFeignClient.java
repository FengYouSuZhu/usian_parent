package com.usian.feign;

import com.usian.pojo.TbItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/12 11:58
 */
@FeignClient("usian-cart-service")
public interface CartServiceFeignClient {

    @RequestMapping("/service/cart/selectCartByUserId")
    public Map<String, TbItem> selectCartByUserId(@RequestParam String userId);

    @RequestMapping("/service/cart/insertCart")
    Boolean insertCart(Map<String, TbItem> cart,@RequestParam String userId);
}

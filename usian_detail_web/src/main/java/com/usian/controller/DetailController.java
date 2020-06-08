package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/8 13:20
 */
@RestController
@RequestMapping("/frontend/detail")
public class DetailController {
    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;
    /**
     * 查询商品基本信息
     */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId){
        TbItem tbitem=itemServiceFeignClient.selectItemInfo(itemId);
        System.out.println("//////////////////////////////////////");
        if (tbitem != null) {
            return  Result.ok(tbitem);
        }
        return Result.error("查无结果");
    }
    /**
     * 查询商品介绍
     */
    @RequestMapping("/selectItemDescByItemId")
    public  Result selectItemDescByItemId(Long itemId){
       TbItemDesc tbItemDesc = itemServiceFeignClient.selectItemDescByItemId(itemId);
        if (tbItemDesc != null) {
            return  Result.ok(tbItemDesc);
        }
        return Result.error("查无结果");
    }
    /**
     * 根据商品 ID 查询商品规格参数
     */
    @RequestMapping("/selectTbItemParamItemByItemId")
    public  Result selectTbItemParamItemByItemId(Long itemId){
        TbItemParamItem itemParamItem=itemServiceFeignClient.selectTbItemParamItemByItemId(itemId);
        if (itemParamItem != null) {
            return  Result.ok(itemParamItem);
        }
        return Result.error("查无结果");
    }


}

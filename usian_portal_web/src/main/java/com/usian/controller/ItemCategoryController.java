package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.utils.CatResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/22 11:39
 */
@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll(){
       CatResult catResult = itemServiceFeignClient.selectItemCategoryAll();
        if (catResult.getData().size()>0){
            return  Result.ok(catResult);
        }
            return Result.error("查无结果");
    }
}

package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/17 18:00
 */
@RestController
@RequestMapping("/backend/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    /**
     * 根据类目 ID 查询当前类目的子节点
     */
    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(defaultValue = "0")Long id ){
        List<TbItemCat> list =  itemServiceFeignClient.selectItemCategoryByParentId(id);
        if (list !=null && list.size()>0){
            return  Result.ok(list);
        }
        return  Result.error("查无数据");
    }

}

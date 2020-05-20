package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItemParam;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/17 18:13
 */
@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {
    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable Long itemCatId){
        TbItemParam tbItemParam=itemServiceFeignClient.selectItemParamByItemCatId(itemCatId);
        if (tbItemParam !=null){
            return  Result.ok(tbItemParam);
        }
        return  Result.error("查无结果");
    }

    /**
     * 查询所有商品规格模板
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "3")Integer rows){
         PageResult pageResult=itemServiceFeignClient.selectItemParamAll(page,rows);
        if(pageResult.getResult().size() > 0){
            return Result.ok(pageResult);
        }
        return Result.error ("查无结果");
    }

    /**
     * 添加商品规格模板
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("/insertItemParam")
    public  Result insertItemParam(Long itemCatId,String paramData){
       Integer num=itemServiceFeignClient.insertItemParam(itemCatId,paramData);
        if (num==1){
            return Result.ok();
        }
        return  Result.error("添加失败：该类目已有规格模板");
    }

    /**
     * 商品规格模板删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteItemParamById")
    public Result deleteItemParamById(Long id){
        Integer num=itemServiceFeignClient.deleteItemParamById(id);
        if (num==1){
            return Result.ok();
        }
        return  Result.error("删除失败");
    }



}

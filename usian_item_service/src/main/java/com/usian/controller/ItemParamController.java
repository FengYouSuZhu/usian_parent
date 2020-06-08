package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/17 18:20
 */
@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品分类 ID 查询规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable Long itemCatId){
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    /**
     * 查询所有商品规格模板
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll(Integer page,Integer rows){
        return  itemParamService.selectItemParamAll(page,rows);
    }

    /**
     * 添加商品规格模板
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("/insertItemParam")
    public  Integer insertItemParam(Long itemCatId,String paramData){
        return  itemParamService.insertItemParam(itemCatId,paramData);
    }

    /**
     * 商品规格模板删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteItemParamById")
    public  Integer deleteItemParamById(Long id){
       return itemParamService.deleteItemParamById(id);
    }

    @RequestMapping("/selectItemDescByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId){
       return itemParamService.selectTbItemParamItemByItemId(itemId);
    }
}

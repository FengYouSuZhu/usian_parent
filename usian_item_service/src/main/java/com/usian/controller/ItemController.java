package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.service.ItemService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:44
 */
@RestController
@RequestMapping("/service/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long itemId){
        return this.itemService.selectItemInfo(itemId);
    }

    /**
     * 查询商品列表接口
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page,Integer rows){
       return itemService.selectTbItemAllByPage(page,rows);
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/insertTbItem")
    public  Integer insertTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return itemService.insertTbItem(tbItem,desc,itemParams);
    }

    /**
     * 删除商品
     * @param itemId
     * @return
     */
    @RequestMapping("/deleteItemById")
    public  Integer deleteItemById(Long itemId){
        return  itemService.deleteItemById(itemId);
    }

    /**
     * 修改前，数据回显
     * @param itemId
     * @return
     */
    @RequestMapping("/preUpdateItem")
    public Map<String,Object> preUpdateItem(Long itemId){
        return itemService.preUpdateItem(itemId);
    }

    @RequestMapping("/updateTbItem")
    public Integer updateTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return  itemService.updateTbItem(tbItem,desc,itemParams);
    }

    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(Long itemId){

     return itemService.selectItemDescByItemId(itemId);
    }
}

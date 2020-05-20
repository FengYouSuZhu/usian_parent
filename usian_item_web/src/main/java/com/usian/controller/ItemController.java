package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:48
 */
@RestController
@RequestMapping("/backend/item")
public class ItemController {
    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId) {
        TbItem tbItem = itemServiceFeignClient.selectItemInfo(itemId);
        if(tbItem != null){
            return Result.ok(tbItem);
        }
        return Result.error("查无结果");

    }

    /**
     * 查询商品列表接口
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1")Integer page,
                                        @RequestParam(defaultValue = "2")Integer rows) {
        PageResult pageResult = itemServiceFeignClient.selectTbItemAllByPage(page,rows);
        if (pageResult.getResult() != null &&
                pageResult.getResult().size() > 0) {
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");

    }
    /**
     * 添加商品
     */
    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
        Integer insertTbItemNum=itemServiceFeignClient.insertTbItem(tbItem,desc,itemParams);
        if(insertTbItemNum==3){
            return Result.ok();
        }
        return Result.error("添加失败");
    }
}

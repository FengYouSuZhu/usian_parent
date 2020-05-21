package com.usian.controller;

import com.usian.feign.ContentServiceFeignClient;
import com.usian.pojo.TbContent;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/21 16:50
 */
@RestController
@RequestMapping("/backend/content")
public class ContentController {
    @Autowired
    private ContentServiceFeignClient contentServiceFeignClient;

    /**
     * 根据分类 ID 查询分类内容
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "30")Integer rows,Long categoryId){
        PageResult pageResult=contentServiceFeignClient.selectTbContentAllByCategoryId(page,rows,categoryId);
        if (pageResult!=null && pageResult.getResult().size()>0){
            return  Result.ok(pageResult);
        }
            return  Result.error("根据分类 ID 查询分类内容");
    }

    /**
     * 内容管理 查询接口
     * @param tbContent
     * @return
     */
    @RequestMapping("/insertTbContent")
    public  Result insertTbContent(TbContent tbContent){
       Integer insertTbContentNum= contentServiceFeignClient.insertTbContent(tbContent);
        if (insertTbContentNum!=null){
            return  Result.ok();
        }
            return Result.error("添加失败");

    }

    /**
     * 删除分类下的内容
     * @param ids
     * @return
     */
    @RequestMapping("/deleteContentByIds")
    public  Result deleteContentByIds(Long ids){
        Integer deleteContentByIdsNum=contentServiceFeignClient.deleteContentByIds(ids);
        if (deleteContentByIdsNum == 1){
            return  Result.ok();
        }
            return  Result.error("删除失败");

    }
}

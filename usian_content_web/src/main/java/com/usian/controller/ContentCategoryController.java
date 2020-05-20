package com.usian.controller;

import com.usian.feign.ContentServiceFeignClient;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/20 19:12
 */
@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {
    @Autowired
    private ContentServiceFeignClient contentServiceFeignClient;

    /**
     * 根据当前节点 ID 查询子节点
     * @param id
     * @return
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(defaultValue = "0")Long id){
        List<TbContentCategory> list=contentServiceFeignClient.selectContentCategoryByParentId(id);
        if ( list.size() > 0) {
            return Result.ok(list);
        }
        return Result.error("查无结果");
    }
}

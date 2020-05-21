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

    /**
     *  添加内容分类
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(TbContentCategory tbContentCategory){
        Integer contentCategoryNum=contentServiceFeignClient.insertContentCategory(tbContentCategory);
        if (contentCategoryNum!=null){
            return  Result.ok();
        }
        return  Result.error("添加失败！");
    }

    /**
     * 删除内容分类
     * @param categoryId
     * @return
     */
    @RequestMapping("/deleteContentCategoryById")
    public  Result deleteContentCategoryById(Long categoryId){
      Integer num=  contentServiceFeignClient.deleteContentCategoryById(categoryId);
      if (num == 200){
          return  Result.ok();
      }
            return  Result.error("删除失败！");
    }

    /**
     * 内容分类修改
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/updateContentCategory")
    public  Result updateContentCategory(TbContentCategory tbContentCategory){
        Integer updateContentCategoryNum=contentServiceFeignClient.updateContentCategory(tbContentCategory);
        if (updateContentCategoryNum == 1){
            return  Result.ok();
        }
            return  Result.error("修改失败！");
    }
}

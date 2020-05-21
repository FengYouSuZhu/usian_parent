package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.service.ContentService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/21 17:16
 */
@RestController
@RequestMapping("/service/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * 内容管理	查询接口
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    @RequestMapping("/selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(Integer page,Integer rows,Long categoryId){
        return contentService.selectTbContentAllByCategoryId(page,rows,categoryId);
    }

    /**
     * 内容管理 添加接口
     * @param tbContent
     * @return
     */
    @RequestMapping("/insertTbContent")
    public  Integer insertTbContent(@RequestBody TbContent tbContent){
        return  contentService.insertTbContent(tbContent);
    }

    /**
     * 删除分类下的内容
     * @param ids
     * @return
     */
    @RequestMapping("/deleteContentByIds")
    public  Integer deleteContentByIds(Long ids){
        return  contentService.deleteContentByIds(ids);
    }

}

package com.usian.service;

import com.usian.pojo.TbContentCategory;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/20 19:25
 */
public interface ContentCategoryService {
    //根据当前节点 ID 查询子节点
    List<TbContentCategory> selectContentCategoryByParentId(Long id);
    //添加内容分类
    Integer insertContentCategory(TbContentCategory tbContentCategory);
    //分类内容管理删除接口
    Integer deleteContentCategoryById(Long categoryId);
    //内容分类修改
    Integer updateContentCategory(TbContentCategory tbContentCategory);
}

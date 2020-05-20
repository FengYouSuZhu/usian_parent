package com.usian.service;

import com.usian.pojo.TbContentCategory;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/20 19:25
 */
public interface ContentCategoryService {

    List<TbContentCategory> selectContentCategoryByParentId(Long id);
}

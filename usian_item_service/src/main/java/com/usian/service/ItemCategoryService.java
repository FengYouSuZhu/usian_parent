package com.usian.service;

import com.usian.pojo.TbItemCat;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/17 17:53
 */
public interface ItemCategoryService {
    List<TbItemCat> selectItemCategoryByParentId(Long id);
}

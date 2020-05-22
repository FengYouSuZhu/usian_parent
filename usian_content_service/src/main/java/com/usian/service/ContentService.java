package com.usian.service;

import com.usian.pojo.TbContent;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/21 17:16
 */
public interface ContentService {
    //内容管理	查询接口
    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);
    //内容管理 添加接口
    Integer insertTbContent(TbContent tbContent);
    //删除分类下的内容
    Integer deleteContentByIds(Long ids);
    //查询首页大广告位
    List<AdNode> selectFrontendContentByAD();
}

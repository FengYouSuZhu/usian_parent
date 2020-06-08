package com.usian.service;

import com.usian.pojo.SearchItem;

import java.io.IOException;
import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/4 23:24
 */
public interface SearchItemService {
    Boolean importAll();

    List<SearchItem> selectByq(String q, Long page, Integer pageSize) throws IOException;

    int addDocement(String msg) throws IOException;
}

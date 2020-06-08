package com.usian.mapper;

import com.usian.pojo.SearchItem;

import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/4 23:18
 */
public interface SearchItemMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(String itemId);
}

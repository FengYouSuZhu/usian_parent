package com.usian.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/15 13:30
 */
/**
 * 分页模型
 */
public class PageResult implements Serializable {
    private Integer pageIndex; //当前页
    private Long totalPage; //总页数
    private List result; //结果集

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }
}

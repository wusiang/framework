package com.xianmao.common.mybatis.support;

import java.util.List;

public class PageQuery {
    /**
     * 当前页
     */
    private Integer pageNo;

    /**
     * 每页的数量
     */
    private Integer pageSize;

    /**
     * 排序的字段名
     */
    private List<String> ascs;

    /**
     * 排序方式
     */
    private List<String> descs;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getAscs() {
        return ascs;
    }

    public void setAscs(List<String> ascs) {
        this.ascs = ascs;
    }

    public List<String> getDescs() {
        return descs;
    }

    public void setDescs(List<String> descs) {
        this.descs = descs;
    }
}

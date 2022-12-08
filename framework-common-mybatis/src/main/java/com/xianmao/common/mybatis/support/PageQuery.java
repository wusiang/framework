package com.xianmao.common.mybatis.support;

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
    private String ascs;

    /**
     * 排序方式
     */
    private String descs;

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

    public String getAscs() {
        return ascs;
    }

    public void setAscs(String ascs) {
        this.ascs = ascs;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }
}

package com.xianmao.support;

/**
 * @ClassName Query
 * @Description: TODO
 * @Author wjh
 * @Data 2020/11/9 12:25 上午
 * @Version 1.0
 */
public class Query {


    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页的数量
     */
    private Integer size;

    /**
     * 排序的字段名
     */
    private String ascs;

    /**
     * 排序方式
     */
    private String descs;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

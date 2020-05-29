package com.xianmao.support;

/**
 * @ClassName BeanProperty
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-30 21:06
 * @Version 1.0
 */
public class BeanProperty {

    private String name;
    private Class<?> type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}

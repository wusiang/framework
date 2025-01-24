package com.xianmao.common.core.enums;

import java.io.Serializable;

public interface IEnum<C, V> extends Serializable {

    /**
     * 获取 code 值
     *
     * @return code 值
     */
    C getCode();

    /**
     * 获取 value 值
     *
     * @return value 值
     */
    V getValue();
}
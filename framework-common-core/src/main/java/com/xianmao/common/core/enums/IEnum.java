package com.xianmao.common.core.enums;

import java.io.Serializable;

/**
 * @ClassName IEnum
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-21 13:35
 * @Version 1.0
 */
public interface IEnum<C, V> extends Serializable {
    /**
     * 获取code值
     *
     * @return code值
     */
    C getCode();

    /**
     * 获取value值
     *
     * @return value值
     */
    V getValue();
}

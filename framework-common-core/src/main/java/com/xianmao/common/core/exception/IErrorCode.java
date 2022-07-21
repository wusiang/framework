package com.xianmao.common.core.exception;

import java.io.Serializable;

public interface IErrorCode extends Serializable {
    /**
     * 获取code值
     *
     * @return code值
     */
    Integer getCode();

    /**
     * 获取value值
     *
     * @return value值
     */
    String getValue();

    default BussinessException exp() {
        return new BussinessException(getCode(), getValue());
    }

    default BussinessException exp(Exception e) {
        return new BussinessException(getCode(), getValue(), e);
    }

    default BussinessException exp(String... param) {
        return new BussinessException(getCode(), String.format(getValue(), param));
    }

    default BussinessException exp(String param) {
        return new BussinessException(getCode(), param);
    }
}

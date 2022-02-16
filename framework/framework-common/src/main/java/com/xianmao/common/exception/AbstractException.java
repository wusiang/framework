package com.xianmao.common.exception;

import com.xianmao.common.enums.IEnum;

import java.io.Serializable;

public class AbstractException extends RuntimeException implements Serializable {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public AbstractException() {
    }

    public AbstractException(String message, int code) {
        super(message);
        this.code = code;
    }

    public AbstractException(IEnum<Integer, String> iEnum) {
        super(iEnum.getValue());
        this.code = iEnum.getCode();
    }
}

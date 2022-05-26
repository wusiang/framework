package com.xianmao.common.core.exception;

import lombok.Data;

@Data
public class BussinessException extends RuntimeException {

    private Integer code;

    private String message;

    private Throwable source;

    public BussinessException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }

    public BussinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BussinessException(Integer code, String message, Throwable e) {
        super(message);
        this.code = code;
        this.message = message;
        this.source = e;
    }
}

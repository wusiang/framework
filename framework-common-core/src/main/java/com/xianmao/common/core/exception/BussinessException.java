package com.xianmao.common.core.exception;

import lombok.Data;

@Data
public class BussinessException extends RuntimeException {

    private ICode code;

    private String message;

    private Throwable source;

    public BussinessException(String message) {
        super(message);
        this.code = ServerErrorCode.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public BussinessException(ICode code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BussinessException(ICode code, String message, Throwable e) {
        super(message);
        this.code = code;
        this.message = message;
        this.source = e;
    }
}

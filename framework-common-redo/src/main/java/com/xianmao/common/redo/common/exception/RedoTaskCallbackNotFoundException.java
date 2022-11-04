package com.xianmao.common.redo.common.exception;

public class RedoTaskCallbackNotFoundException extends RuntimeException {

    public RedoTaskCallbackNotFoundException() {
        super("RedoTaskCallback Not Found!");
    }

    public RedoTaskCallbackNotFoundException(String message) {
        super(message);
    }

}

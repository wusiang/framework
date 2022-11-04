package com.xianmao.common.redo.common.exception;

public class RedoTaskNotFoundException extends RuntimeException {

    public RedoTaskNotFoundException() {
        super("RedoTask Not Found!");
    }

    public RedoTaskNotFoundException(String message) {
        super(message);
    }

}

package com.xianmao.common.redo.bean;

public class RedoResult {

    public static final RedoResult SUCCESS = new RedoResult(true);

    public static final RedoResult BIZ_ERROR = new RedoResult(false, "业务执行异常");
    public static final RedoResult REDOTASK_CALLBACK_NOT_FOUND = new RedoResult(false, "业务执行异常");

    private boolean success;
    private String message;

    public RedoResult(boolean success) {
        this.success = success;
    }

    public RedoResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}

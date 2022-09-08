package com.xianmao.common.core.exception;

import javax.servlet.http.HttpServletResponse;

public enum ServerErrorCode implements IErrorCode {

    /*** 通用错误码*/
    UNAUTHORIZED(101, "登录已失效"),
    BAD_REQUEST(400,"Bad Request"),
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "哎哟喂！服务都被您挤爆了.."),
    ;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态码对应说明文案
     */
    private final String value;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    ServerErrorCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}

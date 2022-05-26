package com.xianmao.common.core.exception;

import javax.servlet.http.HttpServletResponse;

public enum CommonErrorCode implements IErrorCode {

    /*** 通用错误码*/
    ATTACK(100, "非法访问"),
    UNAUTHORIZED(101, "登录已失效"),
    LOGGED_IN(102, "会话未注销，无需登录"),
    FORBIDDEN(103, "无权限"),
    FREQUENT_ACCESS_RESTRICTION(104, "频繁访问限制，请稍后重试"),
    REQ_REJECT(105, "请求被拒绝"),
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

    CommonErrorCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}

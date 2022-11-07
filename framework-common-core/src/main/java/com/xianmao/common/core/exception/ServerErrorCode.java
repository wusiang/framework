package com.xianmao.common.core.exception;

import javax.servlet.http.HttpServletResponse;

public enum ServerErrorCode implements IErrorCode {

    /*** 操作成功*/
    SUCCESS(HttpServletResponse.SC_OK, "操作成功", ErrorLevel.INFO),
    /*** 业务异常*/
    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "业务异常", ErrorLevel.ERROR),
    /*** 请求未授权*/
    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "请求未授权", ErrorLevel.ERROR),
    /*** 404 没找到请求*/
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 没找到请求", ErrorLevel.ERROR),
    /*** 消息不能读取*/
    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "消息不能读取", ErrorLevel.ERROR),
    /*** 不支持当前请求方法*/
    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "不支持当前请求方法", ErrorLevel.ERROR),
    /*** 不支持当前媒体类型*/
    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "不支持当前媒体类型", ErrorLevel.ERROR),
    /*** 请求被拒绝*/
    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "请求被拒绝", ErrorLevel.ERROR),
    /*** 服务器异常*/
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器异常", ErrorLevel.ERROR),
    /*** 缺少必要的请求参数*/
    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "缺少必要的请求参数", ErrorLevel.ERROR),
    /*** 请求参数类型错误*/
    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "请求参数类型错误", ErrorLevel.ERROR),
    /*** 请求参数绑定错误*/
    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "请求参数绑定错误", ErrorLevel.ERROR),
    /*** 参数校验失败*/
    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数校验失败", ErrorLevel.ERROR),
    ;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态码对应说明文案
     */
    private final String value;
    /**
     * 日志级别
     *
     */
    private final ErrorLevel level;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public ErrorLevel getLevel() {
        return level;
    }

    ServerErrorCode(Integer code, String value, ErrorLevel level) {
        this.code = code;
        this.value = value;
        this.level = level;
    }
}

package com.xianmao.rest;

import com.xianmao.enums.IEnum;

public enum ResultCode implements IEnum<Integer, String> {

    /*** 100 访问限制*/
    ATTACK(100, "非法访问"),
    UNAUTHORIZED(101, "登录已失效"),
    LOGGED_IN(102, "会话未注销，无需登录"),
    FORBIDDEN(103, "无权限"),
    FREQUENT_ACCESS_RESTRICTION(104, "频繁访问限制，请稍后重试"),
    REQ_REJECT(105, "请求被拒绝"),
    INTERNAL_SERVER_ERROR(106, "服务器异常"),

    /*** 200 请求成功*/
    SUCCESS(200, "操作成功"),

    /*** 300 - 资源、重定向、定位等提示**/
    RESOURCE_ALREADY_INVALID(300, "资源已失效"),
    FILE_EMPTY(301, "文件上传请求错误"),
    TYPE_CONVERT_ERROR(302, "类型转换错误"),

    /*** 通用错误码*/
    NOT_FOUND(404, "找不到路径"),
    METHOD_NOT_ALLOWED(405, "不允许此方法"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    NOT_ALLOW_NULL(418, "不允许为空"),
    NOT_ALLOWED_REPEAT(419, "不允许重复"),
    NO_RESOURCES(420, "资源不存在"),
    PARAM_VALID_ERROR(421, "参数错误"),
    ERROR_JSON(499, "错误JSON"),

    /*** 服务器错误*/
    BAD_REQUEST(500, "错误请求"),
    DATA_STRUCTURE(501, "数据结构异常"),
    DB_ERROR(502, "数据结构异常，请检查相应数据结构一致性"),
    CLIENT_FALLBACK(503, "哎哟喂！网络开小差了，请稍后重试..."),
    CLIENT_FALLBACK_ERROR(504, "哎哟喂！服务都被您挤爆了..."),
    ;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码对应说明文案
     */
    private String value;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    ResultCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}

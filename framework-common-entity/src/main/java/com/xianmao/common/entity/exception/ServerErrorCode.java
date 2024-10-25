package com.xianmao.common.entity.exception;

public enum ServerErrorCode implements ICode<String> {

    SUCCESS("200", "操作成功"),
    WARN("100000", "服务器异常"),
    ERROR("100400", "服务器异常");
    ;

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态码对应说明文案
     */
    private final String value;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    ServerErrorCode(String code, String value) {
        this.code = code;
        this.value = value;
    }
}

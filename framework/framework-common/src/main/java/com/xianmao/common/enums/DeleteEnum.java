package com.xianmao.common.enums;

public enum DeleteEnum implements IEnum<Integer, String> {

    UN_DELETE(0, "未删除"),
    DELETE(1, "已删除");

    DeleteEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    private final Integer code;

    private final String value;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
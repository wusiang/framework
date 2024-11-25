package com.xianmao.common.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class OperateLogEnums {
    /**
     * 操作类型
     */
    @Getter
    @AllArgsConstructor
    public enum OperateType {
        ADD("add", "新增"),
        DELETE("delete", "删除"),
        UPDATE("update", "修改"),
        LOGIN("login", "登录"),
        ;
        private final String type;
        private final String value;
    }
}

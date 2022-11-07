package com.xianmao.common.core.exception;

public enum ErrorLevel {

    /**
     * INFO
     */
    INFO(1),

    /**
     * ERROR
     */
    ERROR(2),

    /**
     * WARN
     */
    WARN(3),
    ;


    private final int level;

    ErrorLevel(int level) {
        this.level = level;
    }

    public static ErrorLevel find(String code) {
        for (ErrorLevel level : ErrorLevel.values()) {
            if (code.equals(level.getLevel() + "")) {
                return level;
            }
        }
        throw new RuntimeException(String.format("未找到code:%s，对应的错误级别", code));
    }

    public int getLevel() {
        return level;
    }
}

package com.xianmao.common.core.utils;

import java.text.MessageFormat;

public class AssertUtils {

    private AssertUtils() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 检测表达式若为True则抛出异常
     */
    public static void isTrue(boolean expression) {
        if (expression) {
            throw new RuntimeException();
        }
    }

    /**
     * 检测表达式若为True则抛出异常
     */
    public static void isTrue(boolean expression, RuntimeException e) {
        if (expression) {
            throw e;
        }
    }

    /**
     * 检测表达式若为True则抛出异常
     */
    public static void isTrue(boolean expression, Object errorMessage) {
        if (expression) {
            throw new RuntimeException(String.valueOf(errorMessage));
        }
    }

    /**
     * 检测表达式若为True则抛出异常
     */
    public static void isTrue(boolean expression, MessageFormat errorMessageTemplate, Object... errorMessageArgs) {
        if (expression) {
            throw new RuntimeException(errorMessageTemplate.format(errorMessageArgs));
        }
    }


    /**
     * 参数数据若为null则抛出异常
     */
    public static <T> T isNull(T obj) {
        if (obj == null) {
            throw new RuntimeException();
        }
        return obj;
    }

    /**
     * 参数数据若为null则抛出异常
     */
    public static <T> T isNull(T obj, Object message) {
        if (obj == null) {
            throw new RuntimeException(String.valueOf(message));
        }
        return obj;
    }

    /**
     * 参数数据若为null则抛出异常
     */
    public static <T> T isNull(T obj, MessageFormat errorMessageTemplate, Object... errorMessageArgs) {
        if (obj == null) {
            throw new RuntimeException(errorMessageTemplate.format(errorMessageArgs));
        }
        return obj;
    }

    /**
     * 参数数据若为null则抛出异常
     */
    public static <T> T isNull(T obj, RuntimeException e) {
        if (obj == null) {
            throw e;
        }
        return obj;
    }
}

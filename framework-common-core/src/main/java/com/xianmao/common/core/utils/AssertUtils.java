package com.xianmao.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xianmao.common.core.exception.BussinessException;

public class AssertUtils {

    private AssertUtils() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static void assertTrue(boolean expression) {
        assertTrue(expression, null, null, (Object[]) null);
    }

    public static void assertTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new BussinessException(getMessage(message, args, "[Assertion failed] - the expression must be true"));
        }
    }

    public static <T> T assertNotNull(T object) {
        return assertNotNull(object, null, null, (Object[]) null);
    }


    public static <T> T assertNotNull(T object, String message, Object... args) {
        if (object == null) {
            throw new BussinessException(getMessage(message, args, "[Assertion failed] - the argument is required; it must not be null"));
        }
        return object;
    }

    public static <T> T assertNull(T object) {
        return assertNull(object, null, null, (Object[]) null);
    }


    public static <T> T assertNull(T object, String message, Object... args) {
        if (object != null) {
            throw new BussinessException(getMessage(message, args, "[Assertion failed] - the object argument must be null"));
        }
        return object;
    }

    public static void notEmpty(Object[] array, String message) {
        if (ObjectUtil.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, String message) {
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text) {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    private static String getMessage(String message, Object[] args, String defaultMessage) {
        if (message == null) {
            message = defaultMessage;
        }
        if (args == null || args.length == 0) {
            return message;
        }
        return String.format(message, args);
    }
}

package com.xianmao.common.trace.utils;

import org.springframework.lang.Nullable;

public class StrUtils {

    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }
}

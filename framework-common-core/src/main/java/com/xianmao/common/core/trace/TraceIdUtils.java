package com.xianmao.common.core.trace;

import java.util.UUID;

public class TraceIdUtils {

    public static String getTraceId() {
        return UUID.randomUUID().toString();
    }
}

package com.xianmao.common.core.utils;

import java.util.Map;

public class ReplaceUtils {

    private static final String PREFIX_TAG = "\\$\\{";
    private static final String SUFFIX_TAG = "}";

    public static String replaceContent(String content, Map<String, Object> params) {
        for (String k : params.keySet()) {
            Object v = params.get(k);
            String key = PREFIX_TAG + k + SUFFIX_TAG;
            if (v instanceof CharSequence
                    || v instanceof Character
                    || v instanceof Boolean
                    || v instanceof Number) {
                content = content.replaceAll(key, v.toString());
            } else {
                content = content.replaceAll(key, v.toString());
                continue;
            }
        }
        return content;
    }
}

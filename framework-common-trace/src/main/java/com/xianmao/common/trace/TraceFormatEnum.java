package com.xianmao.common.trace;

import com.xianmao.common.trace.constants.Constants;

/**
 * trace 格式 枚举
 */
public enum TraceFormatEnum {

    /**
     * 当前项目名称
     */
    LOCAL_NAME {
        @Override
        public String getValue() {
            return Constants.LOCAL_NAME;
        }
    },

    /**
     * 为一个请求分配的ID号，用来标识一条请求链路。
     */
    TRACE_ID {
        @Override
        public String getValue() {
            return Constants.LEGACY_TRACE_ID_NAME;
        }
    },

    /**
     * web请求分配的ID号，用来标识一条前后请求链路。
     */
    REQUEST_ID {
        @Override
        public String getValue() {
            return Constants.LEGACY_REQUEST_ID_NAME;
        }
    };

    /**
     * 获取参数名称
     */
    public abstract String getValue();

}

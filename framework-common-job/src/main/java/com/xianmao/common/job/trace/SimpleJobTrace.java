package com.xianmao.common.job.trace;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SimpleJobTrace {
    private static Logger log = LoggerFactory.getLogger(SimpleJobTrace.class);

    /**
     * 任务启动trace链路记录
     */
    public static void executeTrace() {
        try {
            String traceId = MDC.get(TraceConstant.TRACE_ID);
            String spanId = MDC.get(TraceConstant.SPAN_ID);
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = RandomStringUtils.randomNumeric(16).toLowerCase();
            }
            if (!StringUtils.isNotBlank(spanId)) {
                spanId = RandomStringUtils.randomNumeric(16).toLowerCase();
            }
            MDC.put(TraceConstant.TRACE_ID, traceId);
            MDC.put(TraceConstant.SPAN_ID, spanId);
        } catch (Exception e) {
            log.warn("发送方启动trace链路记录失败。", e);
        }
    }

    /**
     * 任务结束删除trace
     */
    public static void removeTrace() {
        try {
            MDC.remove(TraceConstant.TRACE_ID);
            MDC.remove(TraceConstant.SPAN_ID);
        } catch (Exception e) {
            log.warn("任务结束删除trace失败。", e);
        }
    }
}

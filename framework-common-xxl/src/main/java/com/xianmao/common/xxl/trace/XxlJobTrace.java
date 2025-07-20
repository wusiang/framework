package com.xianmao.common.xxl.trace;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class XxlJobTrace {
    private static Logger log = LoggerFactory.getLogger(XxlJobTrace.class);

    /**
     * 任务启动trace链路记录
     */
    public static void executeTrace() {
        try {
            String traceId = MDC.get(TraceConstant.LEGACY_TRACE_ID_NAME);
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = traceIdString();
            }
            MDC.put(TraceConstant.LEGACY_TRACE_ID_NAME, traceId);
        } catch (Exception e) {
            log.warn("发送方启动trace链路记录失败。", e);
        }
    }

    public static String traceIdString() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        return getUUID(uuidStr, 16);
    }

    /**
     * 处理 traceId 长度
     */
    public static String getUUID(String uuid, int len) {
        if (0 >= len) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(uuid.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 任务结束删除trace
     */
    public static void removeTrace() {
        try {
            MDC.remove(TraceConstant.LEGACY_TRACE_ID_NAME);
        } catch (Exception e) {
            log.warn("任务结束删除trace失败。", e);
        }
    }
}

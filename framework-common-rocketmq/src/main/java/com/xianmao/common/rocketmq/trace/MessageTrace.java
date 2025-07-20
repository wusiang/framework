package com.xianmao.common.rocketmq.trace;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.shaded.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * trace链路记录
 */
@Slf4j
public class MessageTrace {

    /**
     * 发送方启动trace链路记录
     */
    public static String producerTrace() {
        String traceId = "";
        try {
            traceId = MDC.get(TraceConstant.LEGACY_TRACE_ID_NAME);
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = traceIdString();
            }
        } catch (Exception e) {
            log.warn("发送方启动trace链路记录失败。", e);
        }
        return traceId;
    }

    /**
     * 消息接收方收取trace链路记录
     */
    public static void consumerTrace(MessageView message) {
        try {
            String traceId = message.getProperties().get(TraceConstant.LEGACY_TRACE_ID_NAME);
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = traceIdString();
            }
            MDC.put(TraceConstant.LEGACY_TRACE_ID_NAME, traceId);
        } catch (Exception e) {
            log.warn("消息接收方收取trace链路记录失败。", e);
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
}

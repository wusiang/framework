package com.xianmao.common.rocketmq.trace;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * trace链路记录
 */
public class MessageTrace {

    private final static Logger log = LoggerFactory.getLogger(MessageTrace.class);

    /**
     * 发送方启动trace链路记录
     * @param message
     */
    public static void producerTrace(Message message){
        try {
            String traceId = MDC.get(TraceConstant.TRACE_ID);
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            message.putUserProperties(TraceConstant.TRACE_ID, traceId);
        } catch (Exception e) {
            log.warn("发送方启动trace链路记录失败。", e);
        }
    }

    /**
     * 消息接收方收取trace链路记录
     * @param message
     */
    public static void consumerTrace(Message message){
        try {
            String traceId = message.getUserProperties(TraceConstant.TRACE_ID);
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            MDC.put(TraceConstant.TRACE_ID, traceId);
        } catch (Exception e) {
            log.warn("消息接收方收取trace链路记录失败。", e);
        }
    }
}

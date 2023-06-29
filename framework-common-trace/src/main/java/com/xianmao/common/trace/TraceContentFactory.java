package com.xianmao.common.trace;

import com.xianmao.common.trace.constants.Constants;
import com.xianmao.common.trace.handles.TraceMetaObjectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求链路内容工厂
 */
public class TraceContentFactory {

    private static final Logger log = LoggerFactory.getLogger(TraceContentFactory.class);

    private static Map<String, TraceMetaObjectHandler> traceMetaObjectHandlerMap;

    public TraceContentFactory(Map<String, TraceMetaObjectHandler> traceMetaObjectHandlerMap) {
        TraceContentFactory.traceMetaObjectHandlerMap = traceMetaObjectHandlerMap;
    }

    /**
     * 储存本地 MDC
     */
    public static void storageMDC(Map<String, String> traceContentMap) {
        // 执行 TraceMetaObjectHandler 拓展器
        for (Map.Entry<String, TraceMetaObjectHandler> entry : traceMetaObjectHandlerMap.entrySet()) {
            TraceMetaObjectHandler handler = entry.getValue();
            handler.additionalFill(traceContentMap);
        }
        // 写入 MDC
        TraceContentFactory.putMDC(traceContentMap);
    }

    /**
     * 储存本地 MDC
     */
    public static void putMDC(Map<String, String> traceContentMap) {
        for (Map.Entry<String, String> entry : traceContentMap.entrySet()) {
            MDC.put(entry.getKey(), entry.getValue());
        }
        log.debug("[TraceContentFactory] 请求流量: traceId={}, RequestId={}", MDC.get(Constants.LEGACY_TRACE_ID_NAME), MDC.get(Constants.LEGACY_REQUEST_ID_NAME));
    }

    public Map<String, String> assemblyTraceContent() {
        return buildTraceContent();
    }

    /**
     * 获取 MDC 内容 同时添加 X-B3-ParentName 参数
     */
    private static Map<String, String> buildTraceContent() {
        Map<String, String> traceContentMap = MDC.getCopyOfContextMap();
        if (traceContentMap == null) {
            traceContentMap = new HashMap<>(16);
        }
        return traceContentMap;
    }

}

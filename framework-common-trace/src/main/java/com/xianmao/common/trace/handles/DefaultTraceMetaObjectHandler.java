package com.xianmao.common.trace.handles;

import com.xianmao.common.trace.constants.Constants;
import com.xianmao.common.trace.utils.StrUtils;
import com.xianmao.common.trace.utils.TraceIdUtils;

import java.util.Map;

/**
 * 默认日志格式处理器
 */
public class DefaultTraceMetaObjectHandler implements TraceMetaObjectHandler {

    @Override
    public void additionalFill(Map<String, String> traceContentMap) {
        String headerTraceId = traceContentMap.get(Constants.LEGACY_TRACE_ID_NAME);

        // 如果为空，则表示第一次访问，即上游服务端的请求
        if (StrUtils.isEmpty(headerTraceId)) {
            this.strictInsertFill(traceContentMap, Constants.LEGACY_TRACE_ID_NAME, TraceIdUtils.traceIdString());
        } else {
            this.strictInsertFill(traceContentMap, Constants.LEGACY_TRACE_ID_NAME, headerTraceId);
        }

        // "RequestID" 如果为空设置为 则不显示
        String headerRequestID = traceContentMap.get(Constants.LEGACY_REQUEST_ID_NAME);
        if (!StrUtils.isEmpty(headerRequestID)) {
            this.strictInsertFill(traceContentMap, Constants.LEGACY_REQUEST_ID_NAME, headerRequestID);
        }
    }

}

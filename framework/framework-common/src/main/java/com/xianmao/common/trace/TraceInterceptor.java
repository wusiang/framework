package com.xianmao.common.trace;

import com.xianmao.common.string.StringUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceConstant.TRACE_ID);
        if (StringUtil.isBlank(traceId)) {
            MDC.put(TraceConstant.TRACE_ID, UUID.randomUUID().toString());
        } else {
            MDC.put(TraceConstant.TRACE_ID, traceId);
        }
        return true;
    }

}

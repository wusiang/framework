package com.xianmao.boot.interceptor;

import com.xianmao.constant.TraceConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceConstant.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            MDC.put(TraceConstant.TRACE_ID, UUID.randomUUID().toString());
        } else {
            MDC.put(TraceConstant.TRACE_ID, traceId);
        }
        return true;
    }
}

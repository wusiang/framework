package com.xianmao.common.core.trace;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceHandlerInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceConstant.TRACE_ID);
        if (StrUtil.isBlank(traceId)) {
            traceId = TraceIdUtils.getTraceId();
        }
        MDC.put(TraceConstant.TRACE_ID, traceId);
        PageHelper.clearPage();
        return true;
    }
}

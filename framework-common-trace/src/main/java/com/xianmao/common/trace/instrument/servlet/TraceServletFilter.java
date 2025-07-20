package com.xianmao.common.trace.instrument.servlet;

import com.xianmao.common.trace.TraceContentFactory;
import com.xianmao.common.trace.TraceLogProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求拦截器 初始化 Trace 内容
 */
public class TraceServletFilter implements Filter {

    private final TraceLogProperties traceLogProperties;

    public TraceServletFilter(TraceLogProperties traceLogProperties) {
        this.traceLogProperties = traceLogProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        Map<String, String> formatMap = new HashMap<>(16);
        // 获取自定义参数
        Set<String> expandFormat = traceLogProperties.getFormat();
        for (String k : expandFormat) {
            formatMap.put(k, request.getHeader(k));
        }

        // 写入 MDC
        TraceContentFactory.storageMDC(formatMap);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }

}

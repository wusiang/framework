package com.xianmao.common.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;


import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class CachingContentFilter implements Filter {
    private static final String FORM_CONTENT_TYPE = "multipart/form-data";

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("CachingContentFilter初始化成功");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String contentType = request.getContentType();
        if (request instanceof HttpServletRequest) {
            //异常情况重复读流
            HttpServletRequest requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
            if (contentType != null && contentType.contains(FORM_CONTENT_TYPE)) {
                chain.doFilter(request, response);
            } else {
                chain.doFilter(requestWrapper, response);
            }
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
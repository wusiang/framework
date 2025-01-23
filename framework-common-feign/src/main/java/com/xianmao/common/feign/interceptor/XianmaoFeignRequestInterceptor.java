package com.xianmao.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * feign 传递Request header
 */
public class XianmaoFeignRequestInterceptor implements RequestInterceptor {

    /**
     * 请求和转发的ip
     */
    private static final Set<String> ALLOW_HEADS = new HashSet<>(Arrays.asList(
            "X-Real-IP", "x-forwarded-for", "x-tenant-id"
    ));

    /**
     * Feign GET 请求400 (一下正常200,一下异常400)
     * A服务的Post请求 -> feign -> B服务的Get    (问题复现) okhhtp解析请求头会报错
     * @link https://blog.csdn.net/u011504708/article/details/121315814
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            // 传递请求头
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String key = headerNames.nextElement();
                    // 只支持配置的 header
                    if (ALLOW_HEADS.contains(key)) {
                        String values = httpServletRequest.getHeader(key);
                        // header value 不为空的 传递
                        if (values != null && !values.isEmpty()) {
                            requestTemplate.header(key, values);
                        }
                    }
                }
            }
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 提前进行空值检查
        return attributes != null ? attributes.getRequest() : null;
    }
}
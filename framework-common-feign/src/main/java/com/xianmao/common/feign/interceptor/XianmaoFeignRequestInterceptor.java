package com.xianmao.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * feign 传递Request header
 */
public class XianmaoFeignRequestInterceptor implements RequestInterceptor {

    /**
     * 请求和转发的ip
     */
    private static final String[] ALLOW_HEADS = new String[]{
            "X-Real-IP", "x-forwarded-for", "x-tenant-id"
    };

    /**
     * Feign GET 请求400 (一下正常200,一下异常400)
     * A服务的Post请求 -> feign -> B服务的Get    (问题复现) okhhtp解析请求头会报错
     * @link https://blog.csdn.net/u011504708/article/details/121315814
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (null != httpServletRequest) {
            // 传递请求头
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            List<String> allowHeadsList = new ArrayList<>(Arrays.asList(ALLOW_HEADS));
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String key = headerNames.nextElement();
                    // 只支持配置的 header
                    if (allowHeadsList.contains(key)) {
                        String values = httpServletRequest.getHeader(key);
                        // header value 不为空的 传递
                        if (null != values && !values.isEmpty()) {
                            requestTemplate.header(key, values);
                        }
                    }
                }
            }
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }
}

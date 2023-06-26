package com.xianmao.cloud.feign;

import com.alibaba.nacos.common.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * feign 传递Request header
 */
public class TraceFeignRequestInterceptor implements RequestInterceptor {

    private final static String TRACE_ID = "traceId";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (null != httpServletRequest) {
            //获取头信息
            Map<String, String> headers = getHeaders(httpServletRequest);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestTemplate.header(entry.getKey(), entry.getValue());
            }
            //链路追踪ID
            String traceId = MDC.get(TRACE_ID);
            if (StringUtils.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
            }
            requestTemplate.header(TRACE_ID, traceId);
        }
    }

    /**
     * 获取头信息
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }
}

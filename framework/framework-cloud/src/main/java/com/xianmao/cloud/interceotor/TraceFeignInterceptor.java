package com.xianmao.cloud.interceotor;

import com.xianmao.constant.TraceConstant;
import com.xianmao.utils.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TraceFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = MDC.get(TraceConstant.TRACE_ID);
        if (StringUtil.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        requestTemplate.header(TraceConstant.TRACE_ID, traceId);
    }
}

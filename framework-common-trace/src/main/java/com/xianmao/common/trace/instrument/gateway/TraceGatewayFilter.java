package com.xianmao.common.trace.instrument.gateway;

import com.xianmao.common.trace.TraceContentFactory;
import com.xianmao.common.trace.TraceLogProperties;
import com.xianmao.common.trace.constants.Constants;
import com.xianmao.common.trace.utils.TraceIdUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应 TraceId 处理器
 */
public class TraceGatewayFilter implements GlobalFilter, Ordered {

    private final TraceLogProperties traceLogProperties;

    public TraceGatewayFilter(TraceLogProperties traceLogProperties) {
        this.traceLogProperties = traceLogProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 传递自定义参数header
        Map<String, String> formatMap = new HashMap<>(16);
        Set<String> expandFormat = traceLogProperties.getFormat();
        for (String k : expandFormat) {
            if (k.equals(Constants.LEGACY_TRACE_ID_NAME)) {
                formatMap.put(k, TraceIdUtils.traceIdString());
            } else {
                formatMap.put(k, exchange.getRequest().getHeaders().getFirst(k));
            }
        }
        if (!CollectionUtils.isEmpty(formatMap)) {
            for (Map.Entry<String, String> entry : formatMap.entrySet()) {
                exchange.getRequest().mutate().header(entry.getKey(), entry.getValue()).build();
            }
        }
        // 2.写入 MDC
        TraceContentFactory.storageMDC(formatMap);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}

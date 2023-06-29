package com.xianmao.common.trace.instrument.gateway;

import com.xianmao.common.trace.TraceLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(GlobalFilter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class TraceGatewayAutoConfiguration {

	@Bean
	public TraceGatewayFilter traceGatewayFilter(TraceLogProperties traceLogProperties) {
		return new TraceGatewayFilter(traceLogProperties);
	}

}

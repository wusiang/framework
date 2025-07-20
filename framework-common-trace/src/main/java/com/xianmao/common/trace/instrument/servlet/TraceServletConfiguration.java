package com.xianmao.common.trace.instrument.servlet;

import com.xianmao.common.trace.TraceLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TraceServletConfiguration {

	@Bean
	public TraceServletFilter traceServletFilter(TraceLogProperties traceLogProperties) {
		return new TraceServletFilter(traceLogProperties);
	}

}

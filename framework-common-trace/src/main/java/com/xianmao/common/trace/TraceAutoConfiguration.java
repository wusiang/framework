package com.xianmao.common.trace;

import com.xianmao.common.trace.handles.DefaultTraceMetaObjectHandler;
import com.xianmao.common.trace.handles.TraceMetaObjectHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TraceLogProperties.class)
public class TraceAutoConfiguration {

	@Bean
	public TraceContentFactory traceContentFactory(Map<String, TraceMetaObjectHandler> traceMetaObjectHandlerMap) {
		return new TraceContentFactory(traceMetaObjectHandlerMap);
	}

	@Bean
	public DefaultTraceMetaObjectHandler defaultTraceMetaObjectHandler() {
		return new DefaultTraceMetaObjectHandler();
	}

}

package com.xianmao.common.trace.instrument.feign;

import com.xianmao.common.trace.TraceContentFactory;
import feign.Client;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
@ConditionalOnClass({ Client.class, TraceContentFactory.class })
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class TraceFeignClientAutoConfiguration {

	@Bean
	public TraceFeignRequestInterceptor basicAuthRequestInterceptor(TraceContentFactory traceContentFactory) {
		return new TraceFeignRequestInterceptor(traceContentFactory);
	}

}

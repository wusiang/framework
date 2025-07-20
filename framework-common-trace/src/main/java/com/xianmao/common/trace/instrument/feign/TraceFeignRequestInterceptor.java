package com.xianmao.common.trace.instrument.feign;

import com.xianmao.common.trace.TraceContentFactory;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Map;

public class TraceFeignRequestInterceptor implements RequestInterceptor {

	private final TraceContentFactory traceContentFactory;

	public TraceFeignRequestInterceptor(TraceContentFactory traceContentFactory) {
		this.traceContentFactory = traceContentFactory;
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		Map<String, String> copyOfContextMap = traceContentFactory.assemblyTraceContent();
		for (Map.Entry<String, String> copyOfContext : copyOfContextMap.entrySet()) {
			requestTemplate.header(copyOfContext.getKey(), copyOfContext.getValue());
		}
	}

}

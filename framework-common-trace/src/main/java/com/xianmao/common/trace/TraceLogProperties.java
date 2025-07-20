package com.xianmao.common.trace;

import com.xianmao.common.trace.constants.Constants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 链路追踪配置
 */
@ConfigurationProperties(prefix = "spring.trace.log")
public class TraceLogProperties implements InitializingBean {

    /**
     * 日志格式顺序
     */
    private Set<String> format = new HashSet<>();

    public Set<String> getFormat() {
        return format;
    }

    public void setFormat(Set<String> format) {
        this.format = format;
    }

    /**
     * X-TraceId,X-SpanId
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (0 == format.size()) {
            format.add(Constants.LEGACY_TRACE_ID_NAME);
            format.add(Constants.LEGACY_REQUEST_ID_NAME);
        }
    }

}

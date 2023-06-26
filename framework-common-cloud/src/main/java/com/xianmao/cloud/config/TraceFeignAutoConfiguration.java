package com.xianmao.cloud.config;

import com.xianmao.cloud.feign.TraceFeignRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TraceFeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TraceFeignRequestInterceptor basicAuthRequestInterceptor() {
        return new TraceFeignRequestInterceptor();
    }

}

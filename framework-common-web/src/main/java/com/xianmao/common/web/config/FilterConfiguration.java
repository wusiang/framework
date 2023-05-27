package com.xianmao.common.web.config;

import com.xianmao.common.web.filter.CachingContentFilter;
import com.xianmao.common.web.filter.TraceFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    /**
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<CachingContentFilter> cachingContentFilterFilterRegistrationBean() {
        FilterRegistrationBean<CachingContentFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CachingContentFilter());
        return registrationBean;
    }

    /**
     * 链路追踪trace
     * @return
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilterFilterRegistrationBean() {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter());
        return registrationBean;
    }
}

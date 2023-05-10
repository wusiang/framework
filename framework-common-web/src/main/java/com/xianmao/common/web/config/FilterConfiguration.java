package com.xianmao.common.web.config;

import com.xianmao.common.web.filter.CachingContentFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<CachingContentFilter> cachingContentFilterFilterRegistrationBean() {
        FilterRegistrationBean<CachingContentFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CachingContentFilter());
        return registrationBean;
    }
}

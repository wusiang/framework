package com.xianmao.common.web.config;

import com.xianmao.common.web.filter.CachingContentFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
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
}

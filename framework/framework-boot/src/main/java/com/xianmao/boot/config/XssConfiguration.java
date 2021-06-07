package com.xianmao.boot.config;

import com.xianmao.xss.XssFilter;
import com.xianmao.xss.XssProperties;
import com.xianmao.xss.XssUrlProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

/**
 * @ClassName XssConfiguration
 * @Description: Xss配置
 *               需要在yml配置xss.enabled=true
 * @Author wjh
 * @Data 2021/5/29 5:41 下午
 * @Version 1.0
 */
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
@EnableConfigurationProperties({XssProperties.class, XssUrlProperties.class})
public class XssConfiguration {

    private final XssProperties xssProperties;
    private final XssUrlProperties xssUrlProperties;

    /**
     * 防XSS注入
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter(xssProperties, xssUrlProperties));
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}

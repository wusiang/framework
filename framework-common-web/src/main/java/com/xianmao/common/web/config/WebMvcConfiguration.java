package com.xianmao.common.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    /**
     * 跨域配置
     * 通过重写 addCorsMappings 方法实现跨域配置的支持，使用 CorsRegistry 注册类添加路径映射。示例代码如下：
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//配置允许跨域的路径
                .allowedOrigins("*")//配置允许访问的跨域资源的请求域名
                .allowedMethods("PUT,POST,GET,DELETE,OPTIONS")//配置允许访问该跨域资源服务器的请求方法，如：POST、GET、PUT、DELETE等
                .allowedHeaders("*"); //配置允许请求header的访问，如 ：X-TOKEN
        super.addCorsMappings(registry);
    }
}

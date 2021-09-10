package com.xianmao.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xianmao.jackson.BladeBeanSerializerModifier;
import com.xianmao.trace.TraceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TraceInterceptor())
                .addPathPatterns("/**");

    }

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

    /**
     * APIResult返回值去除为NULL的字段
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, new com.fasterxml.jackson.databind.ser.std.ToStringSerializer());
        simpleModule.addSerializer(Long.TYPE, new com.fasterxml.jackson.databind.ser.std.ToStringSerializer());
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(customJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        // 针对null处理设置默认值BladeBeanSerializerModifier
        objectMapper.setSerializerFactory(objectMapper.copy().getSerializerFactory().withSerializerModifier(new BladeBeanSerializerModifier()));
        // 去除null object
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 字段不存在时不报异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
}

package com.xianmao.cloud.client;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * Cloud启动注解配置
 *
 * @author Chill
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xianmao"})
public @interface XianmaoCloudApplication {
}

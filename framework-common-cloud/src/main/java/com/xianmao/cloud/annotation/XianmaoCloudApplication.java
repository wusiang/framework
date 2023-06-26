package com.xianmao.cloud.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xianmao.**.api.feign"})
@SpringBootApplication(scanBasePackages = "com.xianmao")
public @interface XianmaoCloudApplication {
}

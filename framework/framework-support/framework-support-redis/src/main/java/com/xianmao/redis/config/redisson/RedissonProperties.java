package com.xianmao.redis.config.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson配置类
 * 重写org.redisson.spring.starter.RedissonProperties
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

    private String config;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

}

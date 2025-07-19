package com.xianmao.common.redis.config;

import com.xianmao.common.redis.util.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@EnableCaching
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisTemplateConfiguration {

    /**
     * 主RedisTemplate配置（增强兼容性）
     */
    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate") // 更精确的条件控制
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 1. 序列化配置
        RedisSerializer<Object> valueSerializer = new FastJson2JsonRedisSerializer(Object.class);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer);
        // 2. 必须优先设置ConnectionFactory
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 3. 后处理（修正原顺序错误）
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * Redis工具类Bean（增加条件判断）
     */
    @Bean
    @ConditionalOnMissingBean // 允许用户自定义实现
    public RedisUtil redisUtils(RedisTemplate<String, Object> redisTemplate) {
        return new RedisUtil(redisTemplate);
    }
}

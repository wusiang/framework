package com.xianmao.redis.config;

import com.xianmao.redis.config.cache.RedisCacheConfiguration;
import com.xianmao.redis.config.jedis.JedisConnectionConfiguration;
import com.xianmao.redis.config.redisson.RedissonAutoConfiguration;
import com.xianmao.redis.config.lettuce.LettuceConnectionConfiguration;
import com.xianmao.redis.util.ApplicationContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis自动配置
 *
 * @since 1.8
 */
@Configuration
@ConditionalOnClass({RedisTemplate.class})
@Import({
        ApplicationContextUtil.class,
        LettuceConnectionConfiguration.class,
        JedisConnectionConfiguration.class,
        RedissonAutoConfiguration.class,
        RedisCacheConfiguration.class
})
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({RedisTemplate.class})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        JsonRedisSerializer jsonRedisSerializer = new JsonRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(jsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean({StringRedisTemplate.class})
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}

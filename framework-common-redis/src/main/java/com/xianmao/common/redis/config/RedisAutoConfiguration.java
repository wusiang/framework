package com.xianmao.common.redis.config;

import com.xianmao.common.redis.config.cache.RedisCacheConfiguration;
import com.xianmao.common.redis.config.jedis.JedisConnectionConfiguration;
import com.xianmao.common.redis.config.lettuce.LettuceConnectionConfiguration;
import com.xianmao.common.redis.config.redisson.RedissonAutoConfiguration;
import com.xianmao.common.redis.util.ApplicationContextUtil;
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
 * @author wujh
 * @date 2019/4/18
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

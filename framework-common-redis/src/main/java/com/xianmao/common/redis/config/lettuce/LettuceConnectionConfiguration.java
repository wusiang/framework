package com.xianmao.common.redis.config.lettuce;

import com.xianmao.common.redis.config.RedisConnectionConfiguration;
import io.lettuce.core.RedisClient;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.util.StringUtils;

/**
 * lettuce连接配置
 * 重写org.springframework.boot.autoconfigure.data.redis.LettuceConnectionConfiguration
 * @author wujh
 * @date
 * @since 1.8
 */
@Configuration
@ConditionalOnClass({GenericObjectPool.class, RedisClient.class})
@AutoConfigureAfter({RedisProperties.class})
@EnableConfigurationProperties({RedisProperties.class})
public class LettuceConnectionConfiguration extends RedisConnectionConfiguration {

    public LettuceConnectionConfiguration(
            RedisProperties properties,
            ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
            ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider
    ) {
        super(properties, sentinelConfigurationProvider, clusterConfigurationProvider);
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean({ClientResources.class})
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean({RedisConnectionFactory.class})
    public RedisConnectionFactory redisConnectionFactory(
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
            ClientResources clientResources
    ) {
        LettuceClientConfiguration clientConfig = this.getLettuceClientConfiguration(
                builderCustomizers,
                clientResources,
                this.getProperties().getLettuce().getPool()
        );
        return createLettuceConnectionFactory(clientConfig);
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration) {
        if (getSentinelConfig() != null) {
            return new LettuceConnectionFactory(getSentinelConfig(), clientConfiguration);
        }
        if (getClusterConfiguration() != null) {
            return new LettuceConnectionFactory(getClusterConfiguration(), clientConfiguration);
        }
        return new LettuceConnectionFactory(getStandaloneConfig(), clientConfiguration);
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(
            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
            ClientResources clientResources, RedisProperties.Pool pool) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = createBuilder(pool);
        applyProperties(builder);
        if (StringUtils.hasText(getProperties().getUrl())) {
            customizeConfigurationFromUrl(builder);
        }
        builder.clientResources(clientResources);
        builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder.build();
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder createBuilder(RedisProperties.Pool pool) {
        if (pool == null) {
            return LettuceClientConfiguration.builder();
        }
        return LettucePoolingClientConfiguration.builder().poolConfig(
                this.getPoolConfig(pool, new GenericObjectPoolConfig<>())
        );
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder applyProperties(
            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        if (getProperties().isSsl()) {
            builder.useSsl();
        }
        if (getProperties().getTimeout() != null) {
            builder.commandTimeout(getProperties().getTimeout());
        }
        if (getProperties().getLettuce() != null) {
            RedisProperties.Lettuce lettuce = getProperties().getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(getProperties().getLettuce().getShutdownTimeout());
            }
        }
        return builder;
    }

    private void customizeConfigurationFromUrl(LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        RedisConnectionConfiguration.ConnectionInfo connectionInfo = parseUrl(getProperties().getUrl());
        if (connectionInfo.isUseSsl()) {
            builder.useSsl();
        }
    }
}

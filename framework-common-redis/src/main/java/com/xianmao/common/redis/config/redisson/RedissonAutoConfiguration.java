package com.xianmao.common.redis.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * redisson连接配置
 * 重写org.redisson.spring.starter.RedissonAutoConfiguration
 * @author wujh
 * @date 2019/5/10
 * @since 1.8
 */
@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@EnableConfigurationProperties({RedissonProperties.class, RedisProperties.class})
public class RedissonAutoConfiguration {
    @Autowired
    private RedissonProperties redissonProperties;
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private ApplicationContext ctx;

    public RedissonAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({RedisConnectionFactory.class})
    public RedisConnectionFactory redisConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean({RedissonClient.class})
    public RedissonClient redisson() {
        return Redisson.create(this.createConfig(null));
    }

    public RedissonConnectionConfiguration createConfig(Integer dbIndex) {
        if (dbIndex==null) {
            dbIndex = this.redisProperties.getDatabase();
        }
        RedissonConnectionConfiguration redissonConnectionConfiguration;
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, this.redisProperties);
        int timeout;
        Method nodesMethod;
        if (null == timeoutValue) {
            timeout = 0;
        } else if (!(timeoutValue instanceof Integer)) {
            nodesMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
            timeout = ((Long)ReflectionUtils.invokeMethod(nodesMethod, timeoutValue)).intValue();
        } else {
            timeout = (Integer)timeoutValue;
        }

        if (this.redissonProperties.getConfig() != null) {
            try {
                InputStream is = this.getConfigStream();
                redissonConnectionConfiguration = RedissonConnectionConfiguration.fromJSON(is);
            } catch (IOException e) {
                try {
                    InputStream is = this.getConfigStream();
                    redissonConnectionConfiguration = RedissonConnectionConfiguration.fromYAML(is);
                } catch (IOException e1) {
                    throw new IllegalArgumentException("Can't parse redissonConnectionConfiguration", e1);
                }
            }
        } else if (this.redisProperties.getSentinel() != null) {
            nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, this.redisProperties.getSentinel());
            String[] nodes;
            if (nodesValue instanceof String) {
                nodes = this.convert(Arrays.asList(((String)nodesValue).split(",")));
            } else {
                nodes = this.convert((List)nodesValue);
            }

            redissonConnectionConfiguration = new RedissonConnectionConfiguration();
            redissonConnectionConfiguration.useSentinelServers().setMasterName(this.redisProperties.getSentinel().getMaster()).addSentinelAddress(nodes).setDatabase(dbIndex).setConnectTimeout(timeout).setPassword(this.redisProperties.getPassword());
        } else {
            Method method;
            if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, this.redisProperties) != null) {
                Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, this.redisProperties);
                method = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
                List<String> nodesObject = (List)ReflectionUtils.invokeMethod(method, clusterObject);
                String[] nodes = this.convert(nodesObject);
                redissonConnectionConfiguration = new RedissonConnectionConfiguration();
                redissonConnectionConfiguration.useClusterServers().addNodeAddress(nodes).setConnectTimeout(timeout).setPassword(this.redisProperties.getPassword());
            } else {
                redissonConnectionConfiguration = new RedissonConnectionConfiguration();
                String prefix = "redis://";
                method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
                if (method != null && (Boolean)ReflectionUtils.invokeMethod(method, this.redisProperties)) {
                    prefix = "rediss://";
                }

                redissonConnectionConfiguration.useSingleServer().setAddress(prefix + this.redisProperties.getHost() + ":" + this.redisProperties.getPort()).setConnectTimeout(timeout).setDatabase(dbIndex).setPassword(this.redisProperties.getPassword());
            }
        }
        return redissonConnectionConfiguration;
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        Iterator var3 = nodesObject.iterator();

        while(true) {
            while(var3.hasNext()) {
                String node = (String)var3.next();
                if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                    nodes.add("redis://" + node);
                } else {
                    nodes.add(node);
                }
            }
            return nodes.toArray(new String[nodes.size()]);
        }
    }

    private InputStream getConfigStream() throws IOException {
        Resource resource = this.ctx.getResource(this.redissonProperties.getConfig());
        InputStream is = resource.getInputStream();
        return is;
    }
}
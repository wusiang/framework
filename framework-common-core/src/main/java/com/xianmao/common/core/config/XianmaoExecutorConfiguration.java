package com.xianmao.common.core.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.xianmao.common.core.props.AsyncProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
@ConditionalOnProperty(value = "xianmao.async.enabled", havingValue = "true")
@EnableConfigurationProperties({AsyncProperties.class})
public class XianmaoExecutorConfiguration {

    private final AsyncProperties asyncProperties;

    public ThreadPoolExecutor getPool() {
        /**
         * 自定义线程名称,方便的出错的时候溯源
         */
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix(asyncProperties.getNameFormat() + "-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                asyncProperties.getCorePoolSize(),
                asyncProperties.getMaxPoolSize(),
                asyncProperties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(asyncProperties.getQueueCapacity()),
                namedThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}

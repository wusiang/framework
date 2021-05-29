package com.xianmao.boot.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.xianmao.boot.props.XianmaoAsyncProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({
        XianmaoAsyncProperties.class
})
public class XianmaoExecutorConfiguration {

    @Autowired
    private XianmaoAsyncProperties xianmaoAsyncProperties;

    /**
     * 自定义线程名称,方便的出错的时候溯源
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix(xianmaoAsyncProperties.getNameFormat() + "-%d").build();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            xianmaoAsyncProperties.getCorePoolSize(),
            xianmaoAsyncProperties.getMaxPoolSize(),
            xianmaoAsyncProperties.getKeepAliveSeconds(),
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(xianmaoAsyncProperties.getQueueCapacity()),
            namedThreadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    public ThreadPoolExecutor getPool() {
        return executor;
    }
}

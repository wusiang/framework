package com.xianmao.boot.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异步配置
 * @author Chill
 */

@Setter
@Getter
@ConfigurationProperties("xianmao.async")
public class XianmaoAsyncProperties {
    /**
     * 异步核心线程数，默认：2
     */
    private int corePoolSize = 2;
    /**
     * 异步最大线程数，默认：50
     */
    private int maxPoolSize = 50;
    /**
     * 队列容量，默认：10000
     */
    private int queueCapacity = 10000;
    /**
     * 线程存活时间，默认：300
     */
    private int keepAliveSeconds = 300;
    /**
     * 线程名称-方便的出错的时候溯源
     */
    private String nameFormat = "thread";
}

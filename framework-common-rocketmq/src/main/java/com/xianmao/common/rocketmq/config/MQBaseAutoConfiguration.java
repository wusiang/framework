package com.xianmao.common.rocketmq.config;

import com.xianmao.common.rocketmq.base.AbstractMQProducer;
import com.xianmao.common.rocketmq.base.AbstractMQPushConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ配置文件
 */
@Configuration
@AutoConfigureAfter({AbstractMQProducer.class, AbstractMQPushConsumer.class})
@EnableConfigurationProperties(MqProperties.class)
public class MQBaseAutoConfiguration implements ApplicationContextAware {
    protected MqProperties mqProperties;

    @Autowired
    public void setMqConfig(MqProperties mqProperties) {
        this.mqProperties = mqProperties;
    }

    protected ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

}


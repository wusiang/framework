package com.xianmao.common.rocketmq.config;


import com.xianmao.common.rocketmq.base.AbstractMQProducer;
import com.xianmao.common.rocketmq.base.AbstractMQPushConsumerProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * RocketMQ配置文件
 */
@Configuration
@AutoConfigureAfter({AbstractMQProducer.class, AbstractMQPushConsumerProxy.class})
@EnableConfigurationProperties(MqProperties.class)
public class MQBaseAutoConfiguration implements ApplicationContextAware {
    protected MqProperties mqProperties;

    @Qualifier("mqProperties")
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


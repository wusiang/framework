package com.xianmao.common.rocketmq.config;


import com.xianmao.common.rocketmq.annotation.MQProducer;
import lombok.Getter;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 自动装配消息生产者
 */
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQProducerAutoConfiguration extends MQBaseAutoConfiguration {

    @Getter
    private static Producer producer;

    @Bean
    public Producer exposeProducer() throws ClientException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQProducer.class);
        if (CollectionUtils.isEmpty(beans)) {
            return null;
        }
        if (producer == null) {
            Assert.notNull(mqProperties.getUsername(), "producer username must be defined");
            Assert.notNull(mqProperties.getPassword(), "producer password must be defined");
            Assert.notNull(mqProperties.getNamesrvaddr(), "producer nameSrvAddr address must be defined");
            final ClientServiceProvider provider = ClientServiceProvider.loadService();
            producer = provider.newProducerBuilder()
                    .setClientConfiguration(mqProperties.clientConfiguration())
                    .build();
        }
        return producer;
    }
}

package com.xianmao.common.rocketmq.config;

import com.xianmao.common.rocketmq.annotation.MQProducer;
import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 自动装配消息生产者
 */
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQProducerAutoConfiguration extends MQBaseAutoConfiguration {

    private static Producer producer;

    public static Producer getProducer() {
        return producer;
    }

    @Bean
    public Producer exposeProducer() throws ClientException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQProducer.class);
        if (CollectionUtils.isEmpty(beans)) {
            return null;
        }
        if (producer == null) {
            Assert.notNull(mqProperties.getAccessKey(), "producer accessKey must be defined");
            Assert.notNull(mqProperties.getSecretKey(), "producer secretKey must be defined");
            Assert.notNull(mqProperties.getNameSrvAddr(), "producer nameSrvAddr address must be defined");
            SessionCredentialsProvider sessionCredentialsProvider = new StaticSessionCredentialsProvider(mqProperties.getAccessKey(), mqProperties.getSecretKey());
            ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                    .setEndpoints(mqProperties.getNameSrvAddr())
                    .setCredentialProvider(sessionCredentialsProvider)
                    .build();
            final ClientServiceProvider provider = ClientServiceProvider.loadService();
            producer = provider.newProducerBuilder()
                    .setClientConfiguration(clientConfiguration)
                    // Set the topic name(s), which is optional. It makes producer could prefetch the topic route before
                    // message publishing.
                    //.setTopics("")
                    // May throw {@link ClientException} if the producer is not initialized.
                    .build();
        }
        return producer;
    }
}

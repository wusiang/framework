package com.xianmao.common.rocketmq.config;

import com.xianmao.common.rocketmq.annotation.MQConsumer;
import com.xianmao.common.rocketmq.base.AbstractMQPushConsumer;
import com.xianmao.common.rocketmq.base.BaseMQ;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动装配消息消费者
 */
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQConsumerAutoConfiguration extends MQBaseAutoConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    private final static Logger log = LoggerFactory.getLogger(MQConsumerAutoConfiguration.class);

    private final static ClientServiceProvider provider = ClientServiceProvider.loadService();

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQConsumer.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            checkMessageType(entry);
            publishConsumer(entry.getKey(), entry.getValue());
        }
    }

    private void checkMessageType(Map.Entry<String, Object> entry) {
        Type superType = entry.getValue().getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
            Type messageType = actualTypeArguments[0];
            try {
                Assert.isTrue(BaseMQ.class.isAssignableFrom(Class.forName(messageType.getTypeName())), String.format("%s must extends BaseMQ", messageType.getTypeName()));
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFoundException", e);
            }
        }
    }

    private void publishConsumer(String beanName, Object bean) throws Exception {
        MQConsumer mqConsumer = applicationContext.findAnnotationOnBean(beanName, MQConsumer.class);
        Assert.notNull(mqProperties.getAccessKey(), "consumer accessKey must be defined");
        Assert.notNull(mqProperties.getSecretKey(), "consumer secretKey must be defined");
        Assert.notNull(mqProperties.getNameSrvAddr(), "consumer nameSrvAddr address must be defined");
        Assert.notNull(mqConsumer.topic(), "consumer's topic must be defined");
        if (!AbstractMQPushConsumer.class.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException(bean.getClass().getName() + " - consumer未实现Consumer抽象类");
        }
        Environment environment = applicationContext.getEnvironment();

        String topic = environment.resolvePlaceholders(mqConsumer.topic());
        String tag = mqConsumer.tag();

        // 配置push consumer
        AbstractMQPushConsumer.class.isAssignableFrom(bean.getClass());
        AbstractMQPushConsumer abstractMQPushConsumer = (AbstractMQPushConsumer) bean;

        PushConsumerBuilder pushConsumerBuilder = provider.newPushConsumerBuilder();
        //配置文件
        pushConsumerBuilder.setConsumerGroup(getGroupId(mqConsumer));
        //将消费者线程数固定为20个 20为默认值
        pushConsumerBuilder.setConsumptionThreadCount(20);
        //订阅关系
        Map<FilterExpression, MessageListener> subscriptionTable = new HashMap<>();
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        subscriptionTable.put(filterExpression, (MessageListener) bean);
        pushConsumerBuilder.setClientConfiguration(mqProperties.clientConfiguration());
        pushConsumerBuilder.setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression));
        pushConsumerBuilder.setMessageListener(abstractMQPushConsumer);

        PushConsumer pushConsumer = pushConsumerBuilder.build();
        abstractMQPushConsumer.setConsumer(pushConsumer);

        log.info(String.format("%s is ready to subscribe message, 订阅关系:{}--{}", bean.getClass().getName()),topic, filterExpression);
    }

    private String getGroupId(MQConsumer mqConsumer) {
        StringBuilder group_id = new StringBuilder();
        if ("GID_".equals(mqConsumer.consumerGroup())) {
            group_id.append("GID_").append(mqConsumer.topic());
        } else {
            group_id.append(mqConsumer.consumerGroup());
        }
        String tags = mqConsumer.tag();
        if (!"*".equals(tags)) {
            group_id.append("_").append(tags);
        }
        return group_id.toString();
    }

}
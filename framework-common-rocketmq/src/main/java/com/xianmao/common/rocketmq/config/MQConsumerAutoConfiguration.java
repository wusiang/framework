package com.xianmao.common.rocketmq.config;

import com.xianmao.common.rocketmq.annotation.MQConsumer;
import com.xianmao.common.rocketmq.base.AbstractMQPushConsumer;
import com.xianmao.common.rocketmq.base.AbstractMQPushConsumerProxy;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumerBuilder;
import org.apache.rocketmq.shaded.com.google.common.base.Joiner;
import org.apache.rocketmq.shaded.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * 自动装配消息消费者
 */
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQConsumerAutoConfiguration extends MQBaseAutoConfiguration {

    private final static ClientServiceProvider provider = ClientServiceProvider.loadService();
    private static final Logger log = LoggerFactory.getLogger(MQConsumerAutoConfiguration.class);
    private final static String prefixGroupId = "GID_";

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQConsumer.class);
        if (CollectionUtils.isEmpty(beans)) {
            log.info("no comsumer Subscribe to messages");
            return;
        }
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Assert.notNull(mqProperties.getUsername(), "consumer username must be defined");
            Assert.notNull(mqProperties.getPassword(), "consumer password must be defined");
            Assert.notNull(mqProperties.getNamesrvaddr(), "consumer nameSrvAddr address must be defined");
            Assert.notNull(mqProperties.getConsumergroup(), "consumer consumergroup must be defined");
            checkConsumer(entry);
        }
        publishConsumer(beans);
    }

    private void checkConsumer(Map.Entry<String, Object> entry) {
        MQConsumer mqConsumer = applicationContext.findAnnotationOnBean(entry.getKey(), MQConsumer.class);
        Assert.notNull(mqConsumer.topic(), "consumer's topic must be defined");
        Environment environment = applicationContext.getEnvironment();
        // topic配置
        String topic = environment.resolvePlaceholders(mqConsumer.topic());
        if (StringUtils.isBlank(topic)) {
            throw new RuntimeException("topic topic must be define");
        }
        // tag配置
        String[] tag = mqConsumer.tag();
        if (tag.length == 0) {
            throw new RuntimeException("consumer tag must be define");
        }
        //bean对象判断
        Object bean = entry.getValue();
        if (!(bean instanceof AbstractMQPushConsumer)) {
            throw new RuntimeException("consumer bean[" + entry.getKey() + "] not must impl AbstractMQPushConsumer)");
        }
    }

    private void publishConsumer(Map<String, Object> beans) throws Exception {
        Map<String, List<String>> topicTagMap = new HashMap<>();
        beans.forEach((beanName, bean) -> {
            MQConsumer mqConsumer = applicationContext.findAnnotationOnBean(beanName, MQConsumer.class);
            String topic = mqConsumer.topic();
            String[] tags = mqConsumer.tag();
            // 按topic分组,同时对tag去重
            topicTagMap.compute(topic, (k, v) -> {
                if (CollectionUtils.isEmpty(v)) {
                    v = new ArrayList<>();
                }
                List<String> finalV = v;
                Stream.of(tags).forEach(tag -> Assert.isTrue(!finalV.contains(tag) && !tag.contains("*") && !tag.contains("|"), "tag[" + tag + "]有误"));
                v.addAll(Arrays.asList(tags));
                return v;
            });
            Stream.of(tags).forEach(tag -> AbstractMQPushConsumerProxy.addAbstractMQPushConsumer(topic, tag, (AbstractMQPushConsumer) bean));
        });

        AbstractMQPushConsumerProxy abstractMQPushConsumerProxy = new AbstractMQPushConsumerProxy();
        PushConsumerBuilder pushConsumerBuilder = provider.newPushConsumerBuilder();
        //配置文件
        pushConsumerBuilder.setConsumerGroup(this.getGroupId());
        //将消费者线程数固定为20个 20为默认值
        pushConsumerBuilder.setConsumptionThreadCount(20);
        //订阅关系
        Map<String, FilterExpression> subscriptionTable = new HashMap<>();
        topicTagMap.forEach((k, v) -> {
            FilterExpression filterExpression = new FilterExpression((Joiner.on("||").join(v)), FilterExpressionType.TAG);
            subscriptionTable.put(k, filterExpression);
        });
        pushConsumerBuilder.setClientConfiguration(mqProperties.clientConfiguration());
        pushConsumerBuilder.setSubscriptionExpressions(subscriptionTable);
        pushConsumerBuilder.setMessageListener(abstractMQPushConsumerProxy);
        pushConsumerBuilder.build();
    }

    private String getGroupId() {
        AtomicReference<String> groupId = new AtomicReference<>(applicationContext.getEnvironment().getProperty("rocketmq.consumergroup"));
        Assert.notNull(groupId.get(), "comsumer groupId must not been null");
        if (!groupId.get().contains(prefixGroupId)) {
            groupId.set(prefixGroupId + groupId);
        }
        return groupId.get();
    }

}
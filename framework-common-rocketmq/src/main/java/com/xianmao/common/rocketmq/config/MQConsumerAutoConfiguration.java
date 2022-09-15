package com.xianmao.common.rocketmq.config;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.xianmao.common.rocketmq.annotation.MQConsumer;
import com.xianmao.common.rocketmq.base.AbstractMQPushConsumer;
import com.xianmao.common.rocketmq.base.BaseMQ;
import com.xianmao.common.rocketmq.enums.ConsumeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 自动装配消息消费者
 */
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQConsumerAutoConfiguration extends MQBaseAutoConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    private final static Logger log = LoggerFactory.getLogger(MQConsumerAutoConfiguration.class);

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
        String tags = mqConsumer.tag();

        // 配置push consumer
        AbstractMQPushConsumer.class.isAssignableFrom(bean.getClass());
        ConsumerBean consumerBean = new ConsumerBean();

        //配置文件
        Properties properties = mqProperties.getMqPropertie();
        properties.setProperty(PropertyKeyConst.GROUP_ID, getGroupId(mqConsumer));

        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");
        properties.put(PropertyKeyConst.MessageModel, environment.resolvePlaceholders(mqConsumer.messageMode()));
        consumerBean.setProperties(properties);

        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<Subscription, MessageListener>();
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tags+getTag(mqProperties));
        subscriptionTable.put(subscription, (MessageListener) bean);

        //订阅多个topic如上面设置
        consumerBean.setSubscriptionTable(subscriptionTable);
        AbstractMQPushConsumer abstractMQPushConsumer = (AbstractMQPushConsumer) bean;
        abstractMQPushConsumer.setConsumer(consumerBean);
        consumerBean.start();

        log.info(String.format("%s is ready to subscribe message, 订阅关系:{}--{}", bean.getClass().getName()),subscription.getTopic(), subscription.getExpression());
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
        if (!StringUtils.isEmpty(mqProperties.getEnvironment())) {
            group_id.append("_" + mqProperties.getEnvironment());
        }
        if (ConsumeMode.APPNAME.equals(mqConsumer.consumerMode())) {
            group_id.append("_").append(appName);
        }
        return group_id.toString();
    }

    private String getTag(MqProperties mqProperties) {
        StringBuilder tag = new StringBuilder();
        if (!StringUtils.isEmpty(mqProperties.getEnvironment())) {
            tag.append("_" + mqProperties.getEnvironment());
        }
        return tag.toString();
    }

}
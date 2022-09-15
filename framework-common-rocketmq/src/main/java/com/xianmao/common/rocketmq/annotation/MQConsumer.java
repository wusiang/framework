package com.xianmao.common.rocketmq.annotation;

import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.xianmao.common.rocketmq.enums.ConsumeMode;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * RocketMQ消费者自动装配注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MQConsumer {
    /**
     * 比如您GID_zhuayin 订阅了topic1+tag1，那么所有机器上的GID_zhuayin 都只能订阅topic1+tag1
     * ons中必需GID_  开头
     *
     * @return
     */
    String consumerGroup() default "GID_";

    /**
     * topic
     *
     * @return
     */
    String topic();

    /**
     * 广播模式消费： BROADCASTING
     * 集群模式消费： CLUSTERING
     *
     * @return 消息模式
     */
    String messageMode() default PropertyValueConst.CLUSTERING;

    /**
     * NORMAL： 正常消费
     * APPNAME： 按APP名称 分组多次消费
     * APPNAME: 生成group格式: GID_topic_tag_appname
     *
     * @return消息模式
     */
    String consumerMode() default ConsumeMode.NORMAL;

    /**
     * 不为*时
     * group自动生成时格式GID_topic_tag
     *
     * @return
     */
    String tag() default "*";
}

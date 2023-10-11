package com.xianmao.common.rocketmq.annotation;

import com.xianmao.common.rocketmq.base.MessageExtConst;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MQConsumer {

    /**
     * 消费组
     */
    String consumerGroup() default "";

    /**
     * 主题
     */
    String topic();

    /**
     * 使用线程池并发消费: CONCURRENTLY("CONCURRENTLY"),
     * 单线程消费: ORDERLY("ORDERLY");
     */
    String consumeMode() default MessageExtConst.CONSUME_MODE_CONCURRENTLY;
    /**
     * 消息标签
     */
    String[] tag() default {"*"};
}

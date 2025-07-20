package com.xianmao.common.rocketmq.annotation;

import com.xianmao.common.rocketmq.config.MQBaseAutoConfiguration;
import com.xianmao.common.rocketmq.config.MQConsumerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MQBaseAutoConfiguration.class, MQConsumerAutoConfiguration.class, MQBaseAutoConfiguration.class})
public @interface EnableMQConfiguration {
}


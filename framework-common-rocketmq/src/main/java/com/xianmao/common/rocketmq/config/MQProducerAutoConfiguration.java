package com.xianmao.common.rocketmq.config;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.xianmao.common.rocketmq.annotation.MQProducer;
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

    private static ProducerBean producer;

    public static ProducerBean getProducer() {
        return producer;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean exposeProducer() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQProducer.class);
        if(CollectionUtils.isEmpty(beans)){
            return null;
        }
        if(producer == null) {
            Assert.notNull(mqProperties.getAccessKey(), "producer accessKey must be defined");
            Assert.notNull(mqProperties.getSecretKey(), "producer secretKey must be defined");
            Assert.notNull(mqProperties.getNameSrvAddr(), "producer nameSrvAddr address must be defined");
            producer = new ProducerBean();
            producer.setProperties(mqProperties.getMqPropertie());
        }
        return producer;
    }
}

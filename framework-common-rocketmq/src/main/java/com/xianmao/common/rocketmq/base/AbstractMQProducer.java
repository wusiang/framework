package com.xianmao.common.rocketmq.base;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.xianmao.common.rocketmq.config.MqProperties;
import com.xianmao.common.rocketmq.exception.MQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executors;

/**
 * RocketMQ的生产者的抽象基类
 */
public abstract class AbstractMQProducer {

    private final static Logger log = LoggerFactory.getLogger(AbstractMQProducer.class);

    public AbstractMQProducer() {
    }

    @Lazy
    @Autowired
    private ProducerBean producer;
    @Autowired
    private MqProperties mqProperties;

    /**
     * 同步发送消息
     *
     * @param message 消息体
     * @throws MQException 消息异常
     */
    public void syncSend(Message message) throws MQException {
        try {
            message.setTag(message.getTag() + getTag(mqProperties));
            SendResult sendResult = producer.send(message);
            log.debug("send rocketmq message ,messageId : {}", sendResult.getMessageId());
        } catch (Exception e) {
            log.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message, e);
            throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        }
    }


    /**
     * 异步发送消息
     *
     * @param message      msgObj
     * @param sendCallback 回调
     * @throws MQException 消息异常
     */
    public void asyncSend(Message message, SendCallback sendCallback) throws MQException {
        producer.setCallbackExecutor(Executors.newFixedThreadPool(10));
        try {
            message.setTag(message.getTag() + getTag(mqProperties));
            producer.sendAsync(message, sendCallback);
            log.debug("send rocketmq message async");
        } catch (Exception e) {
            log.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message, e);
            throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        }
    }

    private String getTag(MqProperties mqProperties) {
        StringBuilder tag = new StringBuilder();
        if (!StringUtils.isEmpty(mqProperties.getEnvironment())) {
            tag.append("_" + mqProperties.getEnvironment());
        }
        return tag.toString();
    }
}

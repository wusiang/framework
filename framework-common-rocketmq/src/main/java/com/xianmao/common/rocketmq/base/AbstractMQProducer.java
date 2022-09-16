package com.xianmao.common.rocketmq.base;

import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.CompletableFuture;

/**
 * RocketMQ的生产者的抽象基类
 */
public abstract class AbstractMQProducer {

    private final static Logger log = LoggerFactory.getLogger(AbstractMQProducer.class);

    public AbstractMQProducer() {
    }

    @Autowired
    @Lazy
    private Producer producer;

    /**
     * 同步发送消息
     */
    public void syncSend(Message message) throws Exception {
        try {
            SendReceipt sendResult = producer.send(message);
            log.debug("Send message successfully, messageId={}", sendResult.getMessageId());
        } catch (Exception e) {
            log.error("Send message Failed msgObj: {}", message, e);
            throw new RuntimeException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        } finally {
            producer.close();
        }
    }


    /**
     * 异步发送消息
     */
    public void asyncSend(Message message) throws Exception {
        try {
            final CompletableFuture<SendReceipt> future = producer.sendAsync(message);
            future.whenComplete((sendReceipt, throwable) -> {
                if (null == throwable) {
                    log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
                } else {
                    log.error("Send message Failed", throwable);
                }
            });
        } catch (Exception e) {
            log.error("Send message Failed msgObj:{}", message, e);
            throw new RuntimeException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
        } finally {
            producer.close();
        }
    }
}

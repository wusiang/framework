package com.xianmao.common.rocketmq.base;

import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RocketMQ的消费者(Push模式)处理消息的接口
 */
public abstract class AbstractMQPushConsumer<T> extends AbstractMQConsumer<T> implements MessageListener {

    private final static Logger log = LoggerFactory.getLogger(AbstractMQPushConsumer.class);

    private PushConsumer consumer;

    public PushConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(PushConsumer consumer) {
        this.consumer = consumer;
    }

    public AbstractMQPushConsumer() {

    }


    @Override
    public ConsumeResult consume(MessageView messageView) {
        try {
            log.debug("receive messageId: {}", messageView.getMessageId());
            T t = parseMessage(messageView);
            if (!process(t)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", messageView.getMessageId());
                return ConsumeResult.FAILURE;
            }
            return ConsumeResult.SUCCESS;
        } catch (Exception e) {
            log.warn("consume fail , ask for re-consume , msgId: {}, {}", messageView.getMessageId(), e);
            return ConsumeResult.FAILURE;
        }
    }

    /**
     * 继承这个方法处理消息
     *
     * @return 处理结果
     */
    public abstract boolean process(T message);
}

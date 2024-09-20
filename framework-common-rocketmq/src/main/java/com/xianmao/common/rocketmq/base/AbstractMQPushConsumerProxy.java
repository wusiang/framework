package com.xianmao.common.rocketmq.base;


import com.xianmao.common.rocketmq.trace.MessageTrace;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ的消费者(Push模式)处理消息的接口
 */
public class AbstractMQPushConsumerProxy extends AbstractMQConsumer implements MessageListener {

    private final static Logger log = LoggerFactory.getLogger(AbstractMQPushConsumerProxy.class);
    private static final Map<String, AbstractMQPushConsumer> abstractMQPushConsumerMap = new HashMap<>();
    public static void addAbstractMQPushConsumer(String topic, String tag, AbstractMQPushConsumer mqMessageListener) {
        abstractMQPushConsumerMap.put(topic + tag, mqMessageListener);
    }
    @Override
    public ConsumeResult consume(MessageView messageView) {
        try {
            MessageTrace.consumerTrace(messageView);
            log.info("receive messageId: {} topic:{} tag:{}", messageView.getMessageId(), messageView.getTopic(), messageView.getTag());
            AbstractMQPushConsumer abstractMQPushConsumer = abstractMQPushConsumerMap.get(messageView.getTopic() + messageView.getTag().get());
            if (abstractMQPushConsumer == null) {
                log.error("找不到对应的MQConsumerListener,topic:{},tag:{},msgId:{}", messageView.getTopic(), messageView.getTag(), messageView.getMessageId());
                return ConsumeResult.SUCCESS;
            }
            MessageInfo t = parseMessage(messageView);
            if (!abstractMQPushConsumer.process(t)) {
                log.warn("consume fail, ask for re-consume , msgId: {}", messageView.getMessageId());
                return ConsumeResult.FAILURE;
            }
            return ConsumeResult.SUCCESS;
        } catch (Exception e) {
            log.error("consume fail, ask for re-consume , msgId: {}", messageView.getMessageId(), e);
            return ConsumeResult.FAILURE;
        }
    }
}

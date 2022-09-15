package com.xianmao.common.rocketmq.base;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.xianmao.common.rocketmq.trace.MessageTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RocketMQ的消费者(Push模式)处理消息的接口
 */
public abstract class AbstractMQPushConsumer<T> extends AbstractMQConsumer<T> implements MessageListener {

    private final static Logger log = LoggerFactory.getLogger(AbstractMQPushConsumer.class);

    private ConsumerBean consumer;

    public ConsumerBean getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerBean consumer) {
        this.consumer = consumer;
    }

    public AbstractMQPushConsumer() {
    }

    @Override
    public Action consume(Message msg, ConsumeContext context) {
        try {
            MessageTrace.consumerTrace(msg);
            log.debug("receive msgId: {}, tags : {}", msg.getMsgID(), msg.getTag());
            T t = parseMessage(msg);
            if (!process(t, context)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", msg.getMsgID());
                return Action.ReconsumeLater;
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            log.warn("consume fail , ask for re-consume , msgId: {}, {}", msg.getMsgID(), e);
            return Action.ReconsumeLater;
        }
    }

    /**
     * 继承这个方法处理消息
     * @return 处理结果
     */
    public abstract boolean process(T message, ConsumeContext context);
}

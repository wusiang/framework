package com.xianmao.common.rocketmq.base;

import org.apache.rocketmq.client.apis.message.MessageView;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Description：RocketMQ消费抽象基类
 */
public abstract class AbstractMQConsumer {


    /**
     * 反序列化解析消息
     */
    protected MessageInfo parseMessage(MessageView message) {
        if (message == null || message.getBody() == null) {
            return null;
        }
        Charset charset = StandardCharsets.UTF_8;
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTag(message.getTag().get());
        messageInfo.setTopic(message.getTopic());
        messageInfo.setBody(charset.decode(message.getBody()).toString());
        messageInfo.setKeys(new ArrayList<>(message.getKeys()));
        return messageInfo;
    }
}

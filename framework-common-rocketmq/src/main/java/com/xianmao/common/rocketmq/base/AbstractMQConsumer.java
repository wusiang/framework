package com.xianmao.common.rocketmq.base;

import com.aliyun.openservices.ons.api.Message;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description：RocketMQ消费抽象基类
 */
public abstract class AbstractMQConsumer<T> {

    private final static Logger log = LoggerFactory.getLogger(AbstractMQConsumer.class);

    protected static Gson gson = new Gson();

    /**
     * 反序列化解析消息
     *
     * @param message 消息体
     * @return 序列化结果
     */
    protected T parseMessage(Message message) {
        if (message == null || message.getBody() == null) {
            return null;
        }
        final Type type = this.getMessageType();
        if (type instanceof Class) {
            try {
                T data = gson.fromJson(new String(message.getBody()), type);
                return data;
            } catch (JsonSyntaxException e) {
                log.error("parse message json fail : {}", e);
            }
        } else {
            log.warn("Parse msg error. {}", message);
        }
        return null;
    }


    /**
     * 解析消息类型
     *
     * @return 消息类型
     */
    protected Type getMessageType() {
        Type superType = this.getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments must be 1");
            return actualTypeArguments[0];
        } else {
            return Object.class;
        }
    }
}

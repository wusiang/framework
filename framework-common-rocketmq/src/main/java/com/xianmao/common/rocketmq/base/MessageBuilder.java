package com.xianmao.common.rocketmq.base;


import com.google.gson.Gson;
import com.xianmao.common.rocketmq.annotation.MQKey;
import com.xianmao.common.rocketmq.trace.MessageTrace;
import com.xianmao.common.rocketmq.trace.TraceConstant;
import lombok.Data;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.shaded.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Data
@Component
public class MessageBuilder {

    private final Logger log = LoggerFactory.getLogger(MessageBuilder.class);
    private static Gson gson = new Gson();
    private String topic;
    private String tag;
    private String key;
    private Object message;
    private Duration messageDelayTime;

    public static MessageBuilder of(String topic, String tag) {
        MessageBuilder builder = new MessageBuilder();
        builder.setTopic(topic);
        builder.setTag(tag);
        return builder;
    }

    public static MessageBuilder of(Object message) {
        MessageBuilder builder = new MessageBuilder();
        builder.setMessage(message);
        return builder;
    }

    public MessageBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public MessageBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 延时时间单位为毫秒（ms），指定一个时刻，在这个时刻之后才能被消费，这个例子表示 3秒 后才能被消费
     */
    public MessageBuilder messageDelayTime(Duration messageDelayTime) {
        this.messageDelayTime = messageDelayTime;
        return this;
    }

    public Message build() {
        String messageKey = "";
        try {
            Field[] fields = message.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation[] allFAnnos = field.getAnnotations();
                if (allFAnnos.length > 0) {
                    for (int i = 0; i < allFAnnos.length; i++) {
                        if (allFAnnos[i].annotationType().equals(MQKey.class)) {
                            field.setAccessible(true);
                            MQKey mqKey = (MQKey) allFAnnos[i];
                            Object o = field.get(message);
                            if (o != null) {
                                messageKey = StringUtils.isEmpty(mqKey.prefix()) ? o.toString() : (mqKey.prefix() + o.toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse key error : ", e);
        }
        String str = gson.toJson(message);
        if (StringUtils.isEmpty(topic)) {
            if (StringUtils.isEmpty(getTopic())) {
                throw new RuntimeException("no topic defined to send this message");
            }
        }
        final ClientServiceProvider provider = ClientServiceProvider.loadService();

        if (messageDelayTime != null && messageDelayTime.toMillis() > 0) {
            return provider.newMessageBuilder()
                    .setTopic(topic)
                    .setTag(tag)
                    .setKeys(messageKey)
                    .setDeliveryTimestamp(System.currentTimeMillis() + messageDelayTime.toMillis())
                    .setBody(str.getBytes(StandardCharsets.UTF_8))
                    .addProperty(TraceConstant.LEGACY_TRACE_ID_NAME, MessageTrace.producerTrace())
                    .build();
        } else {
            return provider.newMessageBuilder()
                    .setTopic(topic)
                    .setTag(tag)
                    .setKeys(messageKey)
                    .setBody(str.getBytes(StandardCharsets.UTF_8))
                    .addProperty(TraceConstant.LEGACY_TRACE_ID_NAME, MessageTrace.producerTrace())
                    .build();
        }
    }
}

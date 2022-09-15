package com.xianmao.common.rocketmq.base;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import com.xianmao.common.rocketmq.annotation.MQKey;
import com.xianmao.common.rocketmq.trace.MessageTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@Component
public class MessageBuilder {

    private final Logger log = LoggerFactory.getLogger(MessageBuilder.class);

    private static Gson gson = new Gson();

    private String topic;
    private String tag;
    private String key;
    private Object message;
    private Long delayTimeLevel;

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

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        MessageBuilder.gson = gson;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Long getDelayTimeLevel() {
        return delayTimeLevel;
    }

    public void setDelayTimeLevel(Long delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }

    /**
     * 延时时间单位为毫秒（ms），指定一个时刻，在这个时刻之后才能被消费，这个例子表示 3秒 后才能被消费
     */
    public MessageBuilder delayTimeLevel(long delayTime) {
        // 延时时间单位为毫秒（ms），指定一个时刻，在这个时刻之后才能被消费，这个例子表示 3秒 后才能被消费
        this.delayTimeLevel = System.currentTimeMillis() + delayTime;
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
                            MQKey mqKey = MQKey.class.cast(allFAnnos[i]);
                            Object o = field.get(message);
                            if (o != null) {
                                messageKey = StringUtils.isEmpty(mqKey.prefix()) ? o.toString() : (mqKey.prefix() + o.toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("parse key error : {}", e);
        }
        String str = gson.toJson(message);
        if (StringUtils.isEmpty(topic)) {
            if (StringUtils.isEmpty(getTopic())) {
                throw new RuntimeException("no topic defined to send this message");
            }
        }
        Message message = new Message(topic, tag, str.getBytes(StandardCharsets.UTF_8));
        if (StringUtils.isNotEmpty(messageKey)) {
            message.setKey(messageKey);
        }
        if (delayTimeLevel != null && delayTimeLevel > 0) {
            message.setStartDeliverTime(delayTimeLevel);
        }
        MessageTrace.producerTrace(message);
        return message;
    }
}

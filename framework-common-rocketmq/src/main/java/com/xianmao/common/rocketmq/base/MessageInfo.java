package com.xianmao.common.rocketmq.base;

import lombok.Data;

import java.util.List;


@Data
public class MessageInfo {
    /**
     * 主题
     */
    private String topic;
    /**
     * tag
     */
    private String tag;
    /**
     * 消息体
     */
    private String body;
    /**
     * 消息索引Key
     */
    private List<String> keys;
}

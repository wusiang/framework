package com.xianmao.common.rocketmq.log;

import java.io.Serializable;
import java.util.Date;

public class MessageLog implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * topic
     */
    private String topic;

    /**
     * tag
     */
    private String tag;

    /**
     * status(1 成功 2失败 -1异常)
     */
    private Integer status;

    /**
     * 顺序消费
     */
    private String shardingKey;

    /**
     * bizKey
     */
    private String bizKey;

    /**
     * 要处理的消息体
     */
    private String body;

    /**
     * 消息来源
     */
    private String source;

    /**
     * 发送、消费的应用
     */
    private String application;

    /**
     * 重试次数
     */
    private Integer retry;


    /**
     * 延时时间，单位：毫秒，默认0
     */
    private Long delay;

    /**
     * created
     */
    private Date created;

    /**
     * modified
     */
    private Date modified;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShardingKey() {
        return shardingKey;
    }

    public void setShardingKey(String shardingKey) {
        this.shardingKey = shardingKey;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}

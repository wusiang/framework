package com.xianmao.common.rocketmq.base;

public interface AbstractMQPushConsumer {


    /**
     * 继承这个方法处理消息
     *
     * @return 处理结果
     */
    public boolean process(MessageInfo message);
}

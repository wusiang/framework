package com.xianmao.common.rocketmq.base;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;

/**
 * RocketMQ的事务生产者的抽象基类
 */
public abstract class AbstractMQTransactionProducer implements LocalTransactionChecker {
    @Override
    public TransactionStatus check(Message msg) {
        System.out.println("开始回查本地事务状态");
        return TransactionStatus.CommitTransaction;
    }
}

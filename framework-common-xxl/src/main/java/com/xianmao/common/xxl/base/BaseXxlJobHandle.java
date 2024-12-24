package com.xianmao.common.xxl.base;

import com.xianmao.common.xxl.trace.XxlJobTrace;
import com.xxl.job.core.handler.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseXxlJobHandle extends IJobHandler {

    private static final Logger log = LoggerFactory.getLogger(BaseXxlJobHandle.class);

    @Override
    public void execute() throws Exception {
        XxlJobTrace.executeTrace();
        this.process();
        XxlJobTrace.removeTrace();
    }

    /**
     * 处理数据，由子类实现
     */
    public abstract void process();
}

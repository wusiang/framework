package com.xianmao.common.job.base;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.xianmao.common.job.config.JobBaseConfiguration;
import com.xianmao.common.job.trace.SimpleJobTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSimpleJob extends JobBaseConfiguration implements SimpleJob {

    private static final Logger log = LoggerFactory.getLogger(BaseSimpleJob.class);

    private static final String CLASS_NAME_SEPARATOR = "." ;

    @Override
    public void execute(ShardingContext shardingContext) {
        BaseSimpleJob baseSimpleJob = applicationContext.getBean(getBeanName(this.getClass().getName()), BaseSimpleJob.class);
        SimpleJobTrace.executeTrace();
        baseSimpleJob.process(shardingContext);
        SimpleJobTrace.removeTrace();
    }

    /**
     * 处理数据，由子类实现
     */
    public abstract void process(ShardingContext shardingContext);

    /**
     * 获取bean的名字，比如参数为com.isz.common.job.util.StringUtil，返回值为stringUtil
     */
    private String getBeanName(String className) {
        if (!className.contains(CLASS_NAME_SEPARATOR)) {
            log.error("BaseSimpleJob getBeanName param className error.");
            return "" ;
        }
        String beanNameBig = className.substring(className.lastIndexOf(".") + 1);
        char temp = beanNameBig.charAt(0);
        temp = (char) (temp + ' ');
        return temp + beanNameBig.substring(1, beanNameBig.length());
    }
}

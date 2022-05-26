package com.xianmao.common.job.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 上下文工具
 */
public class JobBaseConfiguration implements ApplicationContextAware {
    protected static ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * 从spring容器中获取bean
     *
     * @param name
     * @param <T>
     * @return
     * @throws BeansException
     */
    public <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }
}

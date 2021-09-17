package com.xianmao.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName SpringContextUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:09
 * @Version 1.0
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        SpringContextUtil.applicationContext = arg0;
    }

    /**
     * 得到applicationContext 对象
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext;
    }

    /**
     * 执行查找bean
     *
     * @param beanName beanName
     * @return 返回查询到的bean实例
     */
    public static <T> T getBean(String beanName) {
        if (null == beanName || "".equals(beanName.trim())) {
            return null;
        }
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 执行查找bean
     *
     * @param beanName beanName
     * @return 返回查询到的bean实例
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (null == beanName || "".equals(beanName.trim())) {
            return null;
        }
        if (clazz == null) {
            return null;
        }
        return (T) applicationContext.getBean(beanName, clazz);
    }

    /**
     * 发送事件
     *
     * @param event
     * @throws Exception
     */
    public static void publishEvent(ApplicationEvent event) throws Exception {
        if (applicationContext == null) {
            return;
        }
        applicationContext.publishEvent(event);
    }
}

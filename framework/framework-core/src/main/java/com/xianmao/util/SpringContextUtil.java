package com.xianmao.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
        return applicationContext;
    }

    /**
     * 执行查找bean
     *
     * @param beanName beanName
     * @return 返回查询到的bean实例
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}

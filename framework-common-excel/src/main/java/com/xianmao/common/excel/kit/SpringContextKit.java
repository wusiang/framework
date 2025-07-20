package com.xianmao.common.excel.kit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;

import java.util.Map;


@Slf4j
@Lazy(false)
public class SpringContextKit implements BeanFactoryPostProcessor, ApplicationContextAware, DisposableBean {

	private static ConfigurableListableBeanFactory beanFactory;

	private static ApplicationContext applicationContext = null;

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * BeanFactoryPostProcessor, 注入Context到静态变量中.
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
		SpringContextKit.beanFactory = factory;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextKit.applicationContext = applicationContext;
	}

	public static ListableBeanFactory getBeanFactory() {
		return null == beanFactory ? applicationContext : beanFactory;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) getBeanFactory().getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, Map<Bean名称，实现类></>
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return getBeanFactory().getBean(requiredType);
	}

	/**
	 * 清除SpringContextKit中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (log.isDebugEnabled()) {
			log.debug("清除SpringContextKit中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}

	/**
	 * 发布事件
	 * @param event
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (applicationContext == null) {
			return;
		}
		applicationContext.publishEvent(event);
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy() {
		SpringContextKit.clearHolder();
	}

}

package com.xianmao.common.xxl.executor;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class XianmaoXxlJobSpringExecutor extends XxlJobExecutor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(XianmaoXxlJobSpringExecutor.class);

    private static ApplicationContext applicationContext;

    public XianmaoXxlJobSpringExecutor() {
    }

    public void afterSingletonsInstantiated() {
        this.initJobHandlerMethodRepository(applicationContext);
        GlueFactory.refreshInstance(1);

        try {
            super.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        super.destroy();
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext != null) {
            String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
            for (String beanDefinitionName : beanDefinitionNames) {
                Object bean = applicationContext.getBean(beanDefinitionName);
                Map<Method, XxlJob> annotatedMethods = null;
                try {
                    annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), new MethodIntrospector.MetadataLookup<XxlJob>() {
                        public XxlJob inspect(Method method) {
                            return (XxlJob) AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class);
                        }
                    });
                } catch (Throwable ex) {
                    logger.error("xxl-job method-jobhandler resolve error for bean[" + beanDefinitionName + "].", ex);
                }

                if (annotatedMethods != null && !annotatedMethods.isEmpty()) {

                    for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                        Method executeMethod = (Method) methodXxlJobEntry.getKey();
                        XxlJob xxlJob = (XxlJob) methodXxlJobEntry.getValue();
                        if (xxlJob != null) {
                            String name = xxlJob.value();
                            if (name.trim().isEmpty()) {
                                throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
                            }
                            if (loadJobHandler(name) != null) {
                                throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
                            }
                            executeMethod.setAccessible(true);
                            Method initMethod = null;
                            Method destroyMethod = null;
                            if (!xxlJob.init().trim().isEmpty()) {
                                try {
                                    initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
                                    initMethod.setAccessible(true);
                                } catch (NoSuchMethodException var18) {
                                    throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
                                }
                            }
                            if (!xxlJob.destroy().trim().isEmpty()) {
                                try {
                                    destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
                                    destroyMethod.setAccessible(true);
                                } catch (NoSuchMethodException var17) {
                                    throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
                                }
                            }
                            registJobHandler(name, new XianmaoMethodJobHandler(bean, executeMethod, initMethod, destroyMethod));
                        }
                    }
                }
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XianmaoXxlJobSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

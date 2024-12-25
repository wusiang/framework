//package com.xianmao.common.xxl.executor;
//
//import com.xxl.job.core.executor.XxlJobExecutor;
//import com.xxl.job.core.glue.GlueFactory;
//import com.xxl.job.core.handler.annotation.XxlJob;
//
//import java.lang.reflect.Method;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.SmartInitializingSingleton;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.core.MethodIntrospector;
//import org.springframework.core.annotation.AnnotatedElementUtils;
//
//public class XianmaoXxlJobSpringExecutor extends XxlJobExecutor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {
//
//    private static final Logger logger = LoggerFactory.getLogger(XianmaoXxlJobSpringExecutor.class);
//    private static ApplicationContext applicationContext;
//
//    public XianmaoXxlJobSpringExecutor() {
//    }
//
//    public void afterSingletonsInstantiated() {
//        this.initJobHandlerMethodRepository(applicationContext);
//        GlueFactory.refreshInstance(1);
//
//        try {
//            super.start();
//        } catch (Exception var2) {
//            throw new RuntimeException(var2);
//        }
//    }
//
//    public void destroy() {
//        super.destroy();
//    }
//
//    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
//        if (applicationContext != null) {
//            String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
//            String[] var3 = beanDefinitionNames;
//            int var4 = beanDefinitionNames.length;
//
//            for (int var5 = 0; var5 < var4; ++var5) {
//                String beanDefinitionName = var3[var5];
//                Object bean = applicationContext.getBean(beanDefinitionName);
//                Map annotatedMethods = null;
//
//                try {
//                    annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), new MethodIntrospector.MetadataLookup<XxlJob>() {
//                        public XxlJob inspect(Method method) {
//                            return (XxlJob) AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class);
//                        }
//                    });
//                } catch (Throwable var19) {
//                    logger.error("xxl-job method-jobhandler resolve error for bean[" + beanDefinitionName + "].", var19);
//                }
//
//                if (annotatedMethods != null && !annotatedMethods.isEmpty()) {
//                    Iterator var9 = annotatedMethods.entrySet().iterator();
//
//                    while (var9.hasNext()) {
//                        Entry<Method, XxlJob> methodXxlJobEntry = (Entry) var9.next();
//                        Method executeMethod = (Method) methodXxlJobEntry.getKey();
//                        XxlJob xxlJob = (XxlJob) methodXxlJobEntry.getValue();
//                        if (xxlJob != null) {
//                            String name = xxlJob.value();
//                            if (name.trim().length() == 0) {
//                                throw new RuntimeException("xxl-job method-jobhandler name invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
//                            }
//
//                            if (loadJobHandler(name) != null) {
//                                throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
//                            }
//
//                            executeMethod.setAccessible(true);
//                            Method initMethod = null;
//                            Method destroyMethod = null;
//                            if (xxlJob.init().trim().length() > 0) {
//                                try {
//                                    initMethod = bean.getClass().getDeclaredMethod(xxlJob.init());
//                                    initMethod.setAccessible(true);
//                                } catch (NoSuchMethodException var18) {
//                                    throw new RuntimeException("xxl-job method-jobhandler initMethod invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
//                                }
//                            }
//
//                            if (xxlJob.destroy().trim().length() > 0) {
//                                try {
//                                    destroyMethod = bean.getClass().getDeclaredMethod(xxlJob.destroy());
//                                    destroyMethod.setAccessible(true);
//                                } catch (NoSuchMethodException var17) {
//                                    throw new RuntimeException("xxl-job method-jobhandler destroyMethod invalid, for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
//                                }
//                            }
//
//                            registJobHandler(name, new XianmaoMethodJobHandler(bean, executeMethod, initMethod, destroyMethod));
//                        }
//                    }
//                }
//            }
//
//        }
//    }
//
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        XianmaoXxlJobSpringExecutor.applicationContext = applicationContext;
//    }
//
//    public static ApplicationContext getApplicationContext() {
//        return applicationContext;
//    }
//}

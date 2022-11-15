package com.xianmao.common.redo.core;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

/**
 * 作用：扫描框架代码里的dao层接口，注册bean
*/
public class InnerMapperBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private String[] basePackages = new String[]{"com.xianmao.common.redo.dao.mapper",
            "com.xianmao.common.redo.clusterlock.dblock.dao"};

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        //这一句是必须加的，注册过滤器，在扫描bean定义会匹配到加了@Mapper注解的类
        scanner.registerFilters();
        scanner.doScan(getAllBasePackages(registry));
        //假如后续有其他jar包需要扫描，可以额外使用@MapperScan,重复扫描不会有影响
    }

    private String[] getAllBasePackages(BeanDefinitionRegistry registry) {
        //根据启动类名称获取启动类
        BeanDefinition beanDefinition = registry.getBeanDefinition("application");
        //启动类
        String applicationBeanClass = beanDefinition.getBeanClassName();
        //获取到启动类所在的包名，MybatisAutoConfiguration的自动配置默认扫描的就是basePackage
        //做一步的目的是为了将失效的自动配置扫描行为重新生效
        String basePackage = ClassUtils.getPackageName(applicationBeanClass);
        String[] newbasePackages = new String[basePackages.length + 1];
        for (int i = 0; i < basePackages.length; i++) {
            newbasePackages[i] = basePackages[i];
        }
        newbasePackages[basePackages.length] = basePackage;
        return newbasePackages;
    }

}

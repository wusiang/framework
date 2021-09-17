package com.xianmao.common.config;

import com.xianmao.aspect.WebLogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName LogAutoConfiguration
 * @Description:
 * @Author guyi
 * @Data 2019-08-16 11:11
 * @Version 1.0
 */
public class WebLogAspectConfiguration implements ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspectConfiguration.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (log.isInfoEnabled()) {
            log.info("WebLogAspect-registerBeanDefinitions begin to init log configure");
        }
        boolean result = registerBeanDefinitionIfNotExists(beanDefinitionRegistry, WebLogAspect.class.getName(),
                WebLogAspect.class);
    }

    private boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, String beanName, Class<?> beanClass) {
        return registerBeanDefinitionIfNotExists(registry, beanName, beanClass, null);
    }

    private boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, String beanName,
                                                      Class<?> beanClass, Map<String, Object> extraPropertyValues) {
        if (registry.containsBeanDefinition(beanName)) {
            return false;
        }
        String[] candidates = registry.getBeanDefinitionNames();
        for (String candidate : candidates) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(candidate);
            if (Objects.equals(beanDefinition.getBeanClassName(), beanClass.getName())) {
                return false;
            }
        }
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();

        if (extraPropertyValues != null) {
            for (Map.Entry<String, Object> entry : extraPropertyValues.entrySet()) {
                beanDefinition.getPropertyValues().add(entry.getKey(), entry.getValue());
            }
        }
        registry.registerBeanDefinition(beanName, beanDefinition);
        return true;
    }
}

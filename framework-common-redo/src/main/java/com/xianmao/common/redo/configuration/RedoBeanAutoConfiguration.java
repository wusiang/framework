package com.xianmao.common.redo.configuration;

import com.xianmao.common.redo.core.InnerMapperBeanDefinitionRegistrar;
import com.xianmao.common.redo.utils.RedoLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * 自动配置类，由spring.factories定义该类
*/
@ComponentScan({"com.xianmao.common.redo"})
@Configuration
@Import({InnerMapperBeanDefinitionRegistrar.class})
public class RedoBeanAutoConfiguration {

    @PostConstruct
    public void init(){

    }

    @Bean
    public RedoLogger redoLogger(){
        return new RedoLogger();
    }


}

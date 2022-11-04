package com.xianmao.common.redo.configuration;

import com.xianmao.common.redo.utils.RedoSleeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过Import的方式，加载bean
*/
@Configuration
public class MyConfiguration {

    /**
     * 简单的bean可以通过这样的方式放入容器
     * @return
     */
    @Bean
    public RedoSleeper redoSleeper(){
        return new RedoSleeper();
    }
}

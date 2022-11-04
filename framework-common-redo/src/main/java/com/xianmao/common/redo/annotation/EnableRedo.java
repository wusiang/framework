package com.xianmao.common.redo.annotation;

import com.xianmao.common.redo.configuration.MyConfiguration;
import com.xianmao.common.redo.configuration.RedoBeanAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Redo组件开关
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({
        RedoBeanAutoConfiguration.class,
        MyConfiguration.class
})
public @interface EnableRedo {


}

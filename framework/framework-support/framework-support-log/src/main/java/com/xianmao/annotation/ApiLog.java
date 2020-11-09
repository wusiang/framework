package com.xianmao.annotation;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiLog {

    /**
     * 业务名称
     *
     * @return
     */
    String value() default "日志记录";
}

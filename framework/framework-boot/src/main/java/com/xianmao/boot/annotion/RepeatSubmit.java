package com.xianmao.boot.annotion;

import java.lang.annotation.*;

/**
 * 自定义注解防止表单重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     *  重复提交、缓存时间设置 参数毫秒
     */
    long time() default 3000L;

    /**
     * 当重复提交异常是否抛出，默认是false 缓存请求信息  true即抛出异常
     */
    boolean throwable() default false;
}

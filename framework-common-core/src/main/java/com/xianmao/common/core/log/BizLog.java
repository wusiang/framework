package com.xianmao.common.core.log;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizLog {

    /**
     * 日志触发形式
     */
    NotifyEnum value() default NotifyEnum.Arround;

    enum NotifyEnum{
        /**
         * 打印2条日志
         */
        Arround,
        /**
         * 只打印入参日志
         */
        Before,
        /**
         * 只打印出参日志,如果方法执行失败,入参也会打印
         */
        After
    }
}

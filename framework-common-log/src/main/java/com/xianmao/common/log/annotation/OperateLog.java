package com.xianmao.common.log.annotation;

import com.xianmao.common.log.enums.OperateLogEnums;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    /**
     * 操作类型
     */
    OperateLogEnums.OperateType operateType();

    /**
     * 模块
     */
    String module();

    /**
     * 描述内容
     */
    String description();

    /**
     * 填充数据来源
     */
    String dataSource() default "front";

    /**
     * 反射类方法名
     */
   String reflectMethod() default "";
}

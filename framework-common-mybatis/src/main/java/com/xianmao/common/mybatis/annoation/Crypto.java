package com.xianmao.common.mybatis.annoation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crypto {
    /**
     * 自动解密
     */
    boolean autoDecrypt() default true;
}

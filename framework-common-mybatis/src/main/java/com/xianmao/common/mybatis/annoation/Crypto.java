package com.xianmao.common.mybatis.annoation;

import com.xianmao.common.mybatis.enums.Algorithm;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crypto {
    /**
     * 自动解密
     */
    boolean autoDecrypt() default true;

    /**
     * 加密方式
     */
    Algorithm algorithm() default  Algorithm.AES;
}

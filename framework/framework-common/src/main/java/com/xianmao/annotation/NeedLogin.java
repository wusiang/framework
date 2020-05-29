package com.xianmao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All rights Reserved, Designed By guyi
 *
 * @Package com.xianmao.annotation
 * @author: guyi
 * @date: 2019/10/12 10:12 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
public @interface NeedLogin {
}

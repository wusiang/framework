package com.xianmao.annotation;

import com.xianmao.enums.Level;
import com.xianmao.enums.Position;

import java.lang.annotation.*;

/**
 * 日志后置通知
 * <p>
 * 方法：【com.example.demo.TestController.name(TestController.java:34)】，
 * 业务：【测试查询用户信息】，
 * 结果：【{describetion=我还只是18岁, name=test, id=1, age=23}】
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiLog {

    /**
     * 业务名称
     *
     * @return
     */
    String value();

    /**
     * 日志级别
     *
     * @return
     */
    Level level() default Level.DEBUG;

    /**
     * 代码定位支持
     *
     * @return
     */
    Position position() default Position.DEFAULT;
}

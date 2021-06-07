package com.xianmao.boot.annotion;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(value="com.xianmao.boot")
public @interface XianmaoBootApplication {
}

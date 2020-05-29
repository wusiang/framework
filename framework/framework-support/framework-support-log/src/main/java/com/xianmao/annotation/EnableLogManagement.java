package com.xianmao.annotation;

import com.xianmao.aspect.WebLogAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author guyi
 * @Description: 日志组件启动注解
 * <p>
 * 需要在springboot项目启动类配置@EnableLogManagement注解启动日志框架
 * 日志级别为DEBUG时，默认开启代码定位，方便调试
 * 然后在启动配置文件配置日志级别,其他级别默认关闭代码定位。减少不必要的开支，如需要可手动开启(position=Position.ENABLED)
 * <p>
 *  springboot项目需要引入aop-jar包,因为maven打包默认不会打入第三方依赖包所以需要的项目需要自己导入
 *          <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-aop</artifactId>
 *         </dependency>
 *         <dependency>
 *             <groupId>org.javassist</groupId>
 *             <artifactId>javassist</artifactId>
 *             <version>3.23.1-GA</version>
 *         </dependency>
 * <p>
 * properties方式：
 * logging.level.com.xianmao=对应级别
 * <p>
 * <p>
 * yml方式：
 * logging:
 * level:
 * com.xianmao: 对应级别
 * <p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(WebLogAspect.class)
public @interface EnableLogManagement {
}

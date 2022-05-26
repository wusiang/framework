package com.xianmao.common.job.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Job {
    /**
     * 作业是否启用
     */
    boolean enable() default true;

    /**
     * 作业名称的具体描述
     */
    String description();

    /**
     * 作业执行时间
     */
    String cron();

    /**
     * 作业自定义参数
     * <p>作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业<p>
     * <p>例：每次获取的数据量、作业实例从数据库读取的主键等<p>
     */
    String jobParameter() default "";

    /**
     * 作业分片总数
     */
    int shardingCount() default 1;

    /**
     * 是否开启失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
     */
    boolean failover() default false;

    /**
     * 是否开启错过任务重新执行
     */
    boolean misfire() default true;

    /**
     * 脚本目录，仅用在脚本作业中
     */
    String script() default "";
}

package com.xianmao.boot.config;

import com.xianmao.boot.mybatis.plugins.SqlLogInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration()
@AllArgsConstructor
public class MybatisPlusConfiguration {


    /**
     * sql 日志
     * 默认开启可以设置关闭，配置如下
     * xianmao:
     *   mybatis-plus:
     *     sql-log: false
     * @return SqlLogInterceptor
     */
    @Bean
    @ConditionalOnProperty(value = "xianmao.mybatis-plus.sql-log", matchIfMissing = true)
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }
}

package com.xianmao.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.xianmao.common.mybatis.plugins.PigPaginationInnerInterceptor;
import com.xianmao.common.mybatis.plugins.SqlCryptoInterceptor;
import com.xianmao.common.mybatis.plugins.SqlLogInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration()
@AllArgsConstructor
public class MybatisPlusConfiguration {

    /**
     * 分页插件, 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 配置分页拦截器
        interceptor.addInnerInterceptor(new PigPaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * sql 日志
     */
    @Bean
    @ConditionalOnProperty(value = "xianmao.mybatis-plus.sql-log", matchIfMissing = true)
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }

    @Bean
    public SqlCryptoInterceptor sqlCryptoInterceptor() {
        return new SqlCryptoInterceptor();
    }
}

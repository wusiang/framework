package com.xianmao.common.redo.config;

import com.xianmao.common.redo.provider.IRedoTaskService;
import com.xianmao.common.redo.provider.IRedoTaskServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

@Order
@AutoConfiguration
@AllArgsConstructor
public class RedoTaskConfig {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnMissingBean(IRedoTaskService.class)
    public IRedoTaskService clientDetailsService() {
        return new IRedoTaskServiceImpl(jdbcTemplate);
    }
}

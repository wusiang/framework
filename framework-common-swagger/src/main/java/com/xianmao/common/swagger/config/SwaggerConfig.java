package com.xianmao.common.swagger.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import java.util.logging.Logger;

/***
 * 创建Swagger配置
 */
@AutoConfiguration
public class SwaggerConfig {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI swaggerOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info().title("SwaggerOpenAPI接口文档")
                        // 信息
                        .contact(new Contact().name("作者").email("邮箱").url("地址"))
                        // 简介
                        .description("SwaggerOpenAPI接口文档")
                        // 版本
                        .version("v1.0.0")
                        // 许可证
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("外部文档")
                        .url("https://springshop.wiki.github.org/docs"))
                // 接口全局
                .addSecurityItem(new SecurityRequirement()
                        .addList("x-request-id")
                        .addList("x-request-token")
                        .addList("x-request-sign")
                        .addList("x-request-timestamp"));
        return openAPI;
    }
}

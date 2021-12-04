//package com.xianmao.common.config;
//
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import com.google.common.collect.Lists;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.annotation.Order;
//import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Configuration
//@EnableSwagger2
//@EnableKnife4j
//@Import({
//        BeanValidatorPluginsConfiguration.class
//})
//@Slf4j
//public class SwaggerConfig {
//
//    @Bean(value = "userApi")
//    @Order(value = 1)
//    public Docket groupRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
//                .build().securityContexts(Lists.newArrayList(securityContext()))
//                .securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().title("爪印科技接口文档").description("springcloud版本")
//                .contact(new Contact("爪印", "", "")).version("1.0.0").build();
//    }
//
//    private List<ApiKey> apiKey() {
//        List<ApiKey> list = new ArrayList();
//        list.add(new ApiKey("ADMINTOKEN", "ADMINTOKEN", "header"));
//        return list;
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
//    }
//
//}

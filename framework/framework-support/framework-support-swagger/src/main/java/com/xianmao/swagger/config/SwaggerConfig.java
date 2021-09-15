package com.xianmao.swagger.config;


import com.google.common.collect.Lists;
import com.xianmao.swagger.props.SwaggerProperties;
import com.xianmao.swagger.util.SwaggerUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * swagger配置
 * @link https://www.bookstack.cn/read/knife4j/settings.md
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean(value = "userApi")
    @Order(value = 1)
    public Docket groupRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
                .build().securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.<SecurityScheme> newArrayList(apiKey()));
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder().title("食肆郎接口文档").description("springcloud版本")
                .contact(new Contact("孙力火", "", "")).version("1.0.0").build();
    }

    private List<ApiKey> apiKey()
    {
        List<ApiKey> list = new ArrayList();
        list.add(new ApiKey("mp_token 小程序用", "mp_token", "header"));
        list.add(new ApiKey("mp_token_test 小程序用", "mp_token_test", "header"));
        list.add(new ApiKey("sys_token 管理平台用", "sys_token", "header"));
        list.add(new ApiKey("store_token 门店端用", "store_token", "header"));
        list.add(new ApiKey("packer_token 门店员工用", "packer_token", "header"));
        list.add(new ApiKey("cook_token 厨师用", "cook_token", "header"));
        return list;
    }

    private SecurityContext securityContext()
    {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
    }

    List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }

//    private static final String DEFAULT_BASE_PATH = "/**";
//    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");
//
//    @Bean
//    @ConditionalOnMissingBean
//    public SwaggerProperties swaggerProperties() {
//        return new SwaggerProperties();
//    }
//
//    @Bean
//    public Docket api(SwaggerProperties swaggerProperties) {
//        // base-path处理
//        if (swaggerProperties.getBasePath().size() == 0) {
//            swaggerProperties.getBasePath().add(DEFAULT_BASE_PATH);
//        }
//
//        // exclude-path处理
//        if (swaggerProperties.getExcludePath().size() == 0) {
//            swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
//        }
//        ApiSelectorBuilder apis = new Docket(DocumentationType.SWAGGER_2)
//                .host(swaggerProperties.getHost())
//                .apiInfo(apiInfo(swaggerProperties)).select()
//                .apis(SwaggerUtil.basePackages(swaggerProperties.getBasePackages()))
//                .paths(PathSelectors.any());
//
//        swaggerProperties.getBasePath().forEach(p -> apis.paths(PathSelectors.ant(p)));
//        swaggerProperties.getExcludePath().forEach(p -> apis.paths(PathSelectors.ant(p).negate()));
//
//        return apis.build()
//                .securitySchemes(Collections.singletonList(securitySchema(swaggerProperties)))
//                .securityContexts(Collections.singletonList(securityContext(swaggerProperties)))
//                .securityContexts(Lists.newArrayList(securityContext(swaggerProperties)))
//                .securitySchemes(Collections.singletonList(securitySchema(swaggerProperties)));
//    }
//
//    /**
//     * 配置默认的全局鉴权策略的开关，通过正则表达式进行匹配；默认匹配所有URL
//     *
//     * @return
//     */
//    private SecurityContext securityContext(SwaggerProperties swaggerProperties) {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth(swaggerProperties))
//                .forPaths(PathSelectors.regex(swaggerProperties.getAuthorization().getAuthRegex()))
//                .build();
//    }
//
//    /**
//     * 默认的全局鉴权策略
//     *
//     * @return
//     */
//    private List<SecurityReference> defaultAuth(SwaggerProperties swaggerProperties) {
//        ArrayList<AuthorizationScope> authorizationScopeList = new ArrayList<>();
//        swaggerProperties.getAuthorization().getAuthorizationScopeList().forEach(authorizationScope -> authorizationScopeList.add(new AuthorizationScope(authorizationScope.getScope(), authorizationScope.getDescription())));
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[authorizationScopeList.size()];
//        return Collections.singletonList(SecurityReference.builder()
//                .reference(swaggerProperties.getAuthorization().getName())
//                .scopes(authorizationScopeList.toArray(authorizationScopes))
//                .build());
//    }
//
//
//    private OAuth securitySchema(SwaggerProperties swaggerProperties) {
//        ArrayList<AuthorizationScope> authorizationScopeList = new ArrayList<>();
//        swaggerProperties.getAuthorization().getAuthorizationScopeList().forEach(authorizationScope -> authorizationScopeList.add(new AuthorizationScope(authorizationScope.getScope(), authorizationScope.getDescription())));
//        ArrayList<GrantType> grantTypes = new ArrayList<>();
//        swaggerProperties.getAuthorization().getTokenUrlList().forEach(tokenUrl -> grantTypes.add(new ResourceOwnerPasswordCredentialsGrant(tokenUrl)));
//        return new OAuth(swaggerProperties.getAuthorization().getName(), authorizationScopeList, grantTypes);
//    }
//
//    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
//        return new ApiInfoBuilder()
//                .title(swaggerProperties.getTitle())
//                .description(swaggerProperties.getDescription())
//                .license(swaggerProperties.getLicense())
//                .licenseUrl(swaggerProperties.getLicenseUrl())
//                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
//                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
//                .version(swaggerProperties.getVersion())
//                .build();
//    }
}

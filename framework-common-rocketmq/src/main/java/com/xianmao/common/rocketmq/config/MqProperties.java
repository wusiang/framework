package com.xianmao.common.rocketmq.config;

import lombok.Data;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;
import org.apache.rocketmq.shaded.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class MqProperties {

    private String username;
    private String password;
    private String namesrvaddr;
    private String namespace;
    private String consumergroup;

    public ClientConfiguration clientConfiguration() {
        SessionCredentialsProvider sessionCredentialsProvider = new StaticSessionCredentialsProvider(this.username, this.password);
        if (StringUtils.isNotBlank(this.namespace)) {
            //Severless版本
            return ClientConfiguration.newBuilder()
                    .setEndpoints(this.namesrvaddr)
                    .setNamespace(this.namespace)
                    .enableSsl(false)
                    .setRequestTimeout(Duration.ofSeconds(15))
                    .setCredentialProvider(sessionCredentialsProvider)
                    .build();
        } else {
            //其他版本（包年包月）
            return ClientConfiguration.newBuilder()
                    .setEndpoints(this.namesrvaddr)
                    .setRequestTimeout(Duration.ofSeconds(15))
                    .enableSsl(false)
                    .setCredentialProvider(sessionCredentialsProvider)
                    .build();
        }

    }
}

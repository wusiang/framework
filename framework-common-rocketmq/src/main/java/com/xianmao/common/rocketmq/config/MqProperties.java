package com.xianmao.common.rocketmq.config;

import lombok.Data;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class MqProperties {

    private String accessKey;
    private String secretKey;
    private String nameSrvAddr;
    private String consumerGroup;

    public ClientConfiguration clientConfiguration() {
        SessionCredentialsProvider sessionCredentialsProvider = new StaticSessionCredentialsProvider(this.accessKey, this.secretKey);
        return ClientConfiguration.newBuilder()
                .setEndpoints(this.nameSrvAddr)
                .setCredentialProvider(sessionCredentialsProvider)
                .enableSsl(false)
                .build();
    }
}

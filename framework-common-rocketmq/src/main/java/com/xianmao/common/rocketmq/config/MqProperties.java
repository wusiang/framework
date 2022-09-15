package com.xianmao.common.rocketmq.config;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class MqProperties {

    private String accessKey;
    private String secretKey;
    private String nameSrvAddr;

    public ClientConfiguration clientConfiguration() {
        SessionCredentialsProvider sessionCredentialsProvider = new StaticSessionCredentialsProvider(this.accessKey, this.secretKey);
        return ClientConfiguration.newBuilder()
                .setEndpoints(this.nameSrvAddr)
                .setCredentialProvider(sessionCredentialsProvider)
                .build();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

    public void setNameSrvAddr(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
    }
}

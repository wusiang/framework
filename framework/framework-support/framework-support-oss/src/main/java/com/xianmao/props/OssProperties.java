package com.xianmao.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @ClassName OssProperties
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-25 10:07
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "alioss")
public class OssProperties {

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码
     */
    private String accessKeySecret;

    /**
     * 默认的存储桶名称
     */
    private String bucketName = "bladex";

    /**
     * 自定义属性
     */
    private Map<String, String> args;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}

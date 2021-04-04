package com.xianmao.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elasticjob")
public class JobProperties {

    /**
     * 连接Zookeeper服务器的列表. 包括IP地址和端口号. 多个地址用逗号分隔. 如: host1:2181,host2:2181
     */
    private String address;

    /**
     * 命名空间.
     */
    private String namespace;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}

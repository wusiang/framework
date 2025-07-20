```
HttpClent替换Feign底层客户端实现
通过连接池，减少了请求延迟.
缓存响应数据来减少重复的网络请求.
允许连接到同一个主机地址的所有请求，提高请求效率.
配置文件修改
#feign-http请求客户端变更
feign:
  httpclient:
    enabled: true
    # 最大连接数
    max-connections: 200
    # 单个请求路径的最大连接数
    max-connections-per-route: 50
```
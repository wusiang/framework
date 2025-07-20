```yaml
xxl:
  job:
    executor:
      #执行器app的名称，和控制台保持一致
      appname: xxl-job-executor-sample
      #有效使用该地址作为注册地址 为空使用内嵌服务地址
      address:
      #执行器IP 默认自动获取
      ip: localhost
      #执行器端口 小于等于0 自动获取 ，默认 9999 ，配置多个执行器时，需要配置不同的执行器端口
      port: 9999
      #执行器日志保持天数 -1永久生效
      logretentiondays: 30
      #执行器日志文件保持地址 ，为空使用默认保存地址
      logpath: D:/idea-project/data/xxl-job-log/executor
    admin:
      #调度中心部署地址，多个配置 ，分割
      addresses: http://127.0.0.1:8080/xxl-job-admin
      #执行器token
    accessToken:
```
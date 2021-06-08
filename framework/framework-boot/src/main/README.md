
# framework-boot 基本配置
```
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  servlet:
    multipart:
      maxFileSize: 5MB  #单位必须大写MB或不写(即为B)
      maxRequestSize: 10MB
  datasource:
    druid:
      url: jdbc:mysql://127.0.0.1:3306/mall?useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowMultiQueries=true&useAffectedRows=true
      username: root
      password: 123456789
      initialSize: 2
      minIdle: 2
      maxActive: 10
      maxWait: 60000
      timeBetweenEvictionRunsMillis: 6000000
      minEvictableIdleTimeMillis: 300000
      validationQuery: select 'x'
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      poolPreparedStatements: true
      filters: stat
      maxPoolPreparedStatementPerConnectionSize: 5

# Mybatis-plus相关配置
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  global-config:
    db-config:
      id-type: AUTO
      logic-delete-field: is_deleted
      logic-delete-value: 1
  configuration:
    # 是否开启自动驼峰命名规则映射:从数据库列名到Java属性驼峰命名的类似映射
    map-underscore-to-camel-case: true
    # 返回map时true:当查询数据为空时字段返回为null,false:不加这个查询数据为空时，字段将被隐藏
    call-setters-on-nulls: true
    # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

xianmao:
  # mybatis-plus日志
  mybatis-plus:
    sql-log: true
  # 异步线程配置
  async:
    corePoolSize: 2
    maxPoolSize: 50
    queueCapacity: 10000
    keepAliveSeconds: 300
    nameFormat: member
```yaml

#mybatisPlus3 版本说明

```yaml
#mybatisPlus
mybatis-plus:
  #configuration:
    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  sql-log: true
  global-cofalse:
    db-config:
      db-type: MYSQL
      logic-delete-field: del_flag
      logic-delete-value: 1
      logic-not-delete-value: 0
  mapper-locations: classpath:/mappers/*.xml
```
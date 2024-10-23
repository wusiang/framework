
#mybatisPlus3 版本说明

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: del_flag
      logic-delete-value: 1
      logic-not-delete-value: 0
  mapper-locations: classpath:/mappers/*.xml
```
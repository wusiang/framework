# 使用步骤
## 1、添加依赖脚本
###### 数据库锁表

```
CREATE TABLE `tbl_pessimistic_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource` varchar(500) NOT NULL COMMENT '锁定的资源，可以是方法名或者业务唯一标志',
  `description` varchar(1000) NOT NULL DEFAULT '' COMMENT '业务场景描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_idx_resource` (`resource`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据库分布式悲观锁表';
```



```
CREATE TABLE `tbl_redotask`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `redo_task_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '补偿任务业务ID',
  `application_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微服务名',
  `max_attempts` int(11) NULL DEFAULT NULL COMMENT '最大重试次数',
  `exec_times` int(11) NOT NULL DEFAULT 0 COMMENT '已执行次数',
  `try_forever` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否永久尝试',
  `expired_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `req_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
```
```java
//@GetMapping("/test")
//    public String test(){
//        //组织即将入库的参数，后续会使用该参数触发回调
//        RedoReqParam redoReqParam = new RedoReqParam();
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "xyk");
//        map.put("desc", "kunghsu");
//        redoReqParam.setParams(map);
//        //调用组件提供出来的工具类方法，添加回调任务
//        redoManager.addRedoTask("redoBiz1", JsonUtils.toJson(redoReqParam));
//        return "OK";
//    }
```



###### 插入数据库锁记录

```
INSERT INTO `tbl_pessimistic_lock`(`id`, `resource`, `description`)
VALUES (1, 'cn.com.kun.component.redo.core.RedoManager.findAndRedo:微服务名', '重试组件依赖锁');
```



## 2.启动类加上@EnableRedo注解
或者可以在其他@Configuration类加也可以

## 3.定义补偿任务和注册补偿任务回调

```
@PostConstruct
public void init() {

    //定义补偿任务
    RedoTask redoTask = RedoTask.newBuilder(this.redoTaskIdOne).maxAttempts(3).build();
    //注册
    RedoTaskRegisterFactory.register(redoTask, new RedoTaskCallback() {
        public RedoResult redo(RedoReqParam redoReqParam) {
            Map<String, Object> map = redoReqParam.getParams();
            String name = (String)map.get("name");
            String desc = (String)map.get("desc");
            //具体的待补偿方法
            doService(name, desc);
            return RedoResult.BIZ_ERROR;
        }
    });
}
```



可以使用匿名内部类实现RedoTaskCallback接口，也可以单独起一个类。
方法的入参即保存到数据库中的参数

## 4.业务代码中使用
需要补偿的时候，调用cn.com.kun.component.redo.core.RedoManager#addRedoTask进行添加

## 使用建议
假如是时效性高且不要求准确性的补偿任务，建议使用spring-retry，因为它更适合实时补偿的场景，它是在遇到错误时立刻重试。而Redo组件，是放入数据库再轮询进行补偿，牺牲了实时性，但提供了最终一致性。可以结合自身业务判断，同时使用spring-retry + Redo，例如在spring-retry的降级方法里调用RedoManager#addRedoTask，将补偿任务落地到数据库。

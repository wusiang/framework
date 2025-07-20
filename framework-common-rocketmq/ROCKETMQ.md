# 使用手册
## 1、pom引用
```xml
        <dependency>
            <groupId>com.xianmao</groupId>
            <artifactId>framework-rocketmq</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
## 2、配置
```yaml
rocketmq: 
  accessKey: xxx
  secretKey: xxx
  nameSrvAddr: xxx

```
## 2、使用指南
### b、生产者/消费者
继承AbstractMQProducer<br />
@MQKey 自定义消息类,指定字段为mqkey<br />
>例如：<br />

①、消息类
必需继承BaseMQ
```java
 @Data
public class Order extends BaseMQ {
    @MQKey
    private String orderNo;
}
```
①、生产类
```java
    @Log4j2
    @MQProducer
    public class OrderMQProducer extends AbstractMQProducer {
    
        public boolean sendMessage(String topic, String str) {
            //延时 传  3000  单位毫秒  表示3秒后消息
            Message msg = MessageBuilder.of(str).topic(topic).tag("tag").build();
            try {
                this.syncSend(msg);
            } catch (Exception e) {
                log.error(e);
                return false;
            }
            return true;
        }
    }
```
②、消费者
继承AbstractMQPushConsumer
```java
    @MQConsumer(topic = "TOPIC", tag = "tag")
    public class OrderMQConsumer extends AbstractMQPushConsumer<BaseMQ> {
    
        @Override
        public boolean process(BaseMQ message, ConsumeContext context) {
            System.out.println(message);
            return true;
        }
    }
```
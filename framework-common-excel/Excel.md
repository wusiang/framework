# excel-spring-boot-starter

- 轻松集成到 Spring Boot 项目中，快速实现 Excel 文件的导入和导出。
- 通过注解配置导入和导出的 Excel 文件格式。
- 提供了简洁易用的 API，极大地减少了手动处理 Excel 文件的工作量。

## 依赖引用

- 项目已上传至 maven 仓库，直接引入即可使用

```xml
<dependency>
  <groupId>com.xianmao</groupId>
  <artifactId>framework-common-excel</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 导入 Excel

### 控制器示例

你可以通过在接口方法中使用 `@RequestExcel` 注解来接收上传的 Excel 文件并将其解析为 Java 对象列表：

```java
@PostMapping("/upload")
public void upload(@RequestExcel List<DemoData> dataList, BindingResult bindingResult) {
  // JSR 303 校验通用校验获取失败的数据
  List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
}
```

### 实体类定义

需要先定义与 Excel 表格对应的实体类，并使用 `@ExcelProperty` 注解来标注 Excel 列的索引：

```java
@Data
public class Demo {
  @ExcelProperty(index = 0)
  private String username;

  @ExcelProperty(index = 1)
  private String password;
}
```

### 示例表格

下图展示了与上述实体类对应的 Excel 表格：

![Example Excel](https://minio.pigx.top/oss/1618560470.png)

## 导出 Excel

你只需在控制器方法中返回一个 `List`，并使用 `@ResponseExcel` 注解即可将数据导出为 Excel 文件：

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {
  String name() default "";
  ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;
  String password() default "";
  Sheet[] sheets() default @Sheet(sheetName = "sheet1");
  boolean inMemory() default false;
  String template() default "";
  String[] include() default {};
  String[] exclude() default {};
  Class<? extends WriteHandler>[] writeHandler() default {};
  Class<? extends Converter>[] converter() default {};
  Class<? extends HeadGenerator> headGenerator() default HeadGenerator.class;
}
```
package com.xianmao.common.log.aop;


import cn.hutool.core.util.StrUtil;
import com.xianmao.common.log.annotation.OperateLog;
import com.xianmao.common.log.enums.OperateLogEnums;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class OperateLogAspect {

//    @Autowired
//    private CarOperateLogRepository carOperateLogRepository;
//
//
//    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
//
//    @Pointcut("com.xianmao.common.log.annotation.OperateLog)")
//    public void pointCutService() {
//    }
//
//    @Around("pointCutService() && @annotation(operateLog)")
//    public Object around(ProceedingJoinPoint point, OperateLogV3 operateLog) throws Throwable {
//        //获取Request请求
//        HttpServletRequest request = getRequest();
//        if (null == request) {
//            return point.proceed();
//        }
//        if (!request.getContentType().equals(ContentType.JSON.getValue()) && !request.getContentType().equals(ContentType.FORM_URLENCODED.getValue())) {
//            return point.proceed();
//        }
//        // 获取用户信息
//        String adminId = request.getHeader("adminId");
//        if (StringUtils.isBlank(adminId)) {
//            return point.proceed();
//        }
//        TokenUserDto tokenUserDto = SecureUtils.getTokenUserDto(Long.parseLong(adminId));
//        if (null == tokenUserDto) {
//            return point.proceed();
//        }
//        // 解析参数
//        Map<String, Object> requestParams = convertRequestParams(point);
//        Object beforeRes = this.getBeforeResByCondition(operateLog, requestParams, tokenUserDto);
//        // 执行方法
//        Object result = point.proceed();
//        // 失败的情况不记录日志信息
//        APIResult apiResult = (APIResult) result;
//        if (!apiResult.isOk()) {
//            return result;
//        }
//
//        // 保存记录日志
//        this.operateLogSave(beforeRes, operateLog, requestParams, tokenUserDto);
//        return result;
//    }
//
//    private Map<String, Object> convertRequestParams(ProceedingJoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        // 请求参数处理
//        Object[] args = joinPoint.getArgs();
//        final Map<String, Object> paraMap = new HashMap<>(16);
//        for (int i = 0; i < args.length; i++) {
//            // 读取方法参数
//            MethodParameter methodParam = getMethodParameter(method, i);
//            // PathVariable 参数跳过
//            PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
//            if (pathVariable != null) {
//                continue;
//            }
//            RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
//            String parameterName = methodParam.getParameterName();
//            Object value = args[i];
//            // 如果是body的json则是对象
//            if (requestBody != null && value != null) {
//                paraMap.putAll(BeanUtil.beanToMap(value));
//                continue;
//            }
//            // 处理 List
//            if (value instanceof List) {
//                value = ((List) value).get(0);
//            }
//            // 处理 参数
//            if (value instanceof HttpServletRequest) {
//                paraMap.putAll(((HttpServletRequest) value).getParameterMap());
//            } else if (value instanceof WebRequest) {
//                paraMap.putAll(((WebRequest) value).getParameterMap());
//            } else if (value instanceof MultipartFile) {
//                MultipartFile multipartFile = (MultipartFile) value;
//                String name = multipartFile.getName();
//                String fileName = multipartFile.getOriginalFilename();
//                paraMap.put(name, fileName);
//            } else if (value instanceof HttpServletResponse) {
//            } else if (value instanceof InputStream) {
//            } else if (value instanceof InputStreamSource) {
//            } else if (value instanceof List) {
//                List<?> list = (List<?>) value;
//                AtomicBoolean isSkip = new AtomicBoolean(false);
//                for (Object o : list) {
//                    if ("StandardMultipartFile".equalsIgnoreCase(o.getClass().getSimpleName())) {
//                        isSkip.set(true);
//                        break;
//                    }
//                }
//                if (isSkip.get()) {
//                    paraMap.put(parameterName, "此参数不能序列化为json");
//                }
//            } else {
//                // 参数名
//                RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
//                String paraName;
//                if (requestParam != null && StrUtil.isNotBlank(requestParam.value())) {
//                    paraName = requestParam.value();
//                } else {
//                    paraName = methodParam.getParameterName();
//                }
//                paraMap.put(paraName, value);
//            }
//        }
//
//        return paraMap;
//    }
//
//    /**
//     * 保存操作日志
//     */
//    private void operateLogSave(Object beforeRes, OperateLogV3 operateLog, Map<String, Object> requestParams, TokenUserDto tokenUserDto) {
//        try {
//            String description = "";
//            //若填充资源可以从前端提交的数据获取时则走前端数据获取
//            if (operateLog.dataSource().equals("front")) {
//                description = fillDescription(requestParams, operateLog);
//            } else {
//                //处理不需要比对数据结果的内容
//                if ("no".equals(operateLog.reflectMethod().getDataCompare())) {
//                    //需要从server端获取数据的到这一步就只有更新需要（删除和新增都在前置查询中完成了）
//                    if (OperateTypeEnum.UPDATE.getType().equals(operateLog.operateType().getType())) {
//                        beforeRes = getDataFromServer(operateLog, tokenUserDto, requestParams);
//                    }
//                    description = this.fillDescriptionByObject(operateLog, beforeRes);
//                } else {
//                    //需要比对数据时，若前置获取的数据为空则直接返回
//                    if (beforeRes == null) {
//                        return;
//                    }
//                    Object resultRes = this.getDataFromServer(operateLog, tokenUserDto, requestParams);
//                    description = this.fillDescriptionByCompare(operateLog, beforeRes, resultRes);
//                }
//            }
//            //如果没有操作内容，不记录日志信息
//            if (StrUtil.isBlank(description)) {
//                return;
//            }
//            //记录日志数据
//            this.saveLog(requestParams, tokenUserDto, description, operateLog);
//        } catch (Exception e) {
//            log.info("日志切片错误{}", e.getMessage(), e);
//        }
//
//    }
//
//    /**
//     * 有条件获取方法处理前数据
//     *
//     * @return
//     */
//    private Object getBeforeResByCondition(OperateLog operateLog, Map<String, Object> requestParams, TokenUserDto tokenUserDto) {
//        Object beforeRes = null;
//        try {
//            //需要进行前置查询的类型为 需要进行数据比对 | 删除操作时需要前置查询 | 新增时数据需要从服务端获取的走前置查询
//            if ("yes".equals(operateLog.reflectMethod().getDataCompare())
//                    || OperateTypeEnum.DELETE.getType().equals(operateLog.operateType().getType())
//                    || (OperateTypeEnum.ADD.getType().equals(operateLog.operateType().getType()) && operateLog.dataSource().equals("server"))) {
//                beforeRes = getDataFromServer(operateLog, tokenUserDto, requestParams);
//            }
//        } catch (Exception e) {
//            log.info("日志反射错误{}", e.getMessage(), e);
//        }
//        return beforeRes;
//    }
//
//    /**
//     * 持久化操作日志
//     */
//    private void saveLog(Map<String, Object> requestParams, TokenUserDto tokenUserDto, String description, OperateLogV3 operateLog) {
//        HttpServletRequest request = getRequest();
//        String remoteIp = Convert.toStr(requestParams.get("ip"));
//        if (StringUtils.isBlank(remoteIp)) {
//            remoteIp = JsonUtil.getIpAddr(request);
//        }
//        //获取appId
//        String appId = Convert.toStr(request.getParameter("app_id"));
//        //获取操作平台
//        String platform = Convert.toStr(request.getHeader("platform"));
//        CarOperateLog carOperateLog = new CarOperateLog();
//        carOperateLog.setOperateModule(operateLog.module().getType());
//        carOperateLog.setOperateType(operateLog.operateType().getType());
//        carOperateLog.setAppId(appId);
//        carOperateLog.setDescription(description);
//        carOperateLog.setCompanyCode(tokenUserDto.getRootCompanyCode());
//        carOperateLog.setIpAddress(remoteIp);
//        carOperateLog.setOperateName(tokenUserDto.getTrueName());
//        carOperateLog.setOperateAccount(tokenUserDto.getAccount());
//        carOperateLog.setSource(platform);
//        carOperateLog.setUserId(tokenUserDto.getUserId());
//        carOperateLog.setCreateBy(tokenUserDto.getUserId());
//        carOperateLog.setUpdateBy(tokenUserDto.getUserId());
//        carOperateLog.setUri(request.getRequestURI());
//        String jsonParam = JsonUtil.toJsonString(requestParams);
//        if (StringUtils.isNotBlank(jsonParam)) {
//            carOperateLog.setParam(jsonParam.length() > 2000 ? jsonParam.substring(0, 2000) : jsonParam);
//        }
//        Date date = new Date();
//        carOperateLog.setCreateDate(date);
//        carOperateLog.setUpdateDate(date);
//
//        carOperateLogRepository.insert(carOperateLog);
//    }
//
//    private Object getDataFromServer(OperateLogV3 operateLog, TokenUserDto tokenUserDto, Map<String, Object> requestParams) throws Exception {
//        //获取反射类
//        Object object = SpringUtil.getBean(operateLog.reflectMethod().getClassName());
//        Method method = object.getClass().getDeclaredMethod(operateLog.reflectMethod().getMethodName(), String.class, TokenUserDto.class);
//        method.setAccessible(true);
//        //反射方法获取结果
//        Object result = method.invoke(object, JSONUtil.toJsonStr(requestParams), tokenUserDto);
//        return result;
//    }
//
//    /**
//     * 通过对比对象结果填充数据
//     */
//    private String fillDescriptionByCompare(OperateLogV3 operateLog, Object first, Object second) throws IllegalAccessException {
//        //进行数据比对
//        StringBuilder description = new StringBuilder(operateLog.description()[0].getDescription());
//        //获取需要填充的参数列表
//        List<String> parameters = Arrays.asList(StringUtils.split(operateLog.description()[0].getParameters(), ","));
//        if (CollectionUtils.isNotEmpty(parameters)) {
//            String[] fillData = new String[parameters.size()];
//            Field[] fields = first.getClass().getDeclaredFields();
//            for (int i = 0; i < parameters.size(); i++) {
//                for (Field field : fields) {
//                    field.setAccessible(true);
//                    if (field.getName().equals(parameters.get(i))) {
//                        fillData[i] = (String) field.get(first);
//                        break;
//                    }
//                }
//            }
//            description = new StringBuilder(String.format(description.toString(), fillData));
//        }
//        //获取数据不同的内容部分
//        Equator equator = new FieldBaseEquator();
//        List<FieldInfo> fieldInfos = equator.getDiffFields(first, second);
//        //若没有变更内容，不记录结果
//        if (CollectionUtils.isEmpty(fieldInfos)) {
//            return description.toString();
//        }
//        for (FieldInfo fieldInfo : fieldInfos) {
//            description.append(",").append("[").append(fieldInfo.getFieldNameCh()).append("]").append("改为").append("[").append(fieldInfo.getSecondVal()).append("]");
//        }
//        return description.toString();
//    }
//
//    /**
//     * 通过对象填充数据
//     */
//    private String fillDescriptionByObject(OperateLogV3 operateLog, Object object) throws Exception {
//        OperateLogEnums.Description[] descriptions = operateLog.description();
//        StringBuilder description = new StringBuilder();
//        for (OperateLogEnums.Description value : descriptions) {
//            //获取需要填充的参数列表
//            //获取需要填充的参数列表
//            List<String> parameters = Arrays.asList(StringUtils.split(value.getParameters(), ","));
//            if (CollectionUtils.isEmpty(parameters)) {
//                description.append(",").append(value.getDescription());
//            }
//            String[] fillData = new String[parameters.size()];
//            if (object != null) {
//                Field[] fields = object.getClass().getDeclaredFields();
//                for (int j = 0; j < parameters.size(); j++) {
//                    for (Field field : fields) {
//                        field.setAccessible(true);
//                        if (field.getName().equals(parameters.get(j))) {
//                            fillData[j] = (String) field.get(object);
//                            break;
//                        }
//                    }
//                }
//                if (StringUtils.isBlank(fillData[0])) {
//                    continue;
//                }
//                String originDescription = String.format(value.getDescription(), fillData);
//                description.append(",").append(originDescription);
//            }
//        }
//        if (description.length() > 0) {
//            return description.substring(1);
//        }
//        return description.toString();
//    }
//
//    /**
//     * 通过请求参数填充数据
//     */
//    private String fillDescription(Map<String, Object> requestParams, OperateLogV3 operateLog) {
//        StringBuilder description = new StringBuilder();
//        if (OperateTypeEnum.LOGIN.getType().equals(operateLog.operateType().getType())) {
//            String from = Convert.toStr(requestParams.get("from"));
//            if (SourceTypeEnum.APP.getType().equals(from)) {
//                // 管理端登录无需记录
//                return description.toString();
//            }
//            OperateLogEnums.Description[] loginDescription = operateLog.description();
//            OperateLogEnums.Description descriptionEnum = loginDescription[0];
//            description = new StringBuilder(descriptionEnum.getDescription());
//            String remoteIp = IpAddressUtils.getIpAddr(getRequest());
//            if (StringUtils.isNotEmpty(remoteIp)) {
//                description.append("，IP为").append(remoteIp);
//            }
//            return description.toString();
//        }
//
//        OperateLogEnums.Description[] descriptions = operateLog.description();
//        for (int i = 0; i < descriptions.length; i++) {
//            //获取需要填充的参数列表
//            List<String> parameters = Arrays.asList(StringUtils.split(descriptions[i].getParameters(), ","));
//            if (CollectionUtils.isEmpty(parameters)) {
//                description.append(",").append(descriptions[i].getDescription());
//            }
//            String[] fillData = new String[parameters.size()];
//            for (int j = 0; j < parameters.size(); j++) {
//                fillData[j] = Convert.toStr(requestParams.get(parameters.get(j)));
//            }
//            //如果取到的参数为空则直接跳出，不填充该段数据内容
//            if (matchBlank(fillData)) {
//                continue;
//            }
//            String originDescription = String.format(descriptions[i].getDescription(), fillData);
//            description.append(",").append(originDescription);
//        }
//        return description.substring(1);
//    }
//
//    /**
//     * 判断填充数据内容是否有空值
//     */
//    private boolean matchBlank(String[] fillData) {
//        boolean res = false;
//        for (String data : fillData) {
//            if (StringUtils.isBlank(data)) {
//                res = true;
//                break;
//            }
//        }
//        return res;
//    }
//
//    /**
//     * 获取方法参数信息
//     */
//    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
//        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
//        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
//        return methodParameter;
//    }
//
//    public static HttpServletRequest getRequest() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
//    }
}

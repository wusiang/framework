package com.xianmao.aspect;

import com.alibaba.fastjson.JSON;
import com.xianmao.annotation.ApiLog;
import com.xianmao.bean.MethodInfo;
import com.xianmao.bean.MethodParser;
import com.xianmao.enums.ArrayType;
import com.xianmao.enums.Level;
import com.xianmao.enums.Position;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName WebLogAspect
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 23:26
 * @Version 1.0
 */
@Aspect
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    /**
     * 代表本地方法，不进行代码定位
     */
    private static final int LINE_NUMBER = -2;

    private static String[] types = {
            "java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Char",
            "java.lang.String",
            "int",
            "double",
            "long",
            "short",
            "byte",
            "boolean",
            "char",
            "float",
            "java.util.List"};

    public WebLogAspect() {
        log.info("Starting Initializing WebLogAspect");
    }

    /**
     * 打印环绕日志
     *
     * @param joinPoint 切入点
     * @return 返回方法返回值
     * @throws Throwable 异常
     */
    @Around(value = "@annotation(com.xianmao.annotation.ApiLog)")
    public Object aroundPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed(args);
        if (this.isEnable()) {
            ApiLog annotation = signature.getMethod().getAnnotation(ApiLog.class);
            this.arountPrint(signature, args, result, annotation.value(), annotation.level(), annotation.position());
        }
        return result;
    }

    /**
     * 打印环绕通知日志
     *
     * @param signature 方法签名
     * @param args      参数列表
     * @param result    返回结果
     * @param busName   业务名称
     * @param level     日志级别
     * @param position  代码定位开启标志
     */
    private void arountPrint(MethodSignature signature, Object[] args, Object result, String busName, Level level, Position position) {

        Method method = signature.getMethod();
        String methodName = method.getName();
        try {
            if (log.isDebugEnabled()) {
                if (position == Position.DEFAULT || position == Position.ENABLED) {
                    this.print(
                            level,
                            getAroundInfo(
                                    busName,
                                    MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames()),
                                    args, result
                            )
                    );
                } else {
                    this.print(
                            level,
                            getAroundInfo(
                                    busName,
                                    MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames()),
                                    args, result
                            )
                    );
                }
            } else {
                if (position == Position.ENABLED) {
                    this.print(
                            level,
                            getAroundInfo(
                                    busName,
                                    MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames()),
                                    args, result
                            )
                    );
                } else {
                    this.print(
                            level,
                            getAroundInfo(
                                    busName,
                                    MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames()),
                                    args, result
                            )
                    );
                }
            }
        } catch (Exception e) {
            log.error("{}.{}方法错误", signature.getDeclaringTypeName(), methodName);
        }
    }

    /**
     * 获取日志信息字符串
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @param result     返回结果
     * @param args       参数
     * @return 返回日志信息字符串
     */
    private String getAroundInfo(String busName, MethodInfo methodInfo, Object[] args, Object result) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getBeforeInfo(busName, methodInfo, args)).append(",").append("结果:").append(JSON.toJSONString(result));
        return builder.toString();
    }

    /**
     * 获取日志信息字符串
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @param params     参数值
     * @return 返回日志信息字符串
     */
    private String getBeforeInfo(String busName, MethodInfo methodInfo, Object[] params) {
        StringBuilder builder = this.createInfoBuilder(busName, methodInfo);
        builder.append("入参:");
        List<String> paramNames = methodInfo.getParamNames();
        int count = paramNames.size();
        if (count > 0) {
            Map<String, Object> paramMap = new HashMap<>(count);
            for (int i = 0; i < count; i++) {
                Class<?> type = params[i].getClass();
                if (!type.isArray()) {
                    paramMap.put(paramNames.get(i), getFieldsValue(params[i]));
                } else {
                    paramMap.put(paramNames.get(i), this.getParam(params[i]));
                }
            }
            return builder.append(paramMap).append("").toString();
        }
        return builder.append("{}").toString();
    }

    /**
     * 创建日志信息builder
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @return 返回日志信息builder
     */
    private StringBuilder createInfoBuilder(String busName, MethodInfo methodInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("方法:");
        if (methodInfo.getLineNumber() == LINE_NUMBER) {
            builder.append(methodInfo.getClassAllName()).append(".").append(methodInfo.getMethodName());
        } else {
            builder.append(this.createMethodStack(methodInfo));
        }
        return builder.append(",").append("业务:").append(busName).append(",");
    }

    /**
     * 获取对应参数
     *
     * @param param 参数
     * @return 返回参数
     */
    private Object getParam(Object param) {
        Class<?> type = param.getClass();
        return type.isArray() ? this.getList(type, param) : param;
    }

    /**
     * 解析实体类，获取实体类中的属性
     *
     * @param obj 实体类
     * @return 实体类信息
     */
    private static String getFieldsValue(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = obj.getClass().getTypeName();
        for (String t : types) {
            if (t.equals(typeName)) {
                return (String) obj;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                for (String str : types) {
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName()).append(":").append(f.get(obj)).append(",");
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 获取数组类型参数列表
     *
     * @param valueType 数组类型
     * @param value     参数值
     * @return 返回参数列表
     */
    @SuppressWarnings("unchecked")
    private List<Object> getList(Class valueType, Object value) {
        if (valueType.isAssignableFrom(ArrayType.OBJECT_ARRAY.getType())) {
            Object[] array = (Object[]) value;
            List<Object> list = new ArrayList<>(array.length);
            Collections.addAll(list, array);
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.INT_ARRAY.getType())) {
            int[] array = (int[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (int v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.LONG_ARRAY.getType())) {
            long[] array = (long[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (long v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.DOUBLE_ARRAY.getType())) {
            double[] array = (double[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (double v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.FLOAT_ARRAY.getType())) {
            float[] array = (float[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (float v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.CHAR_ARRAY.getType())) {
            char[] array = (char[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (char v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.BOOLEAN_ARRAY.getType())) {
            boolean[] array = (boolean[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (boolean v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.BYTE_ARRAY.getType())) {
            byte[] array = (byte[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (byte v : array) {
                list.add(v);
            }
            return list;
        } else if (valueType.isAssignableFrom(ArrayType.SHORT_ARRAY.getType())) {
            short[] array = (short[]) value;
            List<Object> list = new ArrayList<>(array.length);
            for (short v : array) {
                list.add(v);
            }
            return list;
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     * 创建方法栈
     *
     * @param methodInfo 方法信息
     * @return 返回栈信息
     */
    private StackTraceElement createMethodStack(MethodInfo methodInfo) {
        return new StackTraceElement(
                methodInfo.getClassAllName(),
                methodInfo.getMethodName(),
                String.format("%s.java", methodInfo.getClassSimpleName()),
                methodInfo.getLineNumber()
        );
    }

    /**
     * 打印信息
     *
     * @param level 日志级别
     * @param msg   输出信息
     */
    private void print(Level level, String msg) {
        switch (level) {
            case DEBUG:
                log.debug(msg);
                break;
            case INFO:
                log.info(msg);
                break;
            case WARN:
                log.warn(msg);
                break;
            case ERROR:
                log.error(msg);
                break;
            default:
        }
    }

    /**
     * 判断是否开启打印
     *
     * @return 返回布尔值
     */
    private boolean isEnable() {
        return log.isDebugEnabled() ||
                log.isInfoEnabled() ||
                log.isWarnEnabled() ||
                log.isErrorEnabled() ||
                log.isTraceEnabled();
    }
}

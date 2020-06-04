package com.xianmao.aspect;

import com.alibaba.fastjson.JSON;
import com.xianmao.annotation.ApiLog;
import com.xianmao.enums.Level;
import com.xianmao.utils.BeanUtil;
import com.xianmao.jackson.JsonUtil;
import com.xianmao.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        ApiLog annotation = signature.getMethod().getAnnotation(ApiLog.class);
        this.arountPrint(signature, args, result, annotation.value(), annotation.level());
        return result;
    }

    private void arountPrint(MethodSignature signature, Object[] args, Object result, String busName, Level level) {

        Method method = signature.getMethod();
        try {
            String roungInfo = getAroundInfo(busName, method, args, result);
            print(level, roungInfo);
        } catch (Exception e) {
            log.error("日志打印错误:{}", e);
        }
    }


    private String getAroundInfo(String busName, Method method, Object[] args, Object result) {
        StringBuilder builder = new StringBuilder();
        long startNs = System.nanoTime();
        builder.append(getParamInfo(busName, method, args)).append(",").append("结果:").append(JSON.toJSONString(result));
        builder.append(getHeaderInfo());
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        builder.append("耗时:"+tookMs+"ms");
        return builder.toString();
    }


    private String getParamInfo(String busName, Method method, Object[] args) {
        StringBuilder builder = createInfoBuilder(busName, method);
        builder.append("入参:");
        final Map<String, Object> paraMap = new HashMap<>(16);
        for (int i = 0; i < args.length; i++) {
            MethodParameter methodParam = getMethodParameter(method, i);
            PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
            if (pathVariable != null) {
                continue;
            }
            RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
            Object object = args[i];
            // 如果是body的json则是对象
            if (requestBody != null && object != null) {
                paraMap.putAll(BeanUtil.toMap(object));
            } else {
                RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
                String paraName;
                if (requestParam != null && StringUtil.isNotBlank(requestParam.value())) {
                    paraName = requestParam.value();
                    paraMap.put(paraName, object);
                } else {
                    paraName = methodParam.getParameterName();
                    paraMap.put(paraName, JsonUtil.toJson(object));
                }
            }
        }
        builder.append(paraMap.toString());
        return builder.toString();
    }

    private String getHeaderInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            return "";
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Enumeration<String> headers = request.getHeaderNames();
        StringBuilder builder = new StringBuilder();
        builder.append("请求头:");
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            String value = request.getHeader(key);
            builder.append(String.format("{%s}:{%s}", key, value));
        }
        return builder.toString();
    }

    private StringBuilder createInfoBuilder(String busName, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append("方法:");
        builder.append(method.getName());
        return builder.append(",").append("业务:").append(busName).append(",");
    }

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

    private MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
        return methodParameter;
    }
}

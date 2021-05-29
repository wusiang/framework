package com.xianmao.cloud.feign;

import com.fasterxml.jackson.databind.JsonNode;
import com.xianmao.jackson.JsonUtil;
import com.xianmao.obj.ObjectUtil;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * fallBack 代理处理
 *
 * @author L.cm
 */
@Slf4j
@AllArgsConstructor
public class XianmaoFeignFallback<T> implements MethodInterceptor {

    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;
    private final static String CODE = "code";

    @Nullable
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String errorMessage = cause.getMessage();
        log.error("BladeFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
        Class<?> returnType = method.getReturnType();
        // 暂时不支持 flux，rx，异步等，返回值不是 R，直接返回 null。
        Map<String, Object> errorAttributes = new HashMap<>();
        if (Map.class != returnType) {
            return null;
        }
        // 非 FeignException
        if (!(cause instanceof FeignException)) {
            errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
            errorAttributes.put("message", errorMessage);
            return errorAttributes;
        }
        FeignException exception = (FeignException) cause;
        byte[] content = exception.content();
        // 如果返回的数据为空
        if (ObjectUtil.isEmpty(content)) {
            errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
            errorAttributes.put("message", errorMessage);
            return errorAttributes;
        }
        // 转换成 jsonNode 读取，因为直接转换，可能 对方放回的并 不是 R 的格式。
        JsonNode resultNode = JsonUtil.readTree(content);
        // 判断是否 R 格式 返回体
        if (resultNode.has(CODE)) {
            return JsonUtil.getInstance().convertValue(resultNode, Map.class);
        }
        errorAttributes.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
        errorAttributes.put("message", resultNode.toString());
        return errorAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XianmaoFeignFallback<?> that = (XianmaoFeignFallback<?>) o;
        return targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType);
    }
}

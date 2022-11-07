package com.xianmao.common.core.exception;

import com.xianmao.common.core.support.StringPool;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;

public class Error {

    public static String buildError(IErrorCode code, String msg) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String paramInfo = null;
        try {
            // 不影响输出
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            paramInfo = new String(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        } catch (Exception ignored) {

        }
        return buildErrorInfo(code, msg, request.getMethod() + ":" + request.getRequestURI(), paramInfo);
    }

    public static String buildErrorInfo(IErrorCode code, String errorMsg, String method, String paramInfo, Object... extra) {
        StringBuilder sb = new StringBuilder();
        sb.append("----");
        sb.append(StringPool.PIPE);
        sb.append("错误码:");
        sb.append(code.getCode());
        sb.append(StringPool.PIPE);
        sb.append("方法:");
        sb.append(method);
        sb.append(StringPool.PIPE);
        sb.append("参数:");
        sb.append(paramInfo);
        sb.append(StringPool.PIPE);
        sb.append("错误信息:");
        sb.append(errorMsg);
        if (extra != null && extra.length > 0) {
            for (Object o : extra) {
                sb.append(StringPool.PIPE);
                sb.append(o);
            }
        }
        return sb.toString();
    }
}

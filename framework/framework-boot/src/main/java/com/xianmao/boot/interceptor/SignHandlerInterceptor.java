package com.xianmao.boot.interceptor;


import com.xianmao.boot.annotion.IgnoreSign;
import com.xianmao.boot.web.APIResult;
import com.xianmao.constant.Constants;
import com.xianmao.jackson.JsonUtil;
import com.xianmao.utils.MapUtil;
import com.xianmao.utils.ServletUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

public abstract class SignHandlerInterceptor extends HandlerInterceptorAdapter {

    /**
     * 签名超时时长，默认时间为5分钟，ms
     */
    private int expiredTime = 5 * 60 * 1000;

    private String signKey = "sign";

    private static final String TIMESTAMP_KEY = "timestamp";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 忽略签名
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(IgnoreSign.class)) {
            return true;
        }
        //1、验证签名是否正确
        String timestamp = request.getHeader(TIMESTAMP_KEY);
        String sign = request.getHeader(this.signKey);
        //2、判断时间戳是否符合格式要求
        if (StringUtils.isEmpty(timestamp) || !StringUtils.isNumeric(timestamp)) {
            ServletUtil.write(response, JsonUtil.toJson(APIResult.fail("请求时间戳不合法!")), Constants.ContentType.JSON_CONTENT_TYPE);
            return false;
        }
        //3、判断时间戳是否在超时
        long ts = Long.parseLong(timestamp);
        if (System.currentTimeMillis() - ts > expiredTime) {
            ServletUtil.write(response, JsonUtil.toJson(APIResult.fail("请求过期")), Constants.ContentType.JSON_CONTENT_TYPE);
        }
        //4、判断签名是否存在
        if (StringUtils.isEmpty(sign)) {
            ServletUtil.write(response, JsonUtil.toJson(APIResult.fail("认证无法通过!")), Constants.ContentType.JSON_CONTENT_TYPE);
        }
        //5、判断签名是否正确
        String secret = "61aTLFow7l5flcjw9wxz9KE9VpwUcLi3b68pFSs9d7xvvNnsx0LI5rcXTNx3X7Qfw3KEqaNAUmIN3rXzpWBRHIcIZnFz94VCRHhB1G7jaFQroEn5I4NImkADk3gN68YLCnGz6bkE12TudKXNsw2KMri2yxTl3mKb3OG1DMC1P3l8P3GoLoRaPTVrZQQpFAU5XM8XjN6YklZaNPbLbeU533fhow1bHwLGttr9kECBdumVDLAl0n9jMJ3nJPjPC317";
        if (!verificationSign(request, secret, timestamp)) {
            ServletUtil.write(response, JsonUtil.toJson(APIResult.fail("认证无法通过!")), Constants.ContentType.JSON_CONTENT_TYPE);
        }
        return true;
    }

    public abstract boolean verificationSign(HttpServletRequest request, String accessSecret, String timestamp);

    public abstract String createSign(Map<String, Object> params, String accessSecret, String timestamp);

}

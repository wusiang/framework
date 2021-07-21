package com.xianmao.boot.interceptor;

import com.xianmao.boot.annotion.RepeatSubmit;
import com.xianmao.boot.web.APIResult;
import com.xianmao.constant.Constants;
import com.xianmao.jackson.JsonUtil;
import com.xianmao.utils.ServletUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request)) {
                    APIResult<Object> ajaxResult = APIResult.fail("不允许重复提交，请稍后再试");
                    ServletUtil.write(response, JsonUtil.toJson(ajaxResult), Constants.ContentType.JSON_CONTENT_TYPE);
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request) throws Exception;
}
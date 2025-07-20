package com.xianmao.common.core.log;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志拦截器
 */
@Aspect
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class LogAspect {

    @Around("@annotation(com.xianmao.common.core.log.BizLog)")
    public Object invoke(ProceedingJoinPoint point) throws Throwable {
        Object obj = null;
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        BizLog bizLog = method.getAnnotation(BizLog.class);
        //日志打印
        Logger logger = LoggerFactory.getLogger(point.getTarget().getClass());
        //类.方法
        String name = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        //计算耗时
        long start = System.currentTimeMillis();
        //日志输入打印
        if (bizLog.value() != BizLog.NotifyEnum.After) {
            logger.info(">>>>>>>>>>>[begin]|{}|{}", name, JSON.toJSONString(point.getArgs()));
        }
        //执行
        try {
            obj = point.proceed();
        } catch (Throwable e) {
            //如果报错,强制打印输入
            if (bizLog.value() == BizLog.NotifyEnum.After) {
                logger.info(">>>>>>>>>>>[error]|{}|{}", name, JSON.toJSONString(point.getArgs()));
            }
            throw e;
        } finally {
            if (bizLog.value() != BizLog.NotifyEnum.Before) {
                long costTime = System.currentTimeMillis() - start;
                String paramInfo = null;
                if (null != obj) {
                    paramInfo = JSON.toJSONString(obj);
                    //限制输出大小
                    if (StrUtil.isNotBlank(paramInfo) && paramInfo.length() > 2048) {
                        paramInfo = paramInfo.substring(0, 2048);
                    }
                }
                logger.info(">>>>>>>>>>>[end]|{}|{}|{}", name, costTime, paramInfo);
            }
        }
        return obj;
    }
}

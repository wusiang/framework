//package com.xianmao.common.xxl.interceptor;
//
//import com.xianmao.common.xxl.trace.TraceConstant;
//import com.xianmao.common.xxl.trace.XxlJobTrace;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.MDC;
//import org.springframework.core.annotation.Order;
//
//@Aspect
//@Order(-1)
//public class XxlJobInterceptor {
//
//    @Around("@annotation(xxlJob)")
//    public Object handle(ProceedingJoinPoint point, XxlJob xxlJob) throws Throwable{
//        try{
//            MDC.put(TraceConstant.LEGACY_TRACE_ID_NAME, XxlJobTrace.traceIdString());
//            return point.proceed();
//        } finally{
//            MDC.remove(TraceConstant.LEGACY_TRACE_ID_NAME);
//        }
//    }
//}

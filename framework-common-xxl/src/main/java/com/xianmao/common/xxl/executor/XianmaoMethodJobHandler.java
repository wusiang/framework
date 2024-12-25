//package com.xianmao.common.xxl.executor;
//
//
//import com.xianmao.common.xxl.trace.TraceConstant;
//import com.xianmao.common.xxl.trace.XxlJobTrace;
//import com.xxl.job.core.handler.IJobHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.MDC;
//
//import java.lang.reflect.Method;
//
//@Slf4j
//public class XianmaoMethodJobHandler extends IJobHandler {
//
//    public static final String TRACE_ID = TraceConstant.LEGACY_TRACE_ID_NAME;
//
//    private final Object target;
//    private final Method method;
//    private final Method initMethod;
//    private final Method destroyMethod;
//
//    public XianmaoMethodJobHandler(Object target, Method method, Method initMethod, Method destroyMethod) {
//        this.target = target;
//        this.method = method;
//        this.initMethod = initMethod;
//        this.destroyMethod = destroyMethod;
//    }
//
//    public void execute() throws Exception {
//        try {
//            MDC.put(TRACE_ID, XxlJobTrace.traceIdString());
//            Class<?>[] paramTypes = this.method.getParameterTypes();
//            if (paramTypes.length > 0) {
//                this.method.invoke(this.target);
//            } else {
//                this.method.invoke(this.target);
//            }
//        } catch (Exception e) {
//            log.error(target.getClass().getName() + " 执行 " + method.getName() + " 出错", e);
//            throw e;
//        } finally {
//            MDC.remove(TRACE_ID);
//        }
//    }
//
//    public void init() throws Exception {
//        if (this.initMethod != null) {
//            this.initMethod.invoke(this.target);
//        }
//
//    }
//
//    public void destroy() throws Exception {
//        if (this.destroyMethod != null) {
//            this.destroyMethod.invoke(this.target);
//        }
//
//    }
//
//    public String toString() {
//        return super.toString() + "[" + this.target.getClass() + "#" + this.method.getName() + "]";
//    }
//}

package com.xianmao.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import com.xianmao.common.log.annotation.SysLog;
import com.xianmao.common.log.enums.LogTypeEnum;
import com.xianmao.common.log.event.SysLogEvent;
import com.xianmao.common.log.model.SysLogInfo;
import com.xianmao.common.log.utils.SpringContextHolder;
import com.xianmao.common.log.utils.SysLogUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class SysLogAspect {

	@Value("${spring.application.name}")
	private String serviceId;

	@Around("@annotation(sysLog)")
	@SneakyThrows
	public Object around(ProceedingJoinPoint point, SysLog sysLog) {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);
		//todo 待完善

		String value = sysLog.value();
		String expression = sysLog.expression();
		// 当前表达式存在 SPEL，会覆盖 value 的值
		if (StrUtil.isNotBlank(expression)) {
			// 解析SPEL
			MethodSignature signature = (MethodSignature) point.getSignature();
			EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
			try {
				value = SysLogUtils.getValue(context, expression, String.class);
			}
			catch (Exception e) {
				// SPEL 表达式异常，获取 value 的值
				log.error("@SysLog 解析SPEL {} 异常", expression);
			}
		}

		SysLogInfo sysLogInfo = SysLogUtils.getSysLog();
		sysLogInfo.setTitle(value);
		sysLogInfo.setServiceId(serviceId);

		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj;

		try {
			obj = point.proceed();
		}
		catch (Exception e) {
			sysLogInfo.setType(LogTypeEnum.ERROR.getType());
			sysLogInfo.setException(e.getMessage());
			throw e;
		}
		finally {
			Long endTime = System.currentTimeMillis();
			sysLogInfo.setTime(endTime - startTime);
			SpringContextHolder.publishEvent(new SysLogEvent(sysLogInfo));
		}

		return obj;
	}

}

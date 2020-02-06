package com.xz.blog.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecuteTimeAOP {

	private static final Logger log = LoggerFactory.getLogger(ExecuteTimeAOP.class);

	@Pointcut("@annotation(com.xz.blog.common.ExecuteTime)")
	public void executeTime() {
	};

	@Around("executeTime()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long start = System.currentTimeMillis();

		Object result = point.proceed();

		long time = System.currentTimeMillis() - start;
		logExecuteTime(point, time);
		return result;
	}

	private void logExecuteTime(ProceedingJoinPoint point, long time) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		String className = point.getTarget().getClass().getName();
		String methodName = signature.getName();
		log.info("class:" + className + " method:" + methodName + "() 耗时:" + time + "ms");
	}

}

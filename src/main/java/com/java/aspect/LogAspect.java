package com.java.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	@Before("execution(* com.java.controller.*.*(..))")
	public void beforeMethod(JoinPoint joinPoint){
		System.out.println("beforeMethod");
		joinPoint.getArgs();
		logger.info("beforeMethod");
	}
	
	@After("execution(* com.java.controller.*.*(..))")
	public void afterMethod(){
		System.out.println("afterMethod");
		logger.info("afterMethod");
	}

}

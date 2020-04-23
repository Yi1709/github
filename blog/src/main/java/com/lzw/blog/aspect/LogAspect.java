package com.lzw.blog.aspect;


import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Auther: lzw
 * @Date: 2020/04/19/21:14
 * @Description:
 */
@Aspect
@Component
public class LogAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(* com.lzw.blog.web.*.*(..))")
	public void log() {
	}

	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
		String classMethod =
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		Requestlog requestlog = new Requestlog(url, ip, classMethod, args);
		this.logger.info("Request:{}",requestlog);
	}

	@After("log()")
	public void doAfter() {
		//this.logger.info("----doAfter----");
	}

	@AfterReturning(returning = "result", pointcut = "log()")
	public void doAfterReturn(Object result) {
		this.logger.info("Result:{}", result);
	}

	private class Requestlog {
		private String url;
		private String ip;
		private String classMethd;
		private Object[] args;

		public Requestlog(String url, String ip, String classMethd, Object[] args) {
			this.url = url;
			this.ip = ip;
			this.classMethd = classMethd;
			this.args = args;
		}

		@Override
		public String toString() {
			return "Requestlog{" +
					"url='" + url + '\'' +
					", ip='" + ip + '\'' +
					", classMethd='" + classMethd + '\'' +
					", args=" + Arrays.toString(args) +
					'}';
		}
	}

}

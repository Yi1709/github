package com.lzw.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: lzw
 * @Date: 2020/04/19/18:41
 * @Description:
 */
@ControllerAdvice
public class ControllerException {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {

		logger.error("Request URL:{},Exception:{}", request.getRequestURL(), e.getMessage());
		if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){
			throw e;
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("url", request.getRequestURL());
		modelAndView.addObject("exception", e);
		modelAndView.setViewName("error/error");
		return modelAndView;
	}
}

package com.lzw.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/15:45
 * @Description:
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getSession().getAttribute("currentUser") == null) {
			response.sendRedirect("/admin");
			return false;
		}
		return true;
	}
}

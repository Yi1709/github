package com.myshare.code.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;

@Configuration
public class AllFilter implements Filter {

	@Bean
	public FilterRegistrationBean<AllFilter> getAllFilter() {
		FilterRegistrationBean<AllFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new AllFilter());
		filter.addUrlPatterns("/*");
		return filter;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}

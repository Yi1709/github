package com.lzw.blog.interceptor;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/15:48
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin").excludePathPatterns("/admin/login");
	}
}

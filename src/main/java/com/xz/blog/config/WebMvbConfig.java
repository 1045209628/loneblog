package com.xz.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xz.blog.interceptor.LoginInterceptor;
import com.xz.blog.interceptor.RouteInterceptor;

@Configuration
public class WebMvbConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		String[] paths = { "/**/save", "/user/setting/**", "/**/upload","/**/cancel","/user/edit/**","/**/manager" };

		registry.addInterceptor(new RouteInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns(paths);
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

}

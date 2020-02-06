package com.xz.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class RouteInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// referer
		String referer = request.getHeader("referer");
		if (referer != null)
			request.setAttribute("referer", referer);
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}

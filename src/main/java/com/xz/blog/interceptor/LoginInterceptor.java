package com.xz.blog.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.xz.blog.common.WebConstant;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.User;

public class LoginInterceptor implements HandlerInterceptor {
	
	Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		UserDTO user = (UserDTO) request.getSession().getAttribute(WebConstant.USER_SESSION_KEY);
		if (user==null) {
			//System.out.println("无user，拦截器执行:"+request.getRequestURL());
			log.info("无用户，拦截器执行:"+request.getRemoteAddr()+":"+request.getRequestURL());
			reDirect(request, response);
			return false;
		}
		return true;
		
		//return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	 private void reDirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        // 获取当前请求的路径
	        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
	                + request.getContextPath();
	        // 如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理
	        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
	            // 告诉ajax我是重定向
	            response.setHeader("REDIRECT", "REDIRECT");
	            // 告诉ajax我重定向的路径
	            response.setHeader("Location", basePath + "/passport/login/page");
	            response.setStatus(HttpServletResponse.SC_ACCEPTED);
	        } else {
	            response.sendRedirect(basePath + "/passport/login/page");
	        }
	    }
}

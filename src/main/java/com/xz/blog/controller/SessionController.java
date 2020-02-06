package com.xz.blog.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xz.blog.common.WebConstant;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.User;

@Controller
public class SessionController {

	private static final Logger log = LoggerFactory.getLogger(SessionController.class);

	@Autowired
	private HttpSession session;

	public HttpSession getSession() {
		return session;
	}

	public UserDTO getSessionUser() {
		Object user = session.getAttribute(WebConstant.USER_SESSION_KEY);
		if (user != null)
			log.debug(user.toString());
		return (UserDTO) user;
	}

	public void setSessionUser(UserDTO user) {
		if (user != null)
			log.debug(user.toString());
		session.setAttribute(WebConstant.USER_SESSION_KEY, user);
	}

}

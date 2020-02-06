package com.xz.blog.controller;

import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.common.UserValidation;
import com.xz.blog.common.WebConstant;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.MailService;
import com.xz.blog.service.UserService;
import com.xz.blog.util.DataTransferUtil;
import com.xz.blog.util.EncryptUtil;
import com.xz.blog.util.UUIDUtil;

@Controller
@RequestMapping("/passport")
public class PassportController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(PassportController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService mailService;
	
	
	@RequestMapping("/login/page")
	public String showLoginPage(Model model, HttpServletRequest req) {
		String referer = req.getHeader("referer");
		model.addAttribute("referer", referer);
		return "login";
	}

	@GetMapping("/check")
	@ResponseBody
	public FrontResult checkLogin() {
		try {
			Object user = getSessionUser();
			if (user == null) {
				return FrontResult.build(FrontResult.FAULT, "用户尚未登录");
			} else
				return FrontResult.success("用户已登录");
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}
	}

	@PostMapping("/login")
	@ResponseBody
	public FrontResult userLogin(String userPassport, String password) {
		try {
			FrontResult result = userService.getUser(userPassport, password);
			if (result.getStatus().equals(FrontResult.SUCCESS)) {
				// 先存入session,后面改为redis
				setSessionUser((UserDTO) result.getData());
			}
			return result;
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}
	}

	@PostMapping("/logout")
	@ResponseBody
	public FrontResult userLogout() {
		try {
			UserDTO user = getSessionUser();
			if (user == null) {
				return FrontResult.build(FrontResult.FAULT, "用户已经不存在","/home");
			} else {
				getSession().removeAttribute(WebConstant.USER_SESSION_KEY);
				getSession().invalidate();
				return FrontResult.success("注销成功");
			}
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}
	}

	@RequestMapping("/register/page")
	public String showRegisterPage(HttpServletRequest req, Model model) {
		String referer = req.getHeader("referer");
		model.addAttribute("referer", referer);
		return "register";
	}

	@PostMapping("/register")
	@ResponseBody
	@ExecuteTime
	public FrontResult userRegister(User user) {
		try {
			if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null)
				return FrontResult.fault("数据不完整");
			if(!UserValidation.isUserName(user.getUsername()))
				return FrontResult.fault("用户名不符合规范");
			if(!UserValidation.isEmail(user.getEmail()))
				return FrontResult.fault("邮箱不符合格式");
			user.setCreateTime(new Date());
			user.setType(1);
			user.setPassword(EncryptUtil.encodeSha256(user.getPassword()));
			user.setActivecode(UUIDUtil.getUUID());
			userService.insertUser(user);
			//发送激活邮件
			UserDTO userDTO = DataTransferUtil.userToDTO(user, null);
			mailService.sendMailtoActive(userDTO);
			return FrontResult.success("注册成功");
		} catch (MessagingException e) {
			log.error("邮件编码异常",e);
			return FrontResult.error(e);
		} catch (InterruptedException e) {
			log.error("异步异常",e);
			return FrontResult.error(e);
		} catch (Exception e) {
			log.error("发生未知异常",e);
			return FrontResult.error(e);
		}
	}

	@PostMapping("/check")
	@ResponseBody
	public FrontResult checkPassport(String passport) {
		FrontResult result = userService.hasUserByPassport(passport);
		return result;
	}
	
	@GetMapping("/active")
	@ResponseBody
	@ExecuteTime
	public FrontResult activeUser(String code,Integer id) {
		User user = userService.getUser(id);
		if(user==null) {
			return FrontResult.fault("没有该用户");
		}
		UserDTO userDTO = DataTransferUtil.userToDTO(user, null);
		if(StringUtils.isBlank(userDTO.getActiveCode())) {
			return FrontResult.fault("用户已激活");
		}
		
		if(userDTO.getActiveCode().equals(code)) {
			user.setActivecode(null);
			try {
				userService.updateUserCover(user);
				return FrontResult.success("激活成功");
			} catch (Exception e) {
				log.error("用户激活失败",e);
				return FrontResult.error(e);
			}
		}
		else {
			return FrontResult.fault("激活码不正确");
		}
		
	}

}

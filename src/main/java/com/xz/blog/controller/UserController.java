package com.xz.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.BlogService;
import com.xz.blog.service.CollectionService;
import com.xz.blog.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CollectionService collectionService;
	
	@Autowired
	private BlogService blogService;

	@GetMapping("/{userId}")
	public String getUserDetail(@PathVariable Integer userId, Model model) {
		UserDTO userDTO = userService.getUserDTO(userId);
		if(userDTO==null) {
			return "error/404";
		}
		model.addAttribute("user", userDTO);
		try {
			Integer colCount = collectionService.getCountByUser(userId);
			model.addAttribute("collectionCount", colCount);
			return "user";
		} catch (Exception e) {
			log.error("发生异常",e);
			return "500";
		}

	}
	
	@GetMapping("/manager")
	public String getManagerPage(Model model) {
		UserDTO userDTO = getSessionUser();
		if(userDTO==null) {
			return "error/404";
		}
		model.addAttribute("user", userDTO);
		try {
			Integer colCount = collectionService.getCountByUser(userDTO.getId());
			model.addAttribute("collectionCount", colCount);
			model.addAttribute("manager", true);
			return "user";
		} catch (Exception e) {
			log.error("发生异常",e);
			return "500";
		}

	}
	
	@PutMapping("/{userId}")
	@ResponseBody
	public FrontResult updateUserDesc(@PathVariable Integer userId,String description) {
		User user = new User();
		user.setId(userId);
		user.setDescription(description);
		try {
			userService.updateUser(user);
			UserDTO userDTO = userService.getUserDTO(userId);
			setSessionUser(userDTO);
			return FrontResult.success();
		} catch (Exception e) {
			log.error("发送异常",e);
			return FrontResult.error(e);
		}
		
	}
	
	@GetMapping("/{userId}/comments")
	public String getUserComments(@PathVariable Integer userId,Model model) {
		UserDTO userDTO = userService.getUserDTO(userId);
		if(userDTO==null) {
			return "error/404";
		}
		model.addAttribute("user", userDTO);
		return "user/comments";
	}

	@GetMapping("/edit")
	public String getEditPage() {
		return "editor";
	}
	
	@GetMapping("/edit/{blogId}")
	public String getEditPage(@PathVariable Integer blogId,Model model) {
		Article article;
		try {
			article = blogService.getArticleDetailsById(blogId);
			model.addAttribute("article", article);
		} catch (Exception e) {
			log.error("发送异常",e);
			return "500";
		}
		
		return "editor";
	}

	@GetMapping("/setting")
	public String getSettingPage(Model model) {
		return getUserMsg(model, "setting");
	}

	private String getUserMsg(Model model, String template) {
		UserDTO userDTO = getSessionUser();
		if (userDTO == null) {
			return null;
		}
		try {
			Integer colCount = collectionService.getCountByUser(userDTO.getId());
			model.addAttribute("colCount", colCount);
			model.addAttribute("user", userDTO);
			return template;
		} catch (Exception e) {
			log.error("发生异常",e);
			return "500";
		}
	}

}

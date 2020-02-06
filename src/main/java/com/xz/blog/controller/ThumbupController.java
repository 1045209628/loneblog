package com.xz.blog.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xz.blog.common.WebConstant;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Thumbup;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.ThumbupService;


@RestController
@RequestMapping("/thumbup")
public class ThumbupController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(ThumbupController.class);

	@Autowired
	private ThumbupService thumbupService;
	

	@PostMapping("/save")
	public FrontResult saveThumbup(@RequestParam(value = "userId", required = false) Integer userId,
			Integer articleId) {
		try {
			if (userId == null) {
				UserDTO user = getSessionUser();
				userId = user.getId();
			}
			Thumbup thumbup = new Thumbup();
			thumbup.setUserId(userId);
			thumbup.setArticleId(articleId);
			thumbup.setStatus(1);
			thumbupService.save2Redis(thumbup,false);	
			return FrontResult.success("点赞成功");
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.error(e);
		}
	}

	@PostMapping("/cancel")
	public FrontResult deleteThumbup(@RequestParam(value = "userId", required = false) Integer userId,
			Integer articleId, HttpServletRequest req) {
		try {
			if (userId == null) {
				User user = (User) req.getSession().getAttribute(WebConstant.USER_SESSION_KEY);
				userId = user.getId();
			}
			Thumbup thumbup = new Thumbup();
			thumbup.setUserId(userId);
			thumbup.setArticleId(articleId);
			thumbup.setStatus(0);
			thumbupService.save2Redis(thumbup,false);
			return FrontResult.success("取消点赞");
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

	

	@GetMapping("/{userId}/{articleId}")
	public FrontResult isCollected(@PathVariable Integer userId, @PathVariable Integer articleId) {
		try {
			Boolean hasThumbup = thumbupService.hasLikeFromRedis(userId, articleId);
			if (hasThumbup == null || hasThumbup.booleanValue() == false) {
				return FrontResult.build(FrontResult.FAULT, "用户未点赞", false);
			} else
				return FrontResult.success("用户已点赞");
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

}

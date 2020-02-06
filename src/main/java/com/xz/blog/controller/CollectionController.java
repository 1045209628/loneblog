package com.xz.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Collection;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.CollectionService;

@RestController
@RequestMapping("/collection")
public class CollectionController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(CollectionController.class);

	@Autowired
	private CollectionService collectionService;

	@PostMapping("/save")
	public FrontResult saveCollection(@RequestParam(value = "userId", required = false) Integer userId,
			Integer articleId) {
		try {
			if (userId == null) {
				UserDTO user = getSessionUser();
				userId = user.getId();
			}
			Collection collection = new Collection();
			collection.setUserId(userId);
			collection.setArticleId(articleId);
			collection.setStatus(1);

			collectionService.save2Redis(collection,false);

			return FrontResult.success("收藏成功");

		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

	@PostMapping("/cancel")
	public FrontResult deleteCollection(@RequestParam(value = "userId", required = false) Integer userId,
			Integer articleId) {
		try {
			if (userId == null) {
				UserDTO userDTO = getSessionUser();
				userId = userDTO.getId();
			}
			Collection collection = new Collection();
			collection.setUserId(userId);
			collection.setArticleId(articleId);
			collection.setStatus(0);

			collectionService.save2Redis(collection,false);

			return FrontResult.success("取消收藏");
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}
	}

	@GetMapping("/{userId}/{articleId}")
	public FrontResult isCollected(@PathVariable Integer userId, @PathVariable Integer articleId) {
		try {
			Boolean hasCollection = collectionService.hasLikeFromRedis(articleId, userId);
			if (hasCollection == null || hasCollection.booleanValue() == false) {
				return FrontResult.build(FrontResult.FAULT, "用户未收藏", false);
			} else
				return FrontResult.success("用户已收藏");
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

}

package com.xz.blog.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.xz.blog.common.Status;
import com.xz.blog.common.WebConstant;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Comment;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.BlogService;
import com.xz.blog.service.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentService commentService;

	@Autowired
	private BlogService blogService;

	@PostMapping("/save")
	public FrontResult saveComment(@RequestBody Comment comment) {
		UserDTO user = getSessionUser();
		if (user == null) {
			throw new NullPointerException("未登录");
		} else {
			comment.setUserId(user.getId());
		}
		// 补全
		comment.setState(Status.Public.getCode());
		comment.setCreateTime(new Date());

		try {
			commentService.insertComment(comment);
			return FrontResult.success("评论成功");
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}

	}

	@GetMapping("/blog/{articleId}")
	public FrontResult getBlogComment(@PathVariable Integer articleId,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {
		try {
			PageInfo comments = commentService.getArticleComments(articleId, pageNum);
			return FrontResult.success(comments);
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

	@GetMapping("/{commentId}/replies")
	public FrontResult getReplies(@PathVariable("commentId") Integer rootComment,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {

		try {
			PageInfo commentReplies = commentService.getCommentReplies(rootComment, pageNum,
					WebConstant.REPLY_PAGE_SIZE);
			return FrontResult.success(commentReplies);
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

	@DeleteMapping("/{id}")
	public FrontResult deleteComment(@PathVariable Integer id) {

		try {
			FrontResult result = commentService.deleteCommentById(id);
			return result;
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

	@GetMapping("/count/{articleId}")
	public FrontResult countByArticleId(@PathVariable Integer articleId) {
		try {
			Long comCount = commentService.getCountByArticleId(articleId);
			return FrontResult.success(comCount);
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}

	@GetMapping("/user/{userId}")
	public FrontResult getCommentsByUser(@PathVariable Integer userId,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {
		try {

			PageInfo comments = commentService.getCommentsByUser(userId, pageNum);
			return FrontResult.success(comments);
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.error(e);
		}
	}
	
	

}

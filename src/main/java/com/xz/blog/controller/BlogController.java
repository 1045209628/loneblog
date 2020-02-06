package com.xz.blog.controller;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.javassist.expr.NewArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.xz.blog.common.ExecuteTime;
import com.xz.blog.common.RedisClient;
import com.xz.blog.common.Status;
import com.xz.blog.common.WebConstant;
import com.xz.blog.modal.dto.ArticleDTO;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.Picture;
import com.xz.blog.modal.po.Statistic;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.BlogService;
import com.xz.blog.service.CollectionService;
import com.xz.blog.service.CommentService;
import com.xz.blog.service.PictureService;
import com.xz.blog.service.StatisticService;
import com.xz.blog.service.ThumbupService;
import com.xz.blog.service.UserService;
import com.xz.blog.util.DataTransferUtil;

@Controller
@RequestMapping("/blog")
public class BlogController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private ThumbupService thumbupService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private PictureService pictureService;

	@Autowired
	private RedisClient redisClient;

	@GetMapping("/{blogId}")
	// @ExecuteTime
	public String getBlogDetails(@PathVariable Integer blogId, Model model) {

		try {
			Article article = blogService.getArticleDetailsById(blogId);
			if (article == null) {
				return "error/404";
			}
			// 5秒内防刷新
			PreventFlush(blogId, 5);

			User user = userService.getUser(article.getUserId());
			// Statistic statistic =
			// statisticService.getStatisticByArticleId(article.getId());
			ArticleDTO articleDTO = DataTransferUtil.articleDetailToDTO(article, user, null);
			Integer colCount = collectionService.getCountByArticle(article.getId());
			Integer thmCount = thumbupService.getCountByArticle(article.getId());
			model.addAttribute("collectionCount", colCount);
			model.addAttribute("thumbupCount", thmCount);
			model.addAttribute("article", articleDTO);
			return "article";
		} catch (Exception e) {
			log.error("发生异常", e);
			model.addAttribute("exception", e.toString());
			return "article";
		}

	}

	private void PreventFlush(Integer blogId, int seconds) {
		synchronized (this) {
			Object value = redisClient.get(WebConstant.SESSION_FALSH + "::" + blogId);
			if (value == null) {
				redisClient.set(WebConstant.SESSION_FALSH + "::" + blogId, 1);
				redisClient.expire(WebConstant.SESSION_FALSH + "::" + blogId, seconds);
				blogService.incrBlogClick2Redis(blogId);
			}
		}
	}

	@DeleteMapping("/{blogId}")
	@ResponseBody
	public FrontResult deleteBlog(@PathVariable Integer blogId) {
		Article article = new Article();
		article.setId(blogId);
		article.setState(Status.Delete.getCode());
		try {
			blogService.deleteAricle(article);
			return FrontResult.success();
		} catch (Exception e) {
			log.error("发送异常", e);
			return FrontResult.error(e);
		}
	}

	@PostMapping("/save")
	@ResponseBody
	public FrontResult saveBlog(String title, String content, String summary, Integer wordCount,
			@RequestParam(value = "picIds[]", required = false) String[] pictureIds,
			@RequestParam(value = "blogId", required = false) Integer articleId) {

		Article article = new Article();
		// article.setCreateTime(new Date());
		Date date = new Date();
		article.setUpdateTime(date);
		UserDTO user = getSessionUser();
		if (user == null) {
			throw new NullPointerException();
		}
		article.setUserId(user.getId());
		article.setSummary(summary);
		article.setContent(content);
		article.setTitle(title);

		try {
			Integer id = null;
			Statistic statistic = new Statistic();
			statistic.setWordCount(wordCount);
			if (articleId != null) {
				id = articleId;
				article.setId(id);
				// article.setCreateTime(null);
				// article.setUpdateTime(new Date());
				blogService.updateArticle(article);
				statistic.setId(id);
				statistic.setArticleId(id);
				statisticService.updateStatistic(statistic);
				log.info("更新博客：{} 和统计数据", id);
			} else {
				article.setCreateTime(date);
				blogService.insertArticle(article);
				id = article.getId();
				log.info("新增一条博客：{}", id);
				// 新增一条统计信息
				statistic.setId(id);
				statistic.setArticleId(id);
				statistic.setCreateTime(new Date());
				statistic.setCollectionCount(0);
				statistic.setCommentCount(0);
				statistic.setThumbupCount(0);
				statistic.setViewCount(0);
				statisticService.insertStatistic(statistic);
				log.info("新增一条统计信息：{}", statistic.getId());
			}

			// 更新图片索引
			if (pictureIds != null) {
				for (String picId : pictureIds) {
					Picture pic = new Picture();
					pic.setId(picId);
					pic.setUserCount(1);
					pic.setUseId(id);
					pictureService.updatePicture(pic);
				}
			}

			return FrontResult.success("保存成功");
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}

	}

	@GetMapping("/hot")
	@ResponseBody
	public FrontResult getBlogByHot(
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {
		try {
			FrontResult result = blogService.getArticleByClicks(pageNum, WebConstant.COMMON_PAGE_SIZE);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FrontResult.build(500, e.getMessage());
		}

	}

	@GetMapping("/new")
	@ResponseBody
	// @ExecuteTime
	public FrontResult getBlogByNews(
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {
		try {
			FrontResult result = blogService.getArticleByTime(pageNum, WebConstant.COMMON_PAGE_SIZE);
			return result;
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.build(500, e.getMessage());
		}

	}

	@GetMapping("/user/{userId}")
	@ResponseBody
	public FrontResult getBlogByUser(@PathVariable Integer userId,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {

		try {
			FrontResult articleList = blogService.getArticleByUserId(userId, pageNum, WebConstant.COMMON_PAGE_SIZE);
			return articleList;
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}
	}

	@GetMapping("/collection/{userId}")
	@ResponseBody
	public FrontResult getBlogByCollection(@PathVariable Integer userId,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer pageNum) {
		try {
			PageInfo collections = collectionService.getCollectionsByUserFromRedis(userId, pageNum);
			FrontResult result = blogService.getArticleByCollection(collections);
			return result;
		} catch (Exception e) {
			log.error("发生异常", e);
			return FrontResult.build(FrontResult.ERROR, e.getMessage());
		}
	}

}

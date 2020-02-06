package com.xz.blog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BulkBean;

import com.github.pagehelper.PageInfo;
import com.xz.blog.modal.dto.ArticleDTO;
import com.xz.blog.modal.dto.ArticleItemDTO;
import com.xz.blog.modal.dto.CollectionDTO;
import com.xz.blog.modal.dto.CommentDTO;
import com.xz.blog.modal.dto.GitHubUser;
import com.xz.blog.modal.dto.UserDTO;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.Collection;
import com.xz.blog.modal.po.Comment;
import com.xz.blog.modal.po.Picture;
import com.xz.blog.modal.po.Statistic;
import com.xz.blog.modal.po.User;

public class DataTransferUtil {

	public static final Logger log = LoggerFactory.getLogger(DataTransferUtil.class);

	public static ArticleItemDTO articleToDTO(Article article, User user, Long commentCount, Integer collectionCount) {
		ArticleItemDTO articleItemDTO = new ArticleItemDTO();
		articleItemDTO.setId(article.getId());
		articleItemDTO.setTitle(article.getTitle());
		articleItemDTO.setUserId(user.getId());
		articleItemDTO.setUsername(user.getUsername());
		// articleItemDTO.setContent(article.getContent());
		articleItemDTO.setSummary(article.getSummary());
		articleItemDTO.setClicks(article.getClicks());
		articleItemDTO.setState(article.getState());
		articleItemDTO.setCreateTime(article.getCreateTime());
		articleItemDTO.setUpdateTime(article.getUpdateTime());
		articleItemDTO.setCommentCount(commentCount);
		articleItemDTO.setCollectionCount(collectionCount);
		return articleItemDTO;
	}

	public static ArticleDTO articleDetailToDTO(Article article, User user, Statistic statistic) {
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setId(article.getId());
		articleDTO.setTitle(article.getTitle());
		articleDTO.setUserId(user.getId());
		articleDTO.setUsername(user.getUsername());
		articleDTO.setContent(article.getContent());
		articleDTO.setSummary(article.getSummary());
		articleDTO.setClicks(article.getClicks());
		articleDTO.setState(article.getState());
		articleDTO.setCreateTime(article.getCreateTime());
		articleDTO.setUpdateTime(article.getUpdateTime());
		articleDTO.setStatistic(statistic);
		return articleDTO;
	}

	public static CommentDTO commentToDTO(Comment comment, User user, User replyUser, PageInfo replyInfo) {
		CommentDTO commentToDTO = commentToDTO(comment, user, replyUser, null, replyInfo);
		return commentToDTO;
	}

	public static CommentDTO commentToDTO(Comment comment, User user, User replyUser, Article article,
			PageInfo replyInfo) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment.getId());
		commentDTO.setArticleId(comment.getArticleId());
		commentDTO.setContent(comment.getContent());
		commentDTO.setCreateTime(comment.getCreateTime());
		commentDTO.setState(comment.getState());
		commentDTO.setUserId(user.getId());
		commentDTO.setUsername(user.getUsername());
		commentDTO.setReplyId(comment.getReplyId());
		commentDTO.setBlogName(article == null ? null : article.getTitle());
		if (replyUser != null) {
			commentDTO.setReplyname(replyUser.getUsername());
			commentDTO.setReplyUserId(replyUser.getId());
		}
		commentDTO.setRootComment(comment.getRootComment());
		commentDTO.setReplyInfo(replyInfo);
		return commentDTO;
	}

	public static UserDTO userToDTO(User user, Picture picture) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setAvatarId(user.getAvatarId());
		userDTO.setCreateTime(user.getCreateTime());
		userDTO.setDescription(user.getDescription());
		userDTO.setEmail(user.getEmail());
		userDTO.setType(user.getType());
		userDTO.setActiveCode(user.getActivecode());
		userDTO.setUsername(user.getUsername());
		if (picture != null)
			userDTO.setAvatarUrl(picture.getUrl());
		return userDTO;
	}

	public static User gitHubUserToUser(GitHubUser gitUser) {
		User user = new User();
		user.setId(gitUser.getId());
		user.setUsername(gitUser.getLogin());
		user.setEmail(gitUser.getEmail());
		String aurl = gitUser.getAvatar_url();
		String sha256 = EncryptUtil.encodeSha256(aurl + gitUser.getCreated_at());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = null;
		try {
			date = df.parse(gitUser.getCreated_at());
		} catch (ParseException e) {
			log.error("日期解析错误", e);
		}
		user.setCreateTime(date);
		user.setType(2);
		user.setAvatarId(sha256);
		return user;
	}

}

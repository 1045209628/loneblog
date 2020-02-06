package com.xz.blog.service;

import com.github.pagehelper.PageInfo;
import com.xz.blog.common.ExecuteTime;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.vo.FrontResult;

public interface BlogService {

	void insertArticle(Article article) throws Exception;

	FrontResult getArticleByClicks(int pageNum, int pageSize) throws Exception;

	FrontResult getArticleByTime(int pageNum, int pageSize) throws Exception;

	FrontResult getArticleDetailsByClicks(int pageNum, int pageSize) throws Exception;

	FrontResult getArticleDetailsByTime(int pageNum, int pageSize) throws Exception;

	Article getArticleDetailsById(Integer blogId) throws Exception;

	void incrBlogClickById(Article article) throws Exception;

	FrontResult getArticleByUserId(Integer userId, int pageNum, int pageSize) throws Exception;

	FrontResult getArticleByCollection(PageInfo collections) throws Exception;

	void deleteAricle(Article article) throws Exception;

	void updateArticle(Article article) throws Exception;

	void incrBlogClick2Redis(Integer articleId);

	void transClickRedis2DB() throws Exception;

}
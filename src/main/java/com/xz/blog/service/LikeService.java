package com.xz.blog.service;

public interface LikeService<T> {

	void transDB2Redis();

	void transRedis2DB();

	void decrArticleCountRedis(Integer articleId);

	void incrArticleCountRedis(Integer articleId);

	Boolean hasLikeFromRedis(Integer articleId, Integer userId);

	Boolean hasLike(Integer articleId, Integer userId);

	Integer getCountByArticle(Integer articleId);

	void saveOrUpdate(T like);

	void save2Redis(T like, boolean isBatch);

}
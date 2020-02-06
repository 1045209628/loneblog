package com.xz.blog.service;

import com.github.pagehelper.PageInfo;
import com.xz.blog.modal.po.Collection;

public interface CollectionService extends LikeService<Collection> {

	PageInfo getCollectionsByUser(Integer userId, Integer pageNum);

	Integer getCountByUser(Integer userId);

	PageInfo getCollectionsByUserFromRedis(Integer userId, int pageNum);

	void incrUserCountRedis(Integer userId);

	void decrUserCountRedis(Integer userId);

	

}
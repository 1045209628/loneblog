package com.xz.blog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xz.blog.common.ExecuteTime;
import com.xz.blog.common.RedisClient;
import com.xz.blog.common.RedisConstant;
import com.xz.blog.common.WebConstant;
import com.xz.blog.mapper.CollectionMapper;
import com.xz.blog.modal.dto.CollectionDTO;
import com.xz.blog.modal.po.Collection;
import com.xz.blog.modal.po.CollectionExample;
import com.xz.blog.service.CollectionService;

@Service
public class CollectionServiceImpl implements CollectionService {

	private static final Logger log = LoggerFactory.getLogger(CollectionServiceImpl.class);

	@Autowired
	private CollectionMapper collectionMapper;

	@Autowired
	private RedisClient redisClient;

	@Override
	public void saveOrUpdate(Collection collection) {
		CollectionExample example = new CollectionExample();
		example.or().andArticleIdEqualTo(collection.getArticleId()).andUserIdEqualTo(collection.getUserId());
		List<Collection> list = collectionMapper.selectByExample(example);
		if (list == null || list.isEmpty()) {
			collection.setCreateTime(new Date());
			collectionMapper.insertSelective(collection);
			return;
		}
		collectionMapper.updateByExampleSelective(collection, example);
	}

	// 删 无

	@Override
	//@Cacheable(value = RedisConstant.REDIS_COLLECTION_KEY, key = "#userId")
	public PageInfo getCollectionsByUser(Integer userId, Integer pageNum) {
		CollectionExample example = new CollectionExample();
		example.or().andUserIdEqualTo(userId);
		PageHelper.startPage(pageNum, WebConstant.COMMON_PAGE_SIZE);
		List<Collection> list = collectionMapper.selectByExample(example);
		PageInfo pageInfo = new PageInfo(list);
		return pageInfo;
	}

	@Override
	@Cacheable(value = RedisConstant.REDIS_COLLECTION_COUNT_USER_KEY, key = "#userId")
	public Integer getCountByUser(Integer userId) {
		CollectionExample example = new CollectionExample();
		example.or().andUserIdEqualTo(userId);
		int count = collectionMapper.countByExample(example);
		return count;
	}

	@Override
	@Cacheable(value = RedisConstant.REDIS_COLLECTION_COUNT_BLOG_KEY, key = "#articleId")
	public Integer getCountByArticle(Integer articleId) {
		CollectionExample example = new CollectionExample();
		example.or().andArticleIdEqualTo(articleId);
		int count = collectionMapper.countByExample(example);
		return count;
	}

	@Override
	public Boolean hasLike(Integer articleId, Integer userId) {
		CollectionExample example = new CollectionExample();
		example.or().andArticleIdEqualTo(articleId).andUserIdEqualTo(userId);
		List<Collection> list = collectionMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		else {
			Collection collection = list.get(0);
			if (collection.getStatus() == 1)
				return true;
			else
				return false;
		}
	}

	@Override
	public void save2Redis(Collection collection, boolean isBatch) {
		CollectionDTO dto = CollectionDTO.from(collection);
		redisClient.hset(RedisConstant.REDIS_COLLECTION_KEY, dto.getKey(), dto.getValue());
		if (!isBatch) {
			if (collection.getStatus() == 1) {
				incrArticleCountRedis(collection.getArticleId());
				incrUserCountRedis(collection.getUserId());
			} else {
				decrArticleCountRedis(collection.getArticleId());
				decrUserCountRedis(collection.getUserId());
			}
		}
	}

	@Override
	public Boolean hasLikeFromRedis(Integer articleId, Integer userId) {
		Integer value = (Integer) redisClient.hget(RedisConstant.REDIS_COLLECTION_KEY, articleId + "::" + userId);
		if (value == null) {
			Boolean flag = hasLike(articleId, userId);
			if (flag != null) {
				Collection collection = new Collection();
				collection.setArticleId(articleId);
				collection.setUserId(userId);
				collection.setStatus(flag ? 1 : 0);
				save2Redis(collection, false);
			}
			return flag;
		}
		if (value == 1)
			return true;
		return false;
	}

	@Override
	@ExecuteTime
	public PageInfo getCollectionsByUserFromRedis(Integer userId, int pageNum) {
		Map<Object, Object> map = redisClient.entries(RedisConstant.REDIS_COLLECTION_KEY);
		if(map==null||map.size()<=0) {
			PageInfo info = getCollectionsByUser(userId, pageNum);
			return info;
		}
		Set<Entry<Object, Object>> set = map.entrySet();
		List<Collection> list = new ArrayList<>();
		for (Entry<Object, Object> entry : set) {
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			if (value == 0)
				continue;

			CollectionDTO dto = new CollectionDTO();
			dto.setKey(key);
			dto.setValue(value);
			Collection collection = CollectionDTO.to(dto);
			if (collection.getUserId() != userId)
				continue;

			list.add(collection);
		}

		int total = list.size();
		int pageCount = total / WebConstant.COMMON_PAGE_SIZE;
		pageCount += total % WebConstant.COMMON_PAGE_SIZE == 0 ? 0 : 1;
		List<Collection> page = list.subList((pageNum - 1) * WebConstant.COMMON_PAGE_SIZE,
				pageCount == pageNum ? total : pageNum * WebConstant.COMMON_PAGE_SIZE);
		PageInfo pageInfo = new PageInfo<>(page);
		pageInfo.setTotal(total);
		pageInfo.setPages(pageCount);
		pageInfo.setSize(pageCount);
		pageInfo.setPageSize(WebConstant.COMMON_PAGE_SIZE);
		pageInfo.setPageNum(pageNum);
		return pageInfo;
	}

	@Override
	public void incrUserCountRedis(Integer userId) {
		redisClient.incr(RedisConstant.REDIS_COLLECTION_COUNT_USER_KEY + "::" + userId);
	}

	@Override
	public void decrUserCountRedis(Integer userId) {
		redisClient.decr(RedisConstant.REDIS_COLLECTION_COUNT_USER_KEY + "::" + userId);
	}

	@Override
	public void incrArticleCountRedis(Integer articleId) {
		redisClient.incr(RedisConstant.REDIS_COLLECTION_COUNT_BLOG_KEY + "::" + articleId);
	}

	@Override
	public void decrArticleCountRedis(Integer articleId) {
		redisClient.decr(RedisConstant.REDIS_COLLECTION_COUNT_BLOG_KEY + "::" + articleId);
	}

	/**
	 * 定时任务
	 */
	@Override
	@ExecuteTime
	public void transRedis2DB() {
		Cursor<Entry<Object, Object>> cursor = redisClient.cursor(RedisConstant.REDIS_COLLECTION_KEY);
		List<Collection> collections = new ArrayList<Collection>();
		while (cursor.hasNext()) {
			Map.Entry<Object, Object> entry = cursor.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			CollectionDTO dto = new CollectionDTO();
			dto.setKey(key);
			dto.setValue(value);
			Collection collection = CollectionDTO.to(dto);

			collections.add(collection);

		}

		for (Collection collection : collections) {
			saveOrUpdate(collection);
		}
	}

	/**
	 * springboot启动时的操作
	 */
	@Override
	@ExecuteTime
	public void transDB2Redis() {
		List<Collection> list = collectionMapper.selectByExample(null);
		for (Collection collection : list) {
			save2Redis(collection, true);
			if (collection.getStatus() == 1) {
				incrArticleCountRedis(collection.getArticleId());
				incrUserCountRedis(collection.getUserId());
			}
		}
	}

}

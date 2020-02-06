package com.xz.blog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;

import com.xz.blog.common.ExecuteTime;
import com.xz.blog.common.RedisClient;
import com.xz.blog.common.RedisConstant;
import com.xz.blog.mapper.ThumbupMapper;
import com.xz.blog.modal.dto.ThumbupDTO;
import com.xz.blog.modal.po.Thumbup;
import com.xz.blog.modal.po.ThumbupExample;
import com.xz.blog.service.ThumbupService;

@Service
public class ThumbupServiceImpl implements ThumbupService {

	private static final Logger log = LoggerFactory.getLogger(ThumbupServiceImpl.class);

	@Autowired
	private ThumbupMapper thumbupMapper;

	@Autowired
	private RedisClient redisClient;

	@Override
	@ExecuteTime
	public void transDB2Redis() {
		List<Thumbup> list = thumbupMapper.selectByExample(null);
		for (Thumbup thumbup : list) {
			save2Redis(thumbup, true);
			if (thumbup.getStatus() == 1) {
				incrArticleCountRedis(thumbup.getArticleId());
			}
		}
	}

	@Override
	@ExecuteTime
	public void transRedis2DB() {
		Cursor<Entry<Object, Object>> cursor = redisClient.cursor(RedisConstant.REDIS_THUMBUP_KEY);
		List<Thumbup> thumbups = new ArrayList<Thumbup>();
		while (cursor.hasNext()) {
			Map.Entry<Object, Object> entry = cursor.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			ThumbupDTO dto = new ThumbupDTO();
			dto.setKey(key);
			dto.setValue(value);
			Thumbup thumbup = ThumbupDTO.to(dto);

			thumbups.add(thumbup);

		}

		for (Thumbup thumbup : thumbups) {
			saveOrUpdate(thumbup);
		}

	}

	@Override
	public void decrArticleCountRedis(Integer articleId) {
		redisClient.decr(RedisConstant.REDIS_THUMBUP_COUNT_BLOG_KEY + "::" + articleId);
	}

	@Override
	public void incrArticleCountRedis(Integer articleId) {
		redisClient.incr(RedisConstant.REDIS_THUMBUP_COUNT_BLOG_KEY + "::" + articleId);
	}

	@Override
	@Cacheable(value = RedisConstant.REDIS_THUMBUP_COUNT_BLOG_KEY, key = "#articleId")
	public Integer getCountByArticle(Integer articleId) {
		ThumbupExample example = new ThumbupExample();
		example.or().andArticleIdEqualTo(articleId);
		int count = thumbupMapper.countByExample(example);
		return count;
	}

	@Override
	public void saveOrUpdate(Thumbup thumbup) {
		ThumbupExample example = new ThumbupExample();
		example.or().andArticleIdEqualTo(thumbup.getArticleId()).andUserIdEqualTo(thumbup.getUserId());
		List<Thumbup> list = thumbupMapper.selectByExample(example);
		if (list == null || list.isEmpty()) {
			thumbup.setCreateTime(new Date());
			thumbupMapper.insertSelective(thumbup);
			return;
		}
		thumbupMapper.updateByExampleSelective(thumbup, example);

	}

	@Override
	public void save2Redis(Thumbup thumbup, boolean isBatch) {
		ThumbupDTO dto = ThumbupDTO.from(thumbup);
		redisClient.hset(RedisConstant.REDIS_THUMBUP_KEY, dto.getKey(), dto.getValue());
		if (!isBatch) {
			if (thumbup.getStatus() == 1) {
				incrArticleCountRedis(thumbup.getArticleId());

			} else {
				decrArticleCountRedis(thumbup.getArticleId());

			}
		}
	}

	@Override
	public Boolean hasLikeFromRedis(Integer articleId, Integer userId) {
		Integer value = (Integer) redisClient.hget(RedisConstant.REDIS_THUMBUP_KEY, articleId + "::" + userId);
		if (value == null) {
			Boolean flag = hasLike(articleId, userId);
			if (flag != null) {
				Thumbup thumbup = new Thumbup();
				thumbup.setArticleId(articleId);
				thumbup.setUserId(userId);
				thumbup.setStatus(flag ? 1 : 0);
				save2Redis(thumbup, false);
			}
			return flag;
		}
		if (value == 1)
			return true;
		return false;
	}

	@Override
	public Boolean hasLike(Integer articleId, Integer userId) {
		ThumbupExample example = new ThumbupExample();
		example.or().andArticleIdEqualTo(articleId).andUserIdEqualTo(userId);
		List<Thumbup> list = thumbupMapper.selectByExample(example);
		if (list == null || list.isEmpty())
			return null;
		else {
			Thumbup thumbup = list.get(0);
			if (thumbup.getStatus() == 1)
				return true;
			else
				return false;
		}
	}

}

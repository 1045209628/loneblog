package com.xz.blog.common;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Component
public class RedisClient {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void expire(String key, long time) {
		redisTemplate.expire(key, time, TimeUnit.SECONDS);
	}

	public void hset(String hash, String key, Object value) {
		redisTemplate.opsForHash().put(hash, key, value);
	}

	public Object hget(String hash, String key) {
		return redisTemplate.opsForHash().get(hash, key);
	}

	public void lpush(String key, Object value) {
		redisTemplate.opsForList().leftPush(key, value);
	}

	public void lset(String key, List<Object> list) {
		redisTemplate.opsForList().leftPushAll(key, list);
	}

	public List<Object> lget(String key) {
		return redisTemplate.opsForList().range(key, 0, redisTemplate.opsForList().size(key) - 1);
	}

	public Object lget(String key, Long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	public void incr(String key) {

		redisTemplate.opsForValue().increment(key);

	}

	public void decr(String key) {

		redisTemplate.opsForValue().decrement(key);
	}

	public Cursor<Entry<Object, Object>> cursor(String hash) {
		return redisTemplate.opsForHash().scan(hash, ScanOptions.NONE);
	}

	public Map<Object, Object> entries(String hash) {
		return redisTemplate.opsForHash().entries(hash);
	}
	


}

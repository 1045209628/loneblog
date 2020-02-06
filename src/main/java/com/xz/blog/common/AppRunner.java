package com.xz.blog.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.stereotype.Component;

import com.xz.blog.service.BlogService;
import com.xz.blog.service.CollectionService;
import com.xz.blog.service.ThumbupService;

@Component
public class AppRunner implements ApplicationRunner, DisposableBean {

	private static final Logger log = LoggerFactory.getLogger(AppRunner.class);

	@Autowired
	CollectionService collectionService;

	@Autowired
	ThumbupService thumbupService;

	@Autowired
	BlogService blogService;

	@Autowired
	ApplicationContext context;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 启动时执行方法
		String[] activeProfiles = null;
		if (context != null)
			activeProfiles = context.getEnvironment().getActiveProfiles();
		if (activeProfiles != null && activeProfiles.length > 0) {
			String profile = activeProfiles[0];
			if (profile.equals("prod"))
				return;
		}
		log.info("启动APP...  db ----> redis");
		collectionService.transDB2Redis();
		thumbupService.transDB2Redis();
	}

	@Override
	public void destroy() throws Exception {
		// 关闭时执行方法
		log.info("关闭APP...  redis ----> db");
		collectionService.transRedis2DB();
		thumbupService.transRedis2DB();
		blogService.transClickRedis2DB();
	}

}

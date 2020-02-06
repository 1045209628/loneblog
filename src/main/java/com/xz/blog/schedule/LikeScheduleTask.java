package com.xz.blog.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xz.blog.service.CollectionService;
import com.xz.blog.service.ThumbupService;

public class LikeScheduleTask extends QuartzJobBean {

	private static final Logger log = LoggerFactory.getLogger(LikeScheduleTask.class);

	@Autowired
	private CollectionService collectionService;
	@Autowired
	private ThumbupService thumbupService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("执行定时任务 - 刷写 收藏 点赞 - redis ---------> db");
		collectionService.transRedis2DB();
		thumbupService.transRedis2DB();
	}

}

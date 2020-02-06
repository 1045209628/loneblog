package com.xz.blog.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xz.blog.service.BlogService;

public class BlogClickScheduleTask extends QuartzJobBean {

	private static final Logger log = LoggerFactory.getLogger(BlogClickScheduleTask.class);

	@Autowired
	private BlogService blogService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		log.info("执行定时任务 - 刷写 博客点击量 - redis ---------> db");
		try {
			blogService.transClickRedis2DB();
		} catch (JobExecutionException e) {
			throw e;
		} catch (Exception e) {
			log.error("任务异常", e);
		}

	}

}

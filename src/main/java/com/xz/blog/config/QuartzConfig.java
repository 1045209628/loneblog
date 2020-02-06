package com.xz.blog.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xz.blog.schedule.BlogClickScheduleTask;
import com.xz.blog.schedule.LikeScheduleTask;

@Configuration
public class QuartzConfig {

	private static final String LIKE_TASK = "QUARTZ_LIKE_TASK";
	private static final String BLOG_CLICK_TASK = "QUARTZ_BLOG_CLICK_TASK";

	@Bean
	public JobDetail likeJobDetail() {
		return JobBuilder
				.newJob(LikeScheduleTask.class)
				.withIdentity(LIKE_TASK)
				.storeDurably()
				.build();
	}
	
	@Bean
	public JobDetail clickJobDetail() {
		return JobBuilder
				.newJob(BlogClickScheduleTask.class)
				.withIdentity(BLOG_CLICK_TASK)
				.storeDurably()
				.build();
	}
	
	@Bean
	public Trigger clickQuartzTrigger() {
		//8小时执行一次
		 SimpleScheduleBuilder builder = SimpleScheduleBuilder
				 .simpleSchedule()
				 .withIntervalInHours(4)
				 .repeatForever();
		 
		 return TriggerBuilder
				 .newTrigger()
				 .forJob(likeJobDetail())
				 .withIdentity(BLOG_CLICK_TASK)
				 .withSchedule(builder)
				 .build();
	}

	@Bean
	public Trigger quartzTrigger() {
		//8小时执行一次
		 SimpleScheduleBuilder builder = SimpleScheduleBuilder
				 .simpleSchedule()
				 .withIntervalInHours(8)
				 .repeatForever();
		 
		 return TriggerBuilder
				 .newTrigger()
				 .forJob(clickJobDetail())
				 .withIdentity(LIKE_TASK)
				 .withSchedule(builder)
				 .build();
	}

}

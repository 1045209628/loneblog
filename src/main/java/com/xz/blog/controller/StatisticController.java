package com.xz.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xz.blog.modal.po.Statistic;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.StatisticService;

@RestController
@RequestMapping("/statistic")
public class StatisticController extends SessionController {

	private static final Logger log = LoggerFactory.getLogger(StatisticController.class);

	@Autowired
	private StatisticService statisticService;

	// 增 无须在此处增加 新建博客时同时增加统计数据
	// 删 暂无

	/**
	 * 查
	 * @param pageNum
	 */
//	@GetMapping()
//	public getStatistics(@RequestParam(value = "page",defaultValue = "1",required = false) Integer pageNum) {
//		
//	}
	
	@GetMapping("/{id}")
	public FrontResult getStatisticById(@PathVariable Integer id) {
		try {
			Statistic statistic = statisticService.getStatisticById(id);
			return FrontResult.success(statistic);
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.error(e);
		}
	}
	
	@GetMapping("/blog/{articleId}")
	public FrontResult getStatisticByArticleId(@PathVariable Integer articleId) {
		try {
			Statistic statistic = statisticService.getStatisticByArticleId(articleId);
			return FrontResult.success(statistic);
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.error(e);
		}
	}

	/**
	 * 改 更新数据
	 * 
	 * @param statistic
	 * @return
	 */
	@PutMapping("/{id}")
	public FrontResult updateStatistic(@RequestBody Statistic statistic) {
		try {
			FrontResult result = statisticService.updateStatistic(statistic);
			return result;
		} catch (Exception e) {
			log.error("发生异常",e);
			return FrontResult.error(e);
		}
	}

}

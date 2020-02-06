package com.xz.blog.service;

import com.github.pagehelper.PageInfo;
import com.xz.blog.modal.po.Statistic;
import com.xz.blog.modal.vo.FrontResult;

public interface StatisticService {

	FrontResult insertStatistic(Statistic statistic) throws Exception;

	Statistic getStatisticByArticleId(Integer articleId) throws Exception;

	Statistic getStatisticById(Integer id) throws Exception;

	PageInfo getStatisticByPage(Integer pageNum);

	FrontResult updateStatistic(Statistic statistic) throws Exception;

	/**
	 * 可用方法
	 * 
	 * @param statistic
	 * @return
	 * @throws Exception
	 */
	FrontResult updateStatisticByArticleId(Statistic statistic) throws Exception;

}
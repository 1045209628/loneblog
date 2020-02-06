package com.xz.blog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xz.blog.common.WebConstant;
import com.xz.blog.mapper.StatisticMapper;
import com.xz.blog.modal.po.Statistic;
import com.xz.blog.modal.po.StatisticExample;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.StatisticService;

@Service
public class StatisticServiceImpl implements StatisticService {

	private static final Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);

	@Autowired
	private StatisticMapper statisticMapper;

	@Override
	public FrontResult insertStatistic(Statistic statistic) throws Exception {
		statisticMapper.insertSelective(statistic);
		return FrontResult.success("统计数据插入成功");
	}

	@Override
	public Statistic getStatisticByArticleId(Integer articleId) throws Exception {
		StatisticExample example = new StatisticExample();
		example.or().andArticleIdEqualTo(articleId);
		List<Statistic> statistics = statisticMapper.selectByExample(example);
		if (statistics == null || statistics.isEmpty()) {
			log.error("找不到博客统计信息");
			throw new NullPointerException("找不到博客统计信息");
		}
		Statistic statistic = statistics.get(0);
		return statistic;
	}

	@Override
	public Statistic getStatisticById(Integer id) throws Exception {
		Statistic statistic = statisticMapper.selectByPrimaryKey(id);
		return statistic;
	}

	@Override
	public PageInfo getStatisticByPage(Integer pageNum) {
		PageHelper.startPage(pageNum, WebConstant.COMMON_PAGE_SIZE);
		List<Statistic> statistics = statisticMapper.selectByExample(null);
		PageInfo pageInfo = new PageInfo<>(statistics);
		return pageInfo;
	}

	@Override
	public FrontResult updateStatistic(Statistic statistic) throws Exception {
		statisticMapper.updateByPrimaryKeySelective(statistic);
		return FrontResult.success("统计数据更新成功");
	}

	/**
	 * 可用方法
	 * 
	 * @param statistic
	 * @return
	 * @throws Exception
	 */
	@Override
	public FrontResult updateStatisticByArticleId(Statistic statistic) throws Exception {
		StatisticExample example = new StatisticExample();
		example.or().andArticleIdEqualTo(statistic.getArticleId());
		statisticMapper.updateByExampleSelective(statistic, example);
		return FrontResult.success("统计数据更新成功");
	}

}

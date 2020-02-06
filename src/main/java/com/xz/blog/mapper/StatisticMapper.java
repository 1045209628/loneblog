package com.xz.blog.mapper;

import com.xz.blog.modal.po.Statistic;
import com.xz.blog.modal.po.StatisticExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticMapper {
	long countByExample(StatisticExample example);

	int deleteByExample(StatisticExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(Statistic record);

	int insertSelective(Statistic record);

	List<Statistic> selectByExample(StatisticExample example);

	Statistic selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") Statistic record, @Param("example") StatisticExample example);

	int updateByExample(@Param("record") Statistic record, @Param("example") StatisticExample example);

	int updateByPrimaryKeySelective(Statistic record);

	int updateByPrimaryKey(Statistic record);

	int updateCommentCountPlus(Integer articleId);

	int updateCollectionCountPlus(Integer articleId);

	int updateViewCountPlus(Integer articleId);

	int updateThumbupCountPlus(Integer articleId);

	int updateCommentCountMinus(Integer articleId);

	int updateCollectionCountMinus(Integer articleId);

	int updateViewCountMinus(Integer articleId);

	int updateThumbupCountMinus(Integer articleId);

}
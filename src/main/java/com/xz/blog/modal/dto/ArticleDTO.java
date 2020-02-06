package com.xz.blog.modal.dto;

import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.Statistic;

/**
 * 博客详细信息DTO
 * 
 * @author xz
 *
 */
public class ArticleDTO extends Article {

	private Statistic statistic;
	
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Statistic getStatistic() {
		return statistic;
	}

	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}
	
	
	
}

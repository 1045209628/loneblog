package com.xz.blog.modal.dto;

import com.xz.blog.modal.po.Article;

public class ArticleItemDTO extends Article {

	private String username;
	
	private Long commentCount;
	
	private Integer collectionCount;

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}
	
}

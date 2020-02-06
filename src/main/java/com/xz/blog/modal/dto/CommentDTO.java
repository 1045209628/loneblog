package com.xz.blog.modal.dto;

import com.github.pagehelper.PageInfo;
import com.xz.blog.modal.po.Comment;

public class CommentDTO extends Comment {

	String blogName;
	
	String replyname;

	String username;

	PageInfo replyInfo;
	

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReplyname() {
		return replyname;
	}

	public void setReplyname(String replyname) {
		this.replyname = replyname;
	}

	public PageInfo getReplyInfo() {
		return replyInfo;
	}

	public void setReplyInfo(PageInfo replyInfo) {
		this.replyInfo = replyInfo;
	}

}

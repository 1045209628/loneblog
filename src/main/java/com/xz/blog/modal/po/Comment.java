package com.xz.blog.modal.po;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer userId;

    private Integer articleId;

    private Integer state;

    private Integer replyId;

    private Integer replyUserId;

    private Integer rootComment;

    private Date createTime;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Integer replyUserId) {
        this.replyUserId = replyUserId;
    }

    public Integer getRootComment() {
        return rootComment;
    }

    public void setRootComment(Integer rootComment) {
        this.rootComment = rootComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	@Override
	public String toString() {
		return "Comment [id=" + id + ", userId=" + userId + ", articleId=" + articleId + ", state=" + state
				+ ", replyId=" + replyId + ", replyUserId=" + replyUserId + ", rootComment=" + rootComment
				+ ", createTime=" + createTime + ", content=" + content + "]";
	}
}
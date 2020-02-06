package com.xz.blog.modal.po;

import java.util.Date;

public class Statistic {
    private Integer id;

    private Integer articleId;

    private Integer commentCount;

    private Integer viewCount;

    private Integer CollectionCount;

    private Integer ThumbupCount;

    private Integer WordCount;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCollectionCount() {
        return CollectionCount;
    }

    public void setCollectionCount(Integer CollectionCount) {
        this.CollectionCount = CollectionCount;
    }

    public Integer getThumbupCount() {
        return ThumbupCount;
    }

    public void setThumbupCount(Integer ThumbupCount) {
        this.ThumbupCount = ThumbupCount;
    }

    public Integer getWordCount() {
        return WordCount;
    }

    public void setWordCount(Integer WordCount) {
        this.WordCount = WordCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
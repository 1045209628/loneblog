package com.xz.blog.service;

import com.github.pagehelper.PageInfo;
import com.xz.blog.modal.po.Comment;
import com.xz.blog.modal.vo.FrontResult;

public interface CommentService {

	PageInfo getArticleComments(Integer articleId, int commentPageNum) throws Exception;

	PageInfo getCommentReplies(Integer rootComment, int pageNum, int pageSize) throws Exception;

	void insertComment(Comment comment) throws Exception;

	FrontResult deleteCommentById(Integer cid) throws Exception;

	Comment getCommentById(Integer id);

	Long getCountByArticleId(Integer articleId) throws Exception;

	Long getCountByRootComment(Integer rootComment) throws Exception;

	PageInfo getCommentsByUser(Integer userId, int pageNum);

}
package com.xz.blog.service.impl;

import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xz.blog.common.Status;
import com.xz.blog.common.WebConstant;
import com.xz.blog.mapper.ArticleMapper;
import com.xz.blog.mapper.CommentMapper;
import com.xz.blog.mapper.StatisticMapper;
import com.xz.blog.mapper.UserMapper;
import com.xz.blog.modal.dto.CommentDTO;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.Comment;
import com.xz.blog.modal.po.CommentExample;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.CommentService;
import com.xz.blog.util.DataTransferUtil;

@Service
public class CommentServiceImpl implements CommentService {

	private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private StatisticMapper statisticMapper;

	@Autowired
	private ArticleMapper articleMapper;

	@Override
	public PageInfo getArticleComments(Integer articleId, int commentPageNum) throws Exception {
		CommentExample example = new CommentExample();
		example.or().andArticleIdEqualTo(articleId).andReplyIdIsNull().andStateEqualTo(Status.Public.getCode());
		example.setOrderByClause("create_time desc");
		PageHelper.startPage(commentPageNum, WebConstant.COMMON_PAGE_SIZE);
		List<Comment> comments = commentMapper.selectByExampleWithBLOBs(example);

		// 转换成DTO并设置回复
		List<CommentDTO> commmentList = new ArrayList<>();
		for (Comment comment : comments) {

			PageInfo replyInfo = getCommentReplies(comment.getId(), 1, WebConstant.ORIRIN_REPLY_PAGE_SIZE);

			CommentDTO commentDTO = transferToDTO(comment, replyInfo);
			commmentList.add(commentDTO);
		}

		PageInfo comDTOInfo = new PageInfo<>(comments);
		comDTOInfo.setList(commmentList);
		return comDTOInfo;
	}

	@Override
	public PageInfo getCommentReplies(Integer rootComment, int pageNum, int pageSize) throws Exception {
		CommentExample example = new CommentExample();
		example.or().andRootCommentEqualTo(rootComment).andStateEqualTo(Status.Public.getCode());
		example.setOrderByClause("create_time");
		PageHelper.startPage(pageNum, pageSize);
		List<Comment> replies = commentMapper.selectByExampleWithBLOBs(example);

		List<CommentDTO> replyDTOs = new ArrayList<CommentDTO>();
		for (Comment reply : replies) {
			CommentDTO replyDTO = transferToDTO(reply, null);
			replyDTOs.add(replyDTO);
		}

		PageInfo replyInfo = new PageInfo<>(replies);
		replyInfo.setList(replyDTOs);
		return replyInfo;
	}

	@Override
	public void insertComment(Comment comment) throws Exception {
		commentMapper.insertSelective(comment);
		log.info("新增一条评论：{}", comment.getId());
		statisticMapper.updateCommentCountPlus(comment.getArticleId());
	}

	@Override
	public FrontResult deleteCommentById(Integer cid) throws Exception {
		// 假删除，设置为不可见
		Comment comment = new Comment();
		comment.setId(cid);
		comment.setState(Status.Delete.getCode());
		commentMapper.updateByPrimaryKeySelective(comment);
		return FrontResult.success("删除成功");
	}

	@Override
	public Comment getCommentById(Integer id) {
		Comment comment = commentMapper.selectByPrimaryKey(id);
		return comment;
	}

	@Override
	public Long getCountByArticleId(Integer articleId) throws Exception {
		CommentExample example = new CommentExample();
		example.or().andArticleIdEqualTo(articleId);
		long commentCount = commentMapper.countByExample(example);
		return commentCount;
	}

	@Override
	public Long getCountByRootComment(Integer rootComment) throws Exception {
		CommentExample example = new CommentExample();
		example.or().andRootCommentEqualTo(rootComment);
		long replyCount = commentMapper.countByExample(example);
		return replyCount;
	}

	@Override
	public PageInfo getCommentsByUser(Integer userId, int pageNum) {
		CommentExample example = new CommentExample();
		example.or().andUserIdEqualTo(userId).andStateEqualTo(Status.Public.getCode());
		PageHelper.startPage(pageNum, WebConstant.COMMON_PAGE_SIZE, "create_time desc");
		List<Comment> list = commentMapper.selectByExampleWithBLOBs(example);
		PageInfo pageInfo = new PageInfo(list);
		List<CommentDTO> dtoList = new ArrayList<>(list.size());
		for (Comment comment : list) {
			CommentDTO dto = transferToDTO(comment);
			dtoList.add(dto);
		}
		pageInfo.setList(dtoList);
		return pageInfo;
	}

	private CommentDTO transferToDTO(Comment comment) {
		User user = userMapper.selectByPrimaryKey(comment.getUserId());
		User replyUser = userMapper.selectByPrimaryKey(comment.getReplyUserId());
		Article article = articleMapper.selectByPrimaryKey(comment.getArticleId());
		CommentDTO commentDTO = DataTransferUtil.commentToDTO(comment, user, replyUser, article, null);
		return commentDTO;
	}

	private CommentDTO transferToDTO(Comment comment, PageInfo replyInfo) {
		User user = userMapper.selectByPrimaryKey(comment.getUserId());
		User replyUser = userMapper.selectByPrimaryKey(comment.getReplyUserId());
		CommentDTO commentDTO = DataTransferUtil.commentToDTO(comment, user, replyUser, replyInfo);
		return commentDTO;
	}

}

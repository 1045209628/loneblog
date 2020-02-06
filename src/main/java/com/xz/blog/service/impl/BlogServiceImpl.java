package com.xz.blog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.VoiceStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xz.blog.common.ExecuteTime;
import com.xz.blog.common.RedisClient;
import com.xz.blog.common.RedisConstant;
import com.xz.blog.mapper.ArticleMapper;
import com.xz.blog.mapper.StatisticMapper;
import com.xz.blog.mapper.UserMapper;
import com.xz.blog.modal.dto.ArticleItemDTO;
import com.xz.blog.modal.po.Article;
import com.xz.blog.modal.po.ArticleExample;
import com.xz.blog.modal.po.Collection;
import com.xz.blog.modal.po.User;
import com.xz.blog.modal.vo.FrontResult;
import com.xz.blog.service.BlogService;
import com.xz.blog.service.CollectionService;
import com.xz.blog.service.CommentService;
import com.xz.blog.util.DataTransferUtil;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private StatisticMapper statisticMapper;

	@Autowired
	private RedisClient redisClient;

	@Override
	public void insertArticle(Article article) throws Exception {
		articleMapper.insertSelective(article);
	}

	@Override
	public void deleteAricle(Article article) throws Exception {
		articleMapper.updateByPrimaryKeySelective(article);
	}

	@Override
	// @Transactional
	public void updateArticle(Article article) throws Exception {
		articleMapper.updateByPrimaryKeySelective(article);
		Article article2 = articleMapper.selectByPrimaryKey(article.getId());
		if (article2 != null)
			setArticleDetailsFromRedis(article2);
	}

	@Override
	public FrontResult getArticleByClicks(int pageNum, int pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize, "clicks desc");
		List<Article> articles = articleMapper.selectByExample(null);
		List<Article> list = setClickFromRedis(articles);
		PageInfo<ArticleItemDTO> pageInfo = transferToDTO(list);
		return FrontResult.success(pageInfo);
	}

	@Override
	// @ExecuteTime
	public FrontResult getArticleByTime(int pageNum, int pageSize) throws Exception {

		PageInfo<ArticleItemDTO> info = getArticleByTimeFromRedis(pageNum);
//		if (info != null)
//			return FrontResult.success(info);

		PageHelper.startPage(pageNum, pageSize, "create_time desc");
		List<Article> articles = articleMapper.selectByExample(null);
		List<Article> list = setClickFromRedis(articles);
		PageInfo<ArticleItemDTO> pageInfo = transferToDTO(list);
		// write redis
//		if (pageInfo.getList()!=null)
//			setArticleByTimeFromRedis(pageNum, pageInfo);

		return FrontResult.success(pageInfo);
	}

	private List<Article> setClickFromRedis(List<Article> articles) {
		if (articles == null)
			return null;
		for (Article article : articles) {
			Integer click = (Integer) redisClient.hget(RedisConstant.REDIS_BLOG_CLICK_KEY, article.getId().toString());
			if (click != null)
				article.setClicks(click);
		}
		return articles;
	}

	private PageInfo<ArticleItemDTO> getArticleByTimeFromRedis(int pageNum) {
		PageInfo<ArticleItemDTO> info = (PageInfo<ArticleItemDTO>) redisClient
				.get(RedisConstant.REDIS_BLOG_ITEM_KEY + "::" + pageNum);
		return info;
	}

	private void setArticleByTimeFromRedis(int pageNum, PageInfo<ArticleItemDTO> pageInfo) {
		String key = RedisConstant.REDIS_BLOG_ITEM_KEY + "::" + pageNum;
		redisClient.set(key, pageInfo);
		redisClient.expire(key, 600);// 10分钟
	}

	@Override
	public FrontResult getArticleDetailsByClicks(int pageNum, int pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize, "clicks desc");
		List<Article> articles = articleMapper.selectByExampleWithBLOBs(null);
		return FrontResult.success(transferToDTO(articles));
	}

	@Override
	public FrontResult getArticleDetailsByTime(int pageNum, int pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize, "create_time desc");
		List<Article> articles = articleMapper.selectByExampleWithBLOBs(null);
		return FrontResult.success(transferToDTO(articles));
	}

	@Override
	// @ExecuteTime
	public Article getArticleDetailsById(Integer blogId) throws Exception {

		// 从缓存取
		Article articleRedis = getArticleDetailsFromRedis(blogId);
		if (articleRedis != null) {
			Integer click = getBlogClickFromRedis(articleRedis.getId());
			articleRedis.setClicks(click);
			return articleRedis;
		}

		Article article = articleMapper.selectByPrimaryKey(blogId);
		// 取不到写缓存
		if (article != null) {
			setArticleDetailsFromRedis(article);
			saveBlogClick2Redis(article);
		}

		return article;
	}

	private Article getArticleDetailsFromRedis(Integer blogId) {
		Article article = (Article) redisClient.get(RedisConstant.REDIS_BLOG_KEY + "::" + blogId);
		return article;
	}

	private void setArticleDetailsFromRedis(Article article) {
		String key = RedisConstant.REDIS_BLOG_KEY + "::" + article.getId();
		redisClient.set(key, article);
		redisClient.expire(key, 600);// 10分钟
	}

	@Override
	public void incrBlogClickById(Article article) throws Exception {
		article.setClicks(article.getClicks() + 1);
		articleMapper.updateByPrimaryKeySelective(article);
		statisticMapper.updateViewCountPlus(article.getId());
	}

	private void saveBlogClick2Redis(Article article) {
		redisClient.hset(RedisConstant.REDIS_BLOG_CLICK_KEY, article.getId().toString(), article.getClicks());
	}

	private Integer getBlogClickFromRedis(Integer articleId) {
		Object click = redisClient.hget(RedisConstant.REDIS_BLOG_CLICK_KEY, articleId.toString());
		return (Integer) click;
	}

	@Override
	public void incrBlogClick2Redis(Integer articleId) {
		Integer click = getBlogClickFromRedis(articleId);
		if (click != null) {
			redisClient.hset(RedisConstant.REDIS_BLOG_CLICK_KEY, articleId.toString(), click + 1);
		}
	}

	@Override
	public FrontResult getArticleByUserId(Integer userId, int pageNum, int pageSize) throws Exception {
		ArticleExample articleExample = new ArticleExample();
		articleExample.or().andUserIdEqualTo(userId);
		PageHelper.startPage(pageNum, pageSize, "create_time desc");
		List<Article> articles = articleMapper.selectByExample(articleExample);
		return FrontResult.success(transferToDTO(articles, null));
	}

	@Override
	public FrontResult getArticleByCollection(PageInfo collections) throws Exception {
		List<Collection> cols = collections.getList();
		List<Article> articles = new ArrayList<>();
		for (Collection col : cols) {
			Integer articleId = col.getArticleId();
			Article article = getArticleDetailsById(articleId);
			articles.add(article);
		}
		return FrontResult.success(transferToDTO(articles, collections));
	}

	private PageInfo<ArticleItemDTO> transferToDTO(List<Article> articles) throws Exception {
		return transferToDTO(articles, null);
	}

	private PageInfo<ArticleItemDTO> transferToDTO(List<Article> articles, @Nullable PageInfo preInfo)
			throws Exception {
		List<ArticleItemDTO> articleDTOList = new ArrayList<>();
		for (Article article : articles) {
			int uid = article.getUserId();
			User user = userMapper.selectByPrimaryKey(uid);
			Integer colCount = collectionService.getCountByArticle(article.getId());
			Long comCount = commentService.getCountByArticleId(article.getId());
			ArticleItemDTO articleDTO = DataTransferUtil.articleToDTO(article, user, comCount, colCount);
			articleDTOList.add(articleDTO);
		}

		PageInfo pageInfo = preInfo == null ? new PageInfo(articles) : preInfo;
		pageInfo.setList(articleDTOList);
		return pageInfo;
	}

	@Override
	@ExecuteTime
	public void transClickRedis2DB() throws Exception {
		Map<Object, Object> entries = redisClient.entries(RedisConstant.REDIS_BLOG_CLICK_KEY);
		Set<Object> keySet = entries.keySet();
		ArrayList<Article> list = new ArrayList<Article>(keySet.size());
		for (Object key : keySet) {
			String keyString = (String) key;
			Integer aid = Integer.parseInt(keyString);
			Integer click = (Integer) entries.get(key);
			Article article = new Article();
			article.setId(aid);
			article.setClicks(click);
			list.add(article);
		}

		if (list != null && !list.isEmpty()) {
			for (Article article : list) {
				updateArticle(article);
			}
		}

	}

}

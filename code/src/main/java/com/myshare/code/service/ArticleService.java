package com.myshare.code.service;

import com.myshare.code.entity.Article;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;
import java.util.Map;

public interface ArticleService {

	/**
	 * 根据分页条件查询资源消息列表
	 *
	 * @param s_article
	 * @param nickname
	 * @param s_bpublishDate
	 * @param s_epublishDate
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Article> list(Article s_article, String nickname, String s_bpublishDate, String s_epublishDate,
	                          Integer page, Integer
			                          pageSize, Direction direction, String... properties);

	/**
	 * 根据条件获取总记录数
	 *
	 * @param s_article
	 * @param nickname
	 * @param s_bpublishDate
	 * @param s_epublishDate
	 * @return
	 */
	public Long getcount(Article s_article, String nickname, String s_bpublishDate, String s_epublishDate);

	public void save(Article article);

	public void delete(Integer id);

	public Article getById(Integer id);

	/**
	 * 根据资源类型id查询资源(前台)
	 *
	 * @param type     类型id
	 * @param page     当前页
	 * @param pageSize 每页记录数
	 * @return
	 */
	public Map<String, Object> list(String type, Integer page, Integer pageSize);

	/**
	 * 查询所有审核通过资源信息列表
	 */
	public List<Article> listStatePass();

	/**
	 * 点击数+1
	 *
	 * @param articleId
	 */
	public void updateClick(Integer articleId);

	public Integer todayPubish();
	public Integer todayNoAudit();

	//10条最新资源
	public List<Article> getNewArticle(Integer n);
	//10条热门资源
	public List<Article> getHotArticle(Integer n);
	//10随机资源
	public List<Article> getRandomArticle(Integer n);
}

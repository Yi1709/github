package com.myshare.code.repository;

import com.myshare.code.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

	/**
	 * 点击数+1
	 */
	@Query(value = "update Article set click=click+1 where article_Id= ?1", nativeQuery = true)
	@Modifying
	public void updateClick(Integer articleId);

	@Query(value = "select  count(*) from article where TO_DAYS(publish_date)=TO_DAYS(NOW())", nativeQuery = true)
	public Integer todayPubish();

	@Query(value = "select  count(*) from article where state=1", nativeQuery = true)
	public Integer todayNoAudit();

	@Query(value = "select arc_type_id from Article  where article_id=?1", nativeQuery = true)
	public Integer getArcTypeIdByArticleId(Integer articleId);

	//10条最新资源
	@Query(value = "select * from Article  where state=2 order by publish_date desc  limit ?1", nativeQuery = true)
	public List<Article> getNewArticle(Integer n);
	//10条热门资源
	@Query(value = "select * from Article  where state=2 order by click desc  limit ?1", nativeQuery = true)
	public List<Article> getHotArticle(Integer n);
	//10随机资源
	@Query(value = "select * from Article  where state=2 order by RAND() desc  limit ?1", nativeQuery = true)
	public List<Article> getRandomArticle(Integer n);
}

package com.myshare.code.repository;

import com.myshare.code.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment,Integer> ,JpaSpecificationExecutor<Comment> {

	/**
	 * 删除指定资源下的所有评论
	 */
	@Query(value = "delete  from Comment where article_id=?1",nativeQuery = true)
	@Modifying
	public void deleteByArticleId(Integer articleId);
}

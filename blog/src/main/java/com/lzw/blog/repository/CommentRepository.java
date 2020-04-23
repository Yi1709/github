package com.lzw.blog.repository;

import com.lzw.blog.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/22/15:23
 * @Description:
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

	@Query(value = "delete from  t_comment where blog_id=?1", nativeQuery = true)
	@Modifying
	public void deleteByBlogId(Long blogId);
}

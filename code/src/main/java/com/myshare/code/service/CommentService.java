package com.myshare.code.service;

import com.myshare.code.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface CommentService {
	public void save(Comment comment);

	/**
	 * 根据条件分页查询功能信息
	 */
	public Page<Comment> list(Comment s_comment, Integer page, Integer PageSize, Sort.Direction direction, String...
			propoties);

	/**
	 * 根据条件获取总计卢数
	 */
	public Long getTotal(Comment s_comment);

	public Comment getById(Integer commentId);

	public void delete(Integer id);

	/**
	 * 删除指定资源下的所有评论
	 */
	public void deleteByArticleId(Integer articleId);
}

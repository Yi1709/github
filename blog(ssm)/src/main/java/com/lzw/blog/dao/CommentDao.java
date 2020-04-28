package com.lzw.blog.dao;

import com.lzw.blog.entity.Comment;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/16:51
 * @Description:
 */
public interface CommentDao {

	public Integer add(Comment comment);

	public Integer update(Comment comment);

	public Integer delete(Integer id);

	public List<Comment> list(Map<String, Object> map);

	public Long getTotal(Map<String, Object> map);

	public Integer deleteByBlogId(Integer id);
}

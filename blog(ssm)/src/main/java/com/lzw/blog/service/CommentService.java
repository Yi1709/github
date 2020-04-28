package com.lzw.blog.service;

import com.lzw.blog.entity.Comment;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/17:08
 * @Description:
 */
public interface CommentService {

	public Integer add(Comment comment);

	public Integer update(Comment comment);

	public Integer delete(Integer id);

	public List<Comment> list(Map<String, Object> map);

	public Long getTotal(Map<String, Object> map);

	public Integer deleteByBlogId(Integer id);
}

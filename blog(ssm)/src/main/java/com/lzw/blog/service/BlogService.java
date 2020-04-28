package com.lzw.blog.service;

import com.lzw.blog.entity.Blog;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/23:07
 * @Description:
 */
public interface BlogService {
	public List<Blog> countList();//无参数查询所有博客列表

	public List<Blog> list(Map<String, Object> map);

	public Long getTotal(Map<String, Object> map);

	public Blog findById(Integer id);

	public Integer add(Blog blog);

	public Integer update(Blog blog);

	public Integer delete(Integer id);

	public Integer findByBlogTypeId(Integer id);

	public Blog getLastBlog(Integer id);

	public Blog getNextBlog(Integer id);
}

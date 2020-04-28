package com.lzw.blog.dao;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.Blogger;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/22:27
 * @Description:
 */
public interface BlogDao {

	public List<Blog> countList();//无参数查询所有博客列表,主页展示

	public List<Blog> list(Map<String, Object> map);

	public Long getTotal(Map<String, Object> map);

	public Blog findById(Integer id);

	public Integer add(Blog blog);

	public Integer update(Blog blog);

	public Integer delete(Integer id);

	public Integer findByBlogTypeId(Integer id);

	/**
	 * 上一篇博客
	 */
	public Blog getLastBlog(Integer id);

	/**
	 * 下一篇博客
	 */
	public Blog getNextBlog(Integer id);

}

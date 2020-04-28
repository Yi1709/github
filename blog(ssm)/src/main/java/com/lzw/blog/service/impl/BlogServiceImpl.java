package com.lzw.blog.service.impl;

import com.lzw.blog.dao.BlogDao;
import com.lzw.blog.dao.CommentDao;
import com.lzw.blog.entity.Blog;
import com.lzw.blog.service.BlogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/23:07
 * @Description:
 */
@Service("blogService")
public class BlogServiceImpl implements BlogService {

	@Resource
	private BlogDao blogDao;

	@Resource
	private CommentDao commentDao;

	@Override
	public List<Blog> countList() {
		return this.blogDao.countList();
	}

	@Override
	public List<Blog> list(Map<String, Object> map) {
		return this.blogDao.list(map);
	}

	@Override
	public Long getTotal(Map<String, Object> map) {
		return this.blogDao.getTotal(map);
	}

	@Override
	public Blog findById(Integer id) {
		return this.blogDao.findById(id);
	}

	@Override
	public Integer add(Blog blog) {
		return this.blogDao.add(blog);
	}

	@Override
	public Integer update(Blog blog) {
		return this.blogDao.update(blog);
	}

	@Override
	public Integer delete(Integer id) {
		this.commentDao.deleteByBlogId(id);
		return this.blogDao.delete(id);
	}

	@Override
	public Integer findByBlogTypeId(Integer id) {
		return this.blogDao.findByBlogTypeId(id);
	}

	@Override
	public Blog getLastBlog(Integer id) {
		return this.blogDao.getLastBlog(id);
	}

	@Override
	public Blog getNextBlog(Integer id) {
		return this.blogDao.getNextBlog(id);
	}
}

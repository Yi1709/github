package com.lzw.blog.service.impl;

import com.lzw.blog.dao.BlogTypeDao;
import com.lzw.blog.entity.BlogType;
import com.lzw.blog.service.BlogTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/16:35
 * @Description:
 */
@Service("blogTypeService")
public class BlogTypeServiceImpl implements BlogTypeService {

	@Resource
	private BlogTypeDao blogTypeDao;

	@Override
	public List<BlogType> countList() {
		return blogTypeDao.countList();
	}

	@Override
	public BlogType findById(Integer id) {
		return blogTypeDao.findById(id);
	}

	@Override
	public List<BlogType> list(Map<String, Object> paramMap) {
		return this.blogTypeDao.list(paramMap);
	}

	@Override
	public Long getTotal(Map<String, Object> paramMap) {
		return this.blogTypeDao.getTotal(paramMap);
	}

	@Override
	public Integer add(BlogType blogType) {
		return this.blogTypeDao.add(blogType);
	}

	@Override
	public Integer update(BlogType blogType) {
		return this.blogTypeDao.update(blogType);
	}

	@Override
	public Integer delete(Integer id) {
		return this.blogTypeDao.delete(id);
	}
}

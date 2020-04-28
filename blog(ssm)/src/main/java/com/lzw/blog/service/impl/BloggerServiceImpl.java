package com.lzw.blog.service.impl;

import com.lzw.blog.dao.BloggerDao;
import com.lzw.blog.entity.Blogger;
import com.lzw.blog.service.BloggerService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/15:24
 * @Description:
 */
@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService {

	@Resource
	private BloggerDao bloggerDao;

	@Override
	public Blogger getByUserName(String userName) {
		return bloggerDao.getByUserName(userName);
	}

	@Override
	public Integer update(Blogger blogger) {
		SecurityUtils.getSubject().getSession().setAttribute("currentUser", blogger);
		return this.bloggerDao.update(blogger);
	}

	@Override
	public Blogger find() {
		return this.bloggerDao.find();
	}


}

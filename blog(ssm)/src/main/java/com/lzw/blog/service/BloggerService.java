package com.lzw.blog.service;

import com.lzw.blog.entity.Blogger;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/15:23
 * @Description:
 */
public interface BloggerService {

	public Blogger getByUserName(String userName);

	public Integer update(Blogger blogger);

	public Blogger find();
}

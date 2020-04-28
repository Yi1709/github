package com.lzw.blog.dao;

import com.lzw.blog.entity.Blogger;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/14:51
 * @Description:
 */
public interface BloggerDao {

	public Blogger getByUserName(String userName);

	public Integer update(Blogger blogger);

	public Blogger find();
}

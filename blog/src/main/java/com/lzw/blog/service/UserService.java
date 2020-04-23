package com.lzw.blog.service;

import com.lzw.blog.pojo.User;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/12:43
 * @Description:
 */
public interface UserService {

	User checkUser(String username, String password);
}

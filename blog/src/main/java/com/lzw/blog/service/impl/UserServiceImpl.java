package com.lzw.blog.service.impl;

import com.lzw.blog.pojo.User;
import com.lzw.blog.repository.UserRepository;
import com.lzw.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/12:44
 * @Description:
 */
@Service
@Transactional
public class UserServiceImpl  implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User checkUser(String username, String password) {
		User user = this.userRepository.findByUsernameAndPassword(username, password);
		return user;
	}
}

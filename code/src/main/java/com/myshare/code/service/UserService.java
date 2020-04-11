package com.myshare.code.service;

import com.myshare.code.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

	public User findByUserName(String userName);

	public User findByEmail(String email);

	public void save(User user);

	public User getById(Integer id);

	public Long getCount(User s_user, String s_blatelyLoginDate, String s_elatelyLoginDate);

	public Integer todayRegist();

	public Integer todayLogin();

	public List<User> list(User s_user, String s_blatelyLoginTime, String s_elatelyLoginTime, Integer page, Integer
			pageSize, Sort.Direction direction,String...properties);

}

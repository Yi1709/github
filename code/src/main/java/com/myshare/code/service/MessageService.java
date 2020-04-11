package com.myshare.code.service;

import com.myshare.code.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MessageService {

	public Integer getCountByUserId(Integer userId);

	/**
	 * 看所有消息
	 */
	public void updateState(Integer userId);

	/**
	 * 根据条件分页查询消息列表
	 */
	public Page<Message> list(Integer userId, Integer page, Integer pageSize, Sort.Direction direction,
	                          String... properties);

	/**
	 * 根据条件获取总记录数
	 */
	public Long getCount(Integer userId);

	public void save(Message message);


}

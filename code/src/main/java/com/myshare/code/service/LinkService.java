package com.myshare.code.service;

import com.myshare.code.entity.Link;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LinkService {

	/**
	 * 分页查询友情链接
	 */
	public List<Link> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties);

	/**
	 * 获取总记录数
	 */
	public Long getcount();

	public void save(Link link);

	public void deleteById(Integer id);

	/**
	 * 查询所有友情链接
	 *
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Link> listAll(Sort.Direction direction, String... properties);

	/**
	 * id获取友情链接实体
	 */
	public Link  getById(Integer id);
}

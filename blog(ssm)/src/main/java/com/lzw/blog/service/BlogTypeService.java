package com.lzw.blog.service;

import com.lzw.blog.entity.BlogType;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/16:35
 * @Description:
 */
public interface BlogTypeService {
	public List<BlogType> countList();

	public BlogType findById(Integer id);

	//不固定参数查询
	public List<BlogType> list(Map<String, Object> paramMap);

	public Long getTotal(Map<String, Object> paramMap);

	public Integer add(BlogType blogType);

	public Integer update(BlogType blogType);

	public Integer delete(Integer id);
}

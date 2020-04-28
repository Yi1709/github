package com.lzw.blog.service;

import com.lzw.blog.entity.Link;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/27/11:50
 * @Description:
 */
public interface LinkService {

	public List<Link> list(Map<String, Object> map);

	public Long getTotal(Map<String, Object> map);

	public Link findById(Integer id);

	public Integer add(Link link);

	public Integer update(Link link);

	public Integer delete(Integer id);
}

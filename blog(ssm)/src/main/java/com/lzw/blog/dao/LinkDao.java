package com.lzw.blog.dao;


import com.lzw.blog.entity.Link;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/22:27
 * @Description:
 */
public interface LinkDao {

	public List<Link> list(Map<String, Object> map);

	public Long getTotal(Map<String, Object> map);

	public Link findById(Integer id);

	public Integer add(Link link);

	public Integer update(Link link);

	public Integer delete(Integer id);
	
}

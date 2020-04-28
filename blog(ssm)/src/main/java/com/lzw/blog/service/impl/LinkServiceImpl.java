package com.lzw.blog.service.impl;

import com.lzw.blog.dao.LinkDao;
import com.lzw.blog.entity.Link;
import com.lzw.blog.service.LinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/27/11:52
 * @Description:
 */
@Service("linkService")
public class LinkServiceImpl implements LinkService {

	@Resource
	private LinkDao linkDao;

	@Override
	public List<Link> list(Map<String, Object> map) {
		return this.linkDao.list(map);
	}

	@Override
	public Long getTotal(Map<String, Object> map) {
		return this.linkDao.getTotal(map);
	}

	@Override
	public Link findById(Integer id) {
		return this.linkDao.findById(id);
	}

	@Override
	public Integer add(Link link) {
		return this.linkDao.add(link);
	}

	@Override
	public Integer update(Link link) {
		return this.linkDao.update(link);
	}

	@Override
	public Integer delete(Integer id) {
		return this.linkDao.delete(id);
	}
}

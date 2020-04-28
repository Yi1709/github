package com.lzw.blog.service.impl;

import com.lzw.blog.dao.CommentDao;
import com.lzw.blog.entity.Comment;
import com.lzw.blog.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/17:08
 * @Description:
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Resource
	private CommentDao commentDao;

	@Override
	public Integer add(Comment comment) {
		return this.commentDao.add(comment);
	}

	@Override
	public Integer update(Comment comment) {
		return this.commentDao.update(comment);
	}

	@Override
	public Integer delete(Integer id) {
		return this.commentDao.delete(id);
	}

	@Override
	public List<Comment> list(Map<String, Object> map) {
		return this.commentDao.list(map);
	}

	@Override
	public Long getTotal(Map<String, Object> map) {
		return this.commentDao.getTotal(map);
	}

	@Override
	public Integer deleteByBlogId(Integer id) {
		return this.commentDao.deleteByBlogId(id);
	}
}

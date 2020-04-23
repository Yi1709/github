package com.lzw.blog.service;

import com.lzw.blog.pojo.Comment;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/22/15:19
 * @Description:
 */
public interface CommentService {

	List<Comment> listCommentByBlogId(Long blogId);

	Comment saveComment(Comment comment);

	void  deleteByBlogId(Long id);
}

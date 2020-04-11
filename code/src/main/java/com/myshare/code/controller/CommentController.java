package com.myshare.code.controller;


import com.myshare.code.entity.Comment;
import com.myshare.code.entity.User;
import com.myshare.code.service.CommentService;
import com.myshare.code.util.Consts;
import com.myshare.code.util.HTMLUtil;
import com.myshare.code.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	/**
	 * 前端提交保存评论信息
	 *
	 * @param comment
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public Map<String, Object> add(Comment comment, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		comment.setContent(StringUtil.esc(comment.getContent()));
		comment.setCommentDate(new Date());
		comment.setState(0);
		comment.setUser((User) session.getAttribute(Consts.CURRENT_USER));
		commentService.save(comment);
		resultMap.put("success", true);
		return resultMap;
	}

	/**
	 * 分页查询某个资源的评论信息
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(Comment s_comment, @RequestParam(value = "page", required = false) Integer page) {
		s_comment.setState(1);
		Page<Comment> commentPage = this.commentService.list(s_comment, page, 5, Sort.Direction.DESC, "commentDate");
		Map<String, Object> map = new HashMap<>();
		map.put("data", HTMLUtil.commentPageStr(commentPage.getContent()));
		map.put("total", commentPage.getTotalPages());
		return map;


	}
}

package com.myshare.code.controller.admin;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.myshare.code.entity.Comment;
import com.myshare.code.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin/comment")
public class CommentAdminController {

	@Autowired
	private CommentService commentService;

	@RequestMapping(value = "/list")
	@RequiresPermissions(value = "分页查询评论信息")
	public Map<String, Object> list(Comment s_comment, @RequestParam(value = "page", required = false) Integer page,
	                                @RequestParam(value = "pageSize", required = false) Integer
			pageSize) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", commentService.list(s_comment, page, pageSize, Sort.Direction.DESC, "commentDate").getContent
				());
		map.put("total", commentService.getTotal(s_comment));
		map.put("errorNo", 0);
		return map;
	}


	/**
	 * 修改评论转台
	 */
	@RequestMapping(value = "/updateState")
	@RequiresPermissions(value = "修改评论状态")
	public Map<String, Object> updateState(Integer commentId, boolean state) {
		Map<String, Object> resultmap = new HashMap<>();
		Comment comment = this.commentService.getById(commentId);
		if (state == true) {
			comment.setState(1);
		} else {
			comment.setState(2);
		}
		this.commentService.save(comment);
		resultmap.put("success", true);
		return resultmap;
	}

	/**
	 * 删除评论转台
	 */
	@RequestMapping(value = "/delete")
	@RequiresPermissions(value = "删除评论")
	public Map<String, Object> delete(@RequestParam(value = "commentId") String ids) {
		Map<String, Object> resultmap = new HashMap<>();
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {

			this.commentService.delete(Integer.parseInt(idsStr[i]));
		}
		resultmap.put("errorNo", 0);
		return resultmap;

	}
}

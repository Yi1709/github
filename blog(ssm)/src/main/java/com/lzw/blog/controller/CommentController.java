package com.lzw.blog.controller;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.Comment;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.CommentService;
import com.lzw.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Auther: lzw
 * @Date: 2020/04/27/22:36
 * @Description:
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

	@Resource
	private CommentService commentService;

	@Resource
	private BlogService blogService;

	@RequestMapping("/save")
	public String save(Comment comment, @RequestParam("imageCode") String imageCode, HttpServletRequest request,
	                   HttpServletResponse response, HttpSession session) throws Exception {
		String sRand = (String) session.getAttribute("sRand");
		int resultTotal = 0;
		JSONObject result = new JSONObject();
		if (!imageCode.equals(sRand)) {
			result.put("success", false);
			result.put("errorInfo", "验证码不正确，请重新输入!");
		} else {
			String userIp = request.getRemoteAddr();
			comment.setUserIp(userIp);
			if (comment.getId() == null) {
				resultTotal = this.commentService.add(comment);
				Blog blog = this.blogService.findById(comment.getBlog().getId());
				blog.setReplyHit(blog.getReplyHit() + 1);
				this.blogService.update(blog);
			}
			if (resultTotal > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("errorInfo", "评论失败，请稍后再试!");
			}
		}


		ResponseUtil.write(response, result);
		return null;
	}
}

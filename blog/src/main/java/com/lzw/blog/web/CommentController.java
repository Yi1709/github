package com.lzw.blog.web;

import com.lzw.blog.pojo.Blog;
import com.lzw.blog.pojo.Comment;
import com.lzw.blog.pojo.User;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @Auther: lzw
 * @Date: 2020/04/22/15:13
 * @Description:
 */
@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private BlogService blogService;

	@Value("${comment.avatar}")
	private String avatar;

	@GetMapping("/comments/{blogId}")
	public String comments(@PathVariable Long blogId, Model model) {
		model.addAttribute("comments", this.commentService.listCommentByBlogId(blogId));
		return "blog::commentList";
	}

	@PostMapping("/comments")
	public String post(Comment comment, HttpSession session) {
		Blog blog = this.blogService.getBlog(comment.getBlog().getId());
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser!=null){
			comment.setAvatar(currentUser.getAvatar());
			comment.setAdminComment(true);
		}else {
			comment.setAvatar(avatar);
		}
		comment.setBlog(blog);
		this.commentService.saveComment(comment);
		return "redirect:/comments/" + comment.getBlog().getId();
	}

}

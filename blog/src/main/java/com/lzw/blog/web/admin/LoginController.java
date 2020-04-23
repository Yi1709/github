package com.lzw.blog.web.admin;

import com.lzw.blog.pojo.User;
import com.lzw.blog.service.UserService;
import com.lzw.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/12:49
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String loginPage() {
		return "admin/login";
	}

	@RequestMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
	                    RedirectAttributes redirectAttributes) {
		User user = this.userService.checkUser(username, MD5Util.md5(password));
		if (user != null) {
			user.setPassword(null);
			session.setAttribute("currentUser", user);
			return "admin/index";
		} else {
			redirectAttributes.addFlashAttribute("message", "用户名和密码错误!");
			return "redirect:/admin";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("currentUser");
		return "redirect:/admin";
	}
}

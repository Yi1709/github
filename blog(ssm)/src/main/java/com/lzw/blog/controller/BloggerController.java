package com.lzw.blog.controller;

import com.lzw.blog.entity.Blogger;
import com.lzw.blog.service.BloggerService;
import com.lzw.blog.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/14:31
 * @Description:
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {

	@Resource
	private BloggerService bloggerService;

	@RequestMapping("/login")
	public String login(Blogger blogger, HttpServletRequest request) {
		String userName = blogger.getUserName();
		String password = CryptographyUtil.md5(blogger.getPassword(), CryptographyUtil.salt);
		/*从shiro获得subject*/
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		try {
			subject.login(token);
			return "redirect:/admin/main.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("blogger", blogger);
			request.setAttribute("errorInfo", "用户名或密码错误!");
		}

		return "login";
	}

	/**
	 * 关于博主
	 */
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("blogger", this.bloggerService.find());
		modelAndView.addObject("mainPage", "foreground/blogger/info.jsp");
		modelAndView.addObject("pageTitle", "关于博主");
		modelAndView.setViewName("index");
		return modelAndView;
	}
}

package com.lzw.blog.controller.admin;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.BlogType;
import com.lzw.blog.entity.Blogger;
import com.lzw.blog.entity.Link;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.BlogTypeService;
import com.lzw.blog.service.BloggerService;
import com.lzw.blog.service.LinkService;
import com.lzw.blog.util.Consts;
import com.lzw.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/15:43
 * @Description:
 */

@Controller
@RequestMapping("/admin/system")
public class SystemAdminController {

	@Resource
	private BlogTypeService blogTypeService;

	@Resource
	private BloggerService bloggerService;

	@Resource
	private BlogService blogService;

	@Resource
	private LinkService linkService;

	/**
	 * 刷新系统缓存
	 */
	@RequestMapping("/refreshSystem")
	public String refreshSystem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ServletContext application = RequestContextUtils.findWebApplicationContext(request).getServletContext();

		List<BlogType> blogTypes = this.blogTypeService.countList();
		application.setAttribute(Consts.BLOG_TYPE_COUNT_LIST, blogTypes);
		Blogger blogger = this.bloggerService.find();
		application.setAttribute(Consts.BLOGGER, blogger);
		List<Blog> blogs = this.blogService.countList();
		application.setAttribute(Consts.BLOG_COUNT_LIST,blogs);
		List<Link> list = this.linkService.list(null);
		application.setAttribute(Consts.LINK_LIST,list);
		
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}

package com.lzw.blog.controller;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.Comment;
import com.lzw.blog.lucene.BlogIndex;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.CommentService;
import com.lzw.blog.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.MD5;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @Auther: lzw
 * @Date: 2020/04/27/18:55
 * @Description:
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

	@Resource
	private BlogService blogService;

	@Resource
	private CommentService commentService;

	private BlogIndex blogIndex = new BlogIndex();

	@RequestMapping("/articles/{id}")
	public ModelAndView detail(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		Blog blog = this.blogService.findById(id);
		modelAndView.addObject("blog", blog);
		blog.setClickHit(blog.getClickHit() + 1);
		this.blogService.update(blog);
		modelAndView.addObject("mainPage", "foreground/blog/view.jsp");
		modelAndView.addObject("pageTitle", blog.getTitle() + "_个人博客系统");
		//上一篇下一篇
		modelAndView.addObject("pageCode", genUpAndDownPageCode(blogService.getLastBlog(id),
				blogService.getNextBlog(id), request.getServletContext().getContextPath()));

		//查询评论
		Map<String, Object> map = new HashMap<>();
		map.put("id", blog.getId());
		map.put("state", 1);
		modelAndView.addObject("commentList", this.commentService.list(map));

		//获取关键字
		String keyWord = blog.getKeyWord();
		if (StringUtils.isBlank(keyWord)) {
			modelAndView.addObject("keyWords", null);
		} else {
			String[] arr = keyWord.split(" ");
			modelAndView.addObject("keyWords", StringUtil.filterWhite(Arrays.asList(keyWord)));
		}

		modelAndView.setViewName("index");
		return modelAndView;
	}

	/**
	 * 上一篇下一篇
	 */
	private String genUpAndDownPageCode(Blog lastBlog, Blog nextBlog, String projectContext) {
		StringBuffer pageCode = new StringBuffer();
		if (lastBlog == null || lastBlog.getId() == null) {
			pageCode.append("<p>上一篇,没有了</p>");
		} else {
			pageCode.append("<p>上一篇:<a href='" + projectContext + "/blog/articles/" + lastBlog.getId() + ".html'>" +
					lastBlog.getTitle() + "</a></p>");
		}
		if (nextBlog == null || nextBlog.getId() == null) {
			pageCode.append("<p>下一篇,没有了</p>");
		} else {
			pageCode.append("<p>下一篇:<a href='" + projectContext + "/blog/articles/" + nextBlog.getId() + ".html'>" +
					nextBlog.getTitle() + "</a></p>");
		}

		return pageCode.toString();
	}

	@RequestMapping("/q")
	public ModelAndView q(@RequestParam(name = "q", required = false) String q, @RequestParam(name = "page", required =
			false) String page, HttpServletRequest request) throws Exception {
		if (StringUtils.isBlank(page)) {
			page = "1";
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mainPage", "foreground/blog/result.jsp");
		//从lucene查询
		List<Blog> blogs = this.blogIndex.searchBlog(q.trim());
		modelAndView.addObject("resultTotal", blogs.size());
		modelAndView.addObject("q", q);
		int toIndex = 0;
		if (blogs.size() >= Integer.parseInt(page) * 10) {
			toIndex = Integer.parseInt(page) * 10;
		} else {
			toIndex = blogs.size();
		}
		modelAndView.addObject("blogList", blogs.subList((Integer.parseInt(page) - 1) * 10, toIndex));
		modelAndView.addObject("pageTitle", "搜索关键字" + q + "结果页面");
		modelAndView.addObject("pageCode", this.genUpAndDownPage(Integer.parseInt(page), blogs.size(), q, 10,
				request.getServletContext().getContextPath()));
		modelAndView.setViewName("index");
		return modelAndView;

	}

	//翻页功能
	private String genUpAndDownPage(int page, int totalNum, String q, int pageSize, String projectContext) {
		StringBuffer pageCode = new StringBuffer();
		//总页数
		int totalPage = totalNum % pageSize == 0 ? (totalNum / pageSize) : (totalNum / pageSize) + 1;
		if (totalPage == 0) {
			return "";
		}
		pageCode.append("<nav>");
		pageCode.append("<ur class='pager'>");
		//当前页大于1
		if (page > 1) {
			pageCode.append("<li><a href='" + projectContext + "/blog/q.html?page=" + (page - 1) + "&q=" + q +
					"'>上一页</a></li>");
		} else {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}
		if (page <totalPage) {
			pageCode.append("<li><a href='" + projectContext + "/blog/q.html?page=" + (page + 1) + "&q=" + q +
					"'>下一页</a></li>");
		} else {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		}
		pageCode.append("</ur>");
		pageCode.append("</nav>");
		return pageCode.toString();
	}
}

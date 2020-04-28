package com.lzw.blog.controller;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.PageBean;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.util.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/27/14:54
 * @Description:
 */
@Controller
public class IndexController {


	@Resource
	private BlogService blogService;

	@RequestMapping("/index")
	public ModelAndView index(@RequestParam(value = "page", required = false) String page,
	                          @RequestParam(value = "typeId", required = false) String typeId,
	                          @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr,
	                          HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("title", "个人博客系统");

		if (StringUtils.isBlank(page)) {
			page = "1";
		}

		PageBean pageBean = new PageBean(Integer.parseInt(page), 10);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("start", pageBean.getStart());
		resultMap.put("size", pageBean.getPageSize());
		resultMap.put("typeId", typeId);
		resultMap.put("releaseDateStr", releaseDateStr);
		List<Blog> blogs = this.blogService.list(resultMap);

		StringBuffer param = new StringBuffer();
		if(StringUtils.isNotBlank(typeId)){
			param.append("typeId="+typeId+"&");
		}
		if(StringUtils.isNotBlank(releaseDateStr)){
			param.append("releaseDateStr="+releaseDateStr+"&");
		}
		modelAndView.addObject("mainPage", "foreground/blog/list.jsp");
		String pageCode = PageUtil.getPagination(request.getContextPath() + "/index.html",
				blogService.getTotal(resultMap),
				Integer.parseInt(page), 10, param.toString());
		modelAndView.addObject("pageCode", pageCode);
		modelAndView.addObject("blogList", blogs);
		return modelAndView;
	}
}

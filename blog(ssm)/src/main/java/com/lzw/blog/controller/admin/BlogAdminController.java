package com.lzw.blog.controller.admin;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.PageBean;
import com.lzw.blog.lucene.BlogIndex;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.util.DateJsonValueProcessor;
import com.lzw.blog.util.ResponseUtil;
import com.lzw.blog.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: lzw
 * @Date: 2020/04/25/12:01
 * @Description:
 */
@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {

	@Resource
	private BlogService blogService;

	private BlogIndex blogIndex = new BlogIndex();


	@RequestMapping("/save")
	private String save(Blog blog, HttpServletResponse response) throws Exception {
		Integer resultTotal = 0;
		if (blog.getId() == null) {
			blog.setClickHit(50 + new Random().nextInt(267));
			blog.setReplyHit(0);
			resultTotal = this.blogService.add(blog);
			this.blogIndex.addIndex(blog);
		} else {
			resultTotal = this.blogService.update(blog);
			this.blogIndex.updateIndex(blog);
		}
		JSONObject result = new JSONObject();
		if (resultTotal.intValue() > 0) {
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows",
			required = false) String rows, Blog blog, HttpServletResponse response) throws Exception {
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Map<String, Object> map = new HashMap<>();
		map.put("start", pageBean.getStart());
		map.put("pageSize", pageBean.getPageSize());
		map.put("title", StringUtil.formatLike(blog.getTitle()));
		//分页查询博客列表
		List<Blog> blogs = this.blogService.list(map);
		//获取共有多少条信息
		Long total = this.blogService.getTotal(map);
		JSONObject result = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		JSONArray jsonArray = JSONArray.fromObject(blogs, config);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/findById")
	public String findById(@RequestParam(value = "id") String id, HttpServletResponse response) throws Exception {
		Blog blog = this.blogService.findById(Integer.parseInt(id));
		JSONObject result = JSONObject.fromObject(blog);
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
		String idsStr[] = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			this.blogService.delete(Integer.parseInt(idsStr[i]));
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}


}

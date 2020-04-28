package com.lzw.blog.controller.admin;

import com.lzw.blog.entity.BlogType;
import com.lzw.blog.entity.PageBean;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.BlogTypeService;
import com.lzw.blog.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/16:39
 * @Description:
 */
@Controller
@RequestMapping("/admin/blogType")
public class BlogTypeAdminController {

	@Resource
	private BlogTypeService blogTypeService;

	@Resource
	private BlogService blogService;

	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows",
			required = false) String rows, HttpServletResponse response) throws Exception {
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Map<String, Object> map = new HashMap<>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<BlogType> blogTypeList = this.blogTypeService.list(map);
		Long total = this.blogTypeService.getTotal(map);
		//将数据写入response
		JSONObject result = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(blogTypeList);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return "admin/blogTypeManage";
	}

	@RequestMapping({"/save"})
	public String save(BlogType blogType, HttpServletResponse response) throws Exception {

		int resultTotal = 0;
		if (blogType.getId() == null) {
			resultTotal = this.blogTypeService.add(blogType).intValue();
		} else {
			resultTotal = this.blogTypeService.update(blogType);
		}

		JSONObject result = new JSONObject();
		if (resultTotal > 0) {
			result.put("success", Boolean.valueOf(true));
		} else {
			result.put("success", Boolean.valueOf(false));
		}
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
		String[] idsStr = ids.split(",");
		JSONObject result = new JSONObject();
		for (int i = 0; i < idsStr.length; i++) {
			int count = this.blogService.findByBlogTypeId(Integer.parseInt(idsStr[i]));
			if (count > 0) {
				result.put("exist", "该类别下有博客，请先删除博客再试吧!");
			} else {
				this.blogTypeService.delete(Integer.parseInt(idsStr[i]));
			}
		}
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}

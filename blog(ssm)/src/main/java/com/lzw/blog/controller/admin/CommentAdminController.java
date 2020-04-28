package com.lzw.blog.controller.admin;

import com.lzw.blog.entity.Comment;
import com.lzw.blog.entity.PageBean;
import com.lzw.blog.service.CommentService;
import com.lzw.blog.util.DateJsonValueProcessor;
import com.lzw.blog.util.ResponseUtil;
import com.mysql.cj.xdevapi.JsonArray;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/18:40
 * @Description:
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {

	@Resource
	private CommentService commentService;

	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page,
	                   @RequestParam(value = "rows", required = false) String rows,
	                   @RequestParam(value = "state", required = false) String state,
	                   HttpServletResponse response) throws Exception {
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Map<String, Object> map = new HashMap<>();
		map.put("start", pageBean.getStart());
		map.put("pageSize", pageBean.getPageSize());
		map.put("state", state);
		List<Comment> comments = this.commentService.list(map);
		Long total = this.commentService.getTotal(map);
		JSONObject result = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		JSONArray jsonArray = JSONArray.fromObject(comments, jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			this.commentService.delete(Integer.parseInt(idsStr[i]));
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/review")
	public String review(@RequestParam(value = "ids") String ids, @RequestParam(value = "state") String state,
	                     HttpServletResponse response) throws Exception {
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			Comment comment = new Comment();
			comment.setId(Integer.parseInt(idsStr[i]));
			comment.setState(Integer.parseInt(state));
			this.commentService.update(comment);
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}

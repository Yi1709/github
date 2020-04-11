package com.myshare.code.controller;

import com.myshare.code.service.ArcTypeService;
import com.myshare.code.service.ArticleService;
import com.myshare.code.util.Consts;
import com.myshare.code.util.HTMLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
	/**
	 * 首页
	 */

	@Autowired
	private ArcTypeService arcTypeService;

	@Autowired
	private ArticleService articleService;

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		//类型的html代码
		List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
		modelAndView.addObject("arcTypeStr", HTMLUtil.getArcTypeStr("all", arcTypeList));

		//资源列表
		Map<String, Object> map = this.articleService.list("all", 1, Consts.PAGE_SIZE);
		modelAndView.addObject("articleList", map.get("data"));

		//分页html代码
		modelAndView.addObject("pageStr", HTMLUtil.getPagetion("/article/all", Integer.parseInt(String.valueOf(map.get("count"))), 1,
				"已经到底啦！"));


		return modelAndView;
	}
	/**
	 * 购买vip
	 */
	@RequestMapping("/buyVIP")
	public String  buyVIP(){
		return  "buyVIP";
	}
	/**
	 * 充值积分
	 */
	@RequestMapping("/fbzyzjf")
	public String  fbzyzjf(){
		return  "fbzyzjf";
	}
}

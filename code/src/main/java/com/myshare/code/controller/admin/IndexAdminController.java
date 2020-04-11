package com.myshare.code.controller.admin;

import com.myshare.code.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理员首页或者跳转url控制器
 */
@Controller
public class IndexAdminController {

	@Autowired
	private UserService userService;

	/**
	 * 跳转至管理员主页
	 */
	@RequiresPermissions(value = "进入管理员主页")
	@RequestMapping("/toAdminUserCenterPage")
	public String toAdminUserCenterPage() {
		return "/admin/index";

	}

	/**
	 * 跳转至管理员主页
	 */
	@RequiresPermissions(value = "进入管理员主页")
	@RequestMapping("/defaultIndex")
	public ModelAndView defaultIndex() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("userNum", userService.getCount(null, null, null));
		modelAndView.addObject("todayRegisterNum", userService.todayRegist());
		modelAndView.addObject("todayLoginNum", userService.todayLogin());
		modelAndView.addObject("todayPublishNum", userService.todayLogin());
		modelAndView.addObject("noAuditNum", userService.todayLogin());
		modelAndView.setViewName("/admin/default");
		return modelAndView;

	}
}

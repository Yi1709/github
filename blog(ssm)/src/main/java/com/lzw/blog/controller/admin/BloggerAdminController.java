package com.lzw.blog.controller.admin;

import com.lzw.blog.entity.Blogger;
import com.lzw.blog.service.BloggerService;
import com.lzw.blog.util.CryptographyUtil;
import com.lzw.blog.util.DateUtil;
import com.lzw.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/22:23
 * @Description:
 */
@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {

	@Resource
	private BloggerService bloggerService;


	@RequestMapping("/save")
	public String save(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
	                   Blogger blogger,
	                   HttpServletRequest req,
	                   HttpServletResponse res) throws Exception {
		if (!imageFile.isEmpty()) {
			String filePath = req.getServletContext().getRealPath("/");
			String imageName = DateUtil.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];
			imageFile.transferTo(new File(filePath + "static/userImages/" + imageName));
			blogger.setImageName(imageName);
		}
		// TODO: 2020/4/26 //修改个人信息
		Integer resultTotal = this.bloggerService.update(blogger);
		StringBuffer result = new StringBuffer();
		if (resultTotal.intValue() > 0) {
			result.append("<script language='javascript'>alert('修改成功')</script>");
		} else {
			result.append("<script language='javascript'>alert('修改失败')</script>");
		}
		ResponseUtil.write(res, result);
		return null;
	}

	@RequestMapping("/find")
	public String find(HttpServletResponse response) throws Exception {
		Blogger blogger = (Blogger) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
		JSONObject result = JSONObject.fromObject(blogger);
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/updatePassword")
	public String updatePassword(@RequestParam(name = "id") String id,
	                             @RequestParam(value = "newPassword") String newPassword,
	                             HttpServletResponse response) throws Exception {

		Blogger blogger = new Blogger();
		blogger.setId(Integer.parseInt(id));
		blogger.setPassword(CryptographyUtil.md5(newPassword, CryptographyUtil.salt));
		Integer resultToral = this.bloggerService.update(blogger);
		JSONObject result = new JSONObject();
		if (resultToral.intValue() > 0) {
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}

	@RequestMapping("/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";
	}

}

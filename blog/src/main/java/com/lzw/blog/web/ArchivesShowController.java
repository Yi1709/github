package com.lzw.blog.web;

import com.lzw.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: lzw
 * @Date: 2020/04/23/11:12
 * @Description:
 */
@Controller
public class ArchivesShowController {

	@Autowired
	private BlogService blogService;

	@GetMapping("/archives")
	public String archives(Model model) {

		model.addAttribute("archivesMap", this.blogService.archiveBlogs());
		model.addAttribute("blogCount", this.blogService.countBlog());
		return "archives";
	}
}

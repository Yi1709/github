package com.lzw.blog.web;

import com.lzw.blog.pojo.Tag;
import com.lzw.blog.pojo.Type;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.TagService;
import com.lzw.blog.service.TypeService;
import com.lzw.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/22/21:29
 * @Description:
 */
@Controller
public class TagShowController {

	@Autowired
	private TagService tagService;

	@Autowired
	private BlogService blogService;

	@GetMapping("/tags/{id}")
	public String tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
	                   @PathVariable Long id, Model model) {
		List<Tag> tags = tagService.listTagTop(10000);
		if (id == -1) {
			id = tags.get(0).getId();
		}
		model.addAttribute("tags", tags);
		model.addAttribute("page", blogService.listBolg(pageable, id));
		model.addAttribute("activeTagId", id);
		return "tags";
	}

}

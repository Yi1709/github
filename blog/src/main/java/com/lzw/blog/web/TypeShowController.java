package com.lzw.blog.web;

import com.lzw.blog.pojo.Type;
import com.lzw.blog.service.BlogService;
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
public class TypeShowController {

	@Autowired
	private TypeService typeService;

	@Autowired
	private BlogService blogService;

	@GetMapping("/types/{id}")
	public String types(@PathVariable Long id, @PageableDefault(size = 8, sort = {"updateTime"}, direction =
			Sort.Direction.DESC) Pageable pageable,
	                    Model model) {
		List<Type> types = this.typeService.listType(20000);
		if (id == -1) {
			id = types.get(0).getId();
		}
		BlogQuery blogQuery = new BlogQuery();
		blogQuery.setTypeId(id);
		model.addAttribute("types", types);
		model.addAttribute("page", this.blogService.listBolg(pageable, blogQuery));
		model.addAttribute("activeTypeId", id);
		return "types";
	}
}

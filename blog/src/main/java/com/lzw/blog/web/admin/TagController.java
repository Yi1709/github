package com.lzw.blog.web.admin;

import com.lzw.blog.pojo.Tag;
import com.lzw.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/18:17
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping("/tags")
	public String tags(@PageableDefault(size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
	                   Model model) {
		model.addAttribute("page", this.tagService.listTag(pageable));
		return "admin/tags";
	}

	@GetMapping("/tags/input")
	public String input(Model model) {
		model.addAttribute("tag", new Tag());
		return "admin/tags-input";
	}

	@GetMapping("/tags/{id}/input")
	public String editInput(@PathVariable Long id, Model model) {
		model.addAttribute("tag", this.tagService.getById(id));
		return "admin/tags-input";
	}


	@PostMapping("/tags")
	public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
		Tag oldTag = this.tagService.findByName(tag.getName());
		if (oldTag != null) {
			result.rejectValue("name", "nameError", "该标签已存在，无法重复添加!");
		}
		if (result.hasErrors()) {
			return "admin/tags-input";
		}
		Tag tag1 = this.tagService.saveTag(tag);
		if (tag1 == null) {
			attributes.addFlashAttribute("message", "添加失败，请稍后再试!");
		} else {
			attributes.addFlashAttribute("message", "Succes,添加成功!");
		}
		return "redirect:/admin/tags";
	}

	@PostMapping("/tags/{id}")
	public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id,
	                       RedirectAttributes attributes) {
		Tag oldTag = this.tagService.findByName(tag.getName());
		if (oldTag != null) {
			result.rejectValue("name", "nameError", "该标签已存在，无法重复添加!");
		}
		if (result.hasErrors()) {
			return "admin/tags-input";
		}
		Tag tag1 = this.tagService.update(id,tag);
		if (tag1 == null) {
			attributes.addFlashAttribute("message", "添加失败，请稍后再试!");
		} else {
			attributes.addFlashAttribute("message", "Succes,添加成功!");
		}
		return "redirect:/admin/tags";
	}
	@GetMapping("/tags/{id}/delete")
	public String delete(@PathVariable Long id,RedirectAttributes attributes) {
		this.tagService.deleteTag(id);
		attributes.addFlashAttribute("message", "删除成功");
		return "redirect:/admin/tags";
	}

}

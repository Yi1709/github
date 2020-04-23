package com.lzw.blog.web.admin;

import com.lzw.blog.pojo.Type;
import com.lzw.blog.service.TypeService;
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

import javax.persistence.Id;
import javax.validation.Valid;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/16:19
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

	@Autowired
	private TypeService typeService;

	@GetMapping("/types")
	public String types(@PageableDefault(size = 4, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
	                    Model model) {
		model.addAttribute("page", this.typeService.listType(pageable));
		return "admin/types";
	}

	@GetMapping("/types/input")
	public String input(Model model) {
		model.addAttribute("type", new Type());
		return "admin/types-input";
	}

	@GetMapping("/types/{id}/input")
	public String editInput(@PathVariable Long id, Model model) {
		model.addAttribute("type", this.typeService.getType(id));
		return "admin/types-input";
	}

	@PostMapping("/types")
	public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
		Type oldType = this.typeService.findByName(type.getName());
		if (oldType != null) {
			result.rejectValue("name", "nameError", "该分类名称已存在，请重新输入!");
		}
		if (result.hasErrors()) {
			return "admin/types-input";
		}
		Type type1 = this.typeService.saveType(type);
		if (type1 == null) {
			attributes.addFlashAttribute("message", "新增失败");
		} else {
			attributes.addFlashAttribute("message", "新增成功");

		}
		return "redirect:/admin/types";
	}

	@PostMapping("/types/{id}")
	public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id,
	                       RedirectAttributes attributes) {
		Type oldType = this.typeService.findByName(type.getName());
		if (oldType != null) {
			result.rejectValue("name", "nameError", "该分类名称已存在，请重新输入!");
		}
		if (result.hasErrors()) {
			return "admin/types-input";
		}
		Type type1 = this.typeService.update(id, type);
		if (type1 == null) {
			attributes.addFlashAttribute("message", "更新失败");
		} else {
			attributes.addFlashAttribute("message", "更新成功");

		}
		return "redirect:/admin/types";
	}

	@GetMapping("/types/{id}/delete")
	public String delete(@PathVariable Long id,RedirectAttributes attributes) {
		this.typeService.deleteType(id);
		attributes.addFlashAttribute("message", "删除成功");
		return "redirect:/admin/types";
	}

}

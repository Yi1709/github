package com.myshare.code.controller.admin;

import com.myshare.code.entity.Link;
import com.myshare.code.run.StartRunner;
import com.myshare.code.service.LinkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/link")
public class LinkAdminController {

	@Autowired
	private LinkService linkService;

	@Autowired
	private StartRunner startRunner;

	/**
	 * 分页查询
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = {"分页查询友情链接"})
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value =
			"pageSize", required = false) Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", linkService.list(page, pageSize, Sort.Direction.ASC, "sort"));
		map.put("total", linkService.getcount());
		map.put("errorNo", 0);
		return map;

	}

	@RequestMapping("/findById")
	@RequiresPermissions(value = {"查询友情链接实体"})
	public Map<String, Object> findById(Integer linkId) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", linkService.getById(linkId));
		map.put("errorNo", 0);
		return map;

	}

	@RequestMapping("/delete")
	@RequiresPermissions(value = {"删除友情链接实体"})
	public Map<String, Object> delete(@RequestParam(value = "linkId") String ids) {
		Map<String, Object> map = new HashMap<>();
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			this.linkService.deleteById(Integer.parseInt(idsStr[i]));
		}
		startRunner.loadData();
		map.put("errorNo", 0);
		return map;

	}

	@RequestMapping("/save")
	@RequiresPermissions(value = {"添加或修改友情链接实体"})
	public Map<String, Object> save(Link link) {
		Map<String, Object> map = new HashMap<>();
		this.linkService.save(link);
		startRunner.loadData();
		map.put("errorNo", 0);
		return map;

	}

}

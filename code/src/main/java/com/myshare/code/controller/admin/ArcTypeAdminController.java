package com.myshare.code.controller.admin;


import com.myshare.code.entity.ArcType;
import com.myshare.code.run.StartRunner;
import com.myshare.code.service.ArcTypeService;
import com.myshare.code.util.Consts;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/arcType")
public class ArcTypeAdminController {

	@Autowired
	private ArcTypeService arcTypeService;

	@Autowired
	private StartRunner startRunner;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/**
	 * 带条件地查询资源类型列表
	 *
	 * @return
	 */
	@RequestMapping("/list")
	public Map<String, Object> list(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "pageSize",
			required = false) Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		int count = arcTypeService.getCount().intValue();
		if (page == null && pageSize == null) {
			page = 1;
			pageSize = count > 0 ? count : 1;
		}

		map.put("data", this.arcTypeService.list(page, pageSize, Sort.Direction.ASC, "sort") );
		map.put("total", count);
		map.put("errorNo", 0);
		return map;
	}

	/**
	 * 根据id类型查询资源类型实体
	 */
	@RequestMapping("/findById")
	@RequiresPermissions(value = "根据id查询资源类型实体")
	public Map<String, Object> findById(Integer arcTypeId) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", arcTypeService.getById(arcTypeId));
		map.put("errorNo", 0);
		return map;
	}

	/**
	 * 添加或修改资源类型
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = "添加或修改资源类型实体")
	public Map<String, Object> save(ArcType arcType) {
		Map<String, Object> map = new HashMap<>();
		this.arcTypeService.save(arcType);
		map.put("errorNo", 0);
		return map;
	}

	/**
	 * 批量删除功能
	 */
	@RequiresPermissions(value = "删除资源类型实体")
	@RequestMapping("/delete")
	public Map<String, Object> delete(@RequestParam(value = "arcTypeId") String ids) {
		Map<String, Object> map = new HashMap<>();
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			this.arcTypeService.delete(Integer.parseInt(idsStr[i]));
		}
		redisTemplate.delete(Consts.ALL_ARC_TYPE_NAME);
		startRunner.loadData();
		map.put("errorNo", 0);
		return map;
	}
}

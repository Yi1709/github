package com.myshare.code.service;


import com.myshare.code.entity.ArcType;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

public interface ArcTypeService {


	/**
	 * 分页查询资源类型
	 *
	 * @param page       当前页
	 * @param pageSize   每页大小
	 * @param direction  排序规则
	 * @param properties 排序字段
	 * @return
	 */
	public List<ArcType> list(Integer page, Integer pageSize, Direction direction, String... properties);

	public List listAll(Direction direction, String... properties);

	/**
	 * 获取总记录数
	 */

	public Long getCount();

	public void save(ArcType arcType);

	public void delete(Integer id);

	public ArcType getById(Integer id);

}

package com.myshare.code.service.impl;

import com.myshare.code.entity.ArcType;
import com.myshare.code.repository.ArcTypeRepository;
import com.myshare.code.run.StartRunner;
import com.myshare.code.service.ArcTypeService;
import com.myshare.code.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArcTypeServiceImpl implements ArcTypeService {

	@Autowired
	private ArcTypeRepository arcTypeRepository;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private StartRunner startRunner;

	@Override
	public List<ArcType> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
		Page<ArcType> arcTypePage = this.arcTypeRepository.findAll(PageRequest.of(page - 1, pageSize, direction, properties));
		return arcTypePage.getContent();
	}

	@Override
	public List listAll(Sort.Direction direction, String... properties) {
		if (redisTemplate.hasKey(Consts.ALL_ARC_TYPE_NAME)) {
			return redisTemplate.opsForList().range(Consts.ALL_ARC_TYPE_NAME, 0, -1);
		} else {

			List list = arcTypeRepository.findAll(Sort.by(direction, properties));
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					redisTemplate.opsForList().rightPush(Consts.ALL_ARC_TYPE_NAME, list.get(i));
				}
			}
			return list;
		}
	}

	@Override
	public Long getCount() {

		return this.arcTypeRepository.count();
	}

	@Override
	public void save(ArcType arcType) {
		boolean flag = false;
		if (arcType.getArcTypeId() == null) {
			flag = true;
		}
		this.arcTypeRepository.save(arcType);
		if (flag == true) {   //新增类型
			redisTemplate.opsForList().rightPush(Consts.ALL_ARC_TYPE_NAME, arcType);
		} else {         //修改类型
			redisTemplate.delete(Consts.ALL_ARC_TYPE_NAME);
		}
		startRunner.loadData();
	}

	@Override
	public void delete(Integer id) {
		this.arcTypeRepository.deleteById(id);
	}

	@Override
	public ArcType getById(Integer id) {
		return this.arcTypeRepository.getOne(id);
	}
}

package com.lzw.blog.service.impl;

import com.lzw.blog.NotFoundException;
import com.lzw.blog.pojo.Type;
import com.lzw.blog.repository.TypeRepository;
import com.lzw.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/16:10
 * @Description:
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

	@Autowired
	private TypeRepository typeRepository;

	@Override
	public Type saveType(Type type) {
		return this.typeRepository.save(type);
	}

	@Override
	public Type getType(Long id) {
		return this.typeRepository.getOne(id);
	}

	@Override
	public Type findByName(String name) {
		return this.typeRepository.findByName(name);
	}

	@Override
	public Page<Type> listType(Pageable pageable) {
		return this.typeRepository.findAll(pageable);
	}

	@Override
	public Type update(Long id, Type type) {
		Type oldType = this.typeRepository.getOne(id);
		if(oldType==null){
			throw  new NotFoundException("不存在该类型!");
		}
		BeanUtils.copyProperties(type, oldType);
		return this.typeRepository.save(oldType);
	}

	@Override
	public void deleteType(Long id) {
		this.typeRepository.deleteById(id);
	}

	@Override
	public List<Type> listType() {
		return this.typeRepository.findAll();
	}

	@Override
	public List<Type> listType(Integer size) {
		return this.typeRepository.FindTop(PageRequest.of(0, size, Sort.Direction.DESC,"blogs.size"));
	}
}

package com.lzw.blog.service.impl;

import com.lzw.blog.NotFoundException;
import com.lzw.blog.pojo.Tag;
import com.lzw.blog.repository.TagRepository;
import com.lzw.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/18:12
 * @Description:
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

	@Autowired
	private TagRepository tagRepository;

	@Override
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}

	@Override
	public void deleteTag(Long id) {
		this.tagRepository.deleteById(id);
	}

	@Override
	public Page<Tag> listTag(Pageable pageable) {
		return this.tagRepository.findAll(pageable);
	}

	@Override
	public Tag update(Long id, Tag tag) {
		Tag oldTag = this.tagRepository.getOne(id);
		if (oldTag == null) {
			throw new NotFoundException("该标签不存在，无法修改噢!");
		}
		BeanUtils.copyProperties(tag, oldTag);
		return this.tagRepository.save(oldTag);
	}

	@Override
	public Tag findByName(String name) {
		return this.tagRepository.findByName(name);
	}

	@Override
	public Tag getById(Long id) {
		return this.tagRepository.getOne(id);
	}

	@Override
	public List<Tag> listTag() {
		return this.tagRepository.findAll();
	}

	@Override
	public List<Tag> listTag(String ids) {
		return this.tagRepository.findAllById(this.toList(ids));
	}

	@Override
	public List<Tag> listTagTop(Integer size) {
		return this.tagRepository.findTop(PageRequest.of(0, size, Sort.Direction.DESC, "blogs.size"));
	}

	private List<Long> toList(String ids) {
		List<Long> list = new ArrayList<>();
		if (!StringUtils.isEmpty(ids)) {
			String[] idsStr = ids.split(",");
			for (int i = 0; i < idsStr.length; i++) {
				list.add(new Long(idsStr[i]));
			}
		}
		return list;
	}
}
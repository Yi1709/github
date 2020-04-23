package com.lzw.blog.service;

import com.lzw.blog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/18:10
 * @Description:
 */
public interface TagService {
	Tag saveTag(Tag tag);

	void deleteTag(Long id);

	Page<Tag> listTag(Pageable pageable);

	Tag update(Long id, Tag tag);

	Tag findByName(String name);

	Tag getById(Long id);

	List<Tag> listTag();

	List<Tag> listTag(String ids);

	List<Tag> listTagTop(Integer size);
}

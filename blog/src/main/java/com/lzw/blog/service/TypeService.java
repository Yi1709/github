package com.lzw.blog.service;

import com.lzw.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/16:07
 * @Description:
 */
public interface TypeService {
	Type saveType(Type type);

	Type getType(Long id);

	Type findByName(String name);

	Page<Type> listType(Pageable pageable);

	Type update(Long id, Type type);

	void deleteType(Long id);

	List<Type> listType();

	List<Type> listType(Integer size);
}

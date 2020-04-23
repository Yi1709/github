package com.lzw.blog.repository;

import com.lzw.blog.pojo.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/18:09
 * @Description:
 */
public interface TagRepository  extends JpaRepository<Tag,Long>, JpaSpecificationExecutor<Tag> {

	@Query(value = "select * from t_tag where name =?1",nativeQuery=true)
	Tag findByName(String name);

	@Query("select t from Tag t")
	List<Tag> findTop(Pageable pageable);
}

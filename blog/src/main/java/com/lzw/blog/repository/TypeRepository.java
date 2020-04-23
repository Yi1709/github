package com.lzw.blog.repository;

import com.lzw.blog.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/16:11
 * @Description:
 */
public interface TypeRepository extends JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {

	@Query(value = "select * from t_type where name =?1",nativeQuery=true)
	Type findByName(String name);

	@Query("select t from Type t")
	List<Type> FindTop(Pageable pageable);
}

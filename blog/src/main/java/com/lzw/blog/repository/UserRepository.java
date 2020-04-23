package com.lzw.blog.repository;

import com.lzw.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/12:45
 * @Description:
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findByUsernameAndPassword(String username,String password);
}

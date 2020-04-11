package com.myshare.code.repository;

import com.myshare.code.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 用户repository接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	/**
	 * 根据用户名查找用户实体
	 *
	 * @param userName
	 * @return
	 */
	@Query(value = "select * from user where user_name =?1 ", nativeQuery = true)
	public User findByUserName(String userName);

	/**
	 * 根据邮箱查找用户
	 *
	 * @param email
	 * @return
	 */
	@Query(value = "select  * from User where email =?1", nativeQuery = true)
	public User findByEmail(String email);

	@Query(value = "select  count(*) from User where TO_DAYS(registration_date)=TO_DAYS(NOW())", nativeQuery = true)
	public Integer todayRegist();

	@Query(value = "select  count(*) from User where TO_DAYS(lately_login_time)=TO_DAYS(NOW())", nativeQuery = true)
	public Integer todayLogin();

}

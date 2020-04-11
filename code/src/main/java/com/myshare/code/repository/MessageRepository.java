package com.myshare.code.repository;

import com.myshare.code.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface MessageRepository extends JpaSpecificationExecutor<Message>, JpaRepository<Message, Integer> {

	/**
	 * 查询某个用户下的所有消息总数（未查看）
	 */

	@Query(value = "select count(*) from message where is_see=false and user_id=?1",nativeQuery = true)
	Integer getCountByUserId(Integer userId);

	/**
	 * 看所有消息
	 */
	@Query(value = "update message set is_see = true where user_id= ?1 ", nativeQuery = true)
	@Modifying
	public void updateState(Integer userId);
}
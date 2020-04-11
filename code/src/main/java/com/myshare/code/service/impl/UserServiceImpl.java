package com.myshare.code.service.impl;

import com.myshare.code.entity.User;
import com.myshare.code.repository.UserRepository;
import com.myshare.code.service.UserService;
import com.myshare.code.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findByUserName(String userName) {
		User user = this.userRepository.findByUserName(userName);
		return user;
	}

	@Override
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public void save(User user) {
		userRepository.saveAndFlush(user);
	}

	@Override
	public User getById(Integer id) {
		return this.userRepository.getOne(id);
	}

	@Override
	public Long getCount(User s_user, String s_blatelyLoginDate, String s_elatelyLoginDate) {
		return this.userRepository.count(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
					cb) {
				Predicate predicate = cb.conjunction();
				if (StringUtil.notEmpty(s_blatelyLoginDate)) {
					predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("latelyLoginTime").as(String
									.class),
							s_blatelyLoginDate));
				}
				if (StringUtil.notEmpty(s_elatelyLoginDate)) {
					predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("latelyLoginTime").as(String.class),
							s_elatelyLoginDate));
				}
				if (s_user != null) {
					if (StringUtil.notEmpty(s_user.getSex())) {
						predicate.getExpressions().add(cb.equal(root.get("sex"), s_user.getSex()));
					}
					if (StringUtil.notEmpty(s_user.getUserName())) {
						predicate.getExpressions().add(cb.equal(root.get("userName"), "%" + s_user.getUserName() +
								"%"));
					}
				}
				return predicate;
			}
		});
	}

	@Override
	public Integer todayRegist() {
		return this.userRepository.todayRegist();
	}

	@Override
	public Integer todayLogin() {
		return this.userRepository.todayLogin();
	}

	@Override
	public List<User> list(User s_user, String s_blatelyLoginTime, String s_elatelyLoginTime, Integer page, Integer
			pageSize, Sort.Direction direction, String... properties) {
		Page<User> userPage = this.userRepository.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
					cb) {
				Predicate predicate = cb.conjunction();
				if (StringUtil.notEmpty(s_blatelyLoginTime)) {
					predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("latelyLoginTime").as(String
									.class),
							s_blatelyLoginTime));
				}
				if (StringUtil.notEmpty(s_elatelyLoginTime)) {
					predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("latelyLoginTime").as(String.class),
							s_elatelyLoginTime));
				}
				if (s_user != null) {
					if (StringUtil.notEmpty(s_user.getSex())) {
						predicate.getExpressions().add(cb.equal(root.get("sex"), s_user.getSex()));
					}
					if (StringUtil.notEmpty(s_user.getUserName())) {
						predicate.getExpressions().add(cb.equal(root.get("userName"), "%" + s_user.getUserName() +
								"%"));
					}
				}
				return predicate;
			}
		}, PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "latelyLoginTime"));
		return userPage.getContent();
	}
}

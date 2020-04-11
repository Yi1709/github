package com.myshare.code.service.impl;

import com.myshare.code.entity.UserDownload;
import com.myshare.code.repository.UserDownRepository;
import com.myshare.code.service.UserDownloadService;
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

@Service
public class UserDownloadServiceImpl implements UserDownloadService {

	@Autowired
	private UserDownRepository userDownRepository;


	@Override
	public Integer getCountByUserIdAAndArticleId(Integer userId, Integer articleId) {
		return this.userDownRepository.getCountByUserIdAAndArticleId(userId, articleId);
	}

	@Override
	public Page<UserDownload> list(Integer userId, Integer page, Integer pageSize, Sort.Direction direction, String...
			properties) {
		return userDownRepository.findAll(new Specification<UserDownload>() {
			@Override
			public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
					cb) {
				Predicate predicate = cb.conjunction();
				if (userId != null) {
					predicate.getExpressions().add(cb.equal(root.get("user").get("userId"), userId));
				}
				return predicate;
			}
		}, PageRequest.of(page - 1, pageSize, direction, properties));
	}

	@Override
	public Long getCount(Integer userId) {
		return this.userDownRepository.count(new Specification<UserDownload>() {
			@Override
			public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
					cb) {
				Predicate predicate = cb.conjunction();
				if (userId != null) {
					predicate.getExpressions().add(cb.equal(root.get("user").get("userId"), userId));
				}
				return predicate;
			}
		});
	}

	@Override
	public void save(UserDownload userDownload) {
		this.userDownRepository.save(userDownload);
	}
}

package com.myshare.code.service.impl;

import com.myshare.code.entity.Message;
import com.myshare.code.repository.MessageRepository;
import com.myshare.code.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {


	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Integer getCountByUserId(Integer userId) {
		return this.messageRepository.getCountByUserId(userId);
	}

	@Override
	public void updateState(Integer userId) {
		this.messageRepository.updateState(userId);
	}

	@Override
	public Page<Message> list(Integer userId, Integer page, Integer pageSize, Sort.Direction direction, String...
			properties) {
		return this.messageRepository.findAll(new Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
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
		return this.messageRepository.count(new Specification<Message>() {
			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
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
	public void save(Message message) {
		this.messageRepository.save(message);
	}
}

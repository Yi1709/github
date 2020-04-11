package com.myshare.code.service.impl;

import com.myshare.code.entity.Comment;
import com.myshare.code.repository.CommentRepository;
import com.myshare.code.service.CommentService;
import javafx.scene.transform.Rotate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.attribute.standard.PagesPerMinute;


@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void save(Comment comment) {
		this.commentRepository.save(comment);
	}

	@Override
	public Page<Comment> list(Comment s_comment, Integer page, Integer PageSize, Sort.Direction direction, String...
			propoties) {
		return this.commentRepository.findAll(new Specification<Comment>() {
			@Override
			public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
					cb) {
				Predicate predicate = cb.conjunction();
				if (s_comment != null) {
					if (s_comment.getState() != null) {
						predicate.getExpressions().add(cb.equal(root.get("state"), s_comment.getState()));
					}
					if (s_comment.getArticle() != null && s_comment.getArticle().getArticleId() != null) {
						predicate.getExpressions().add(cb.equal(root.get("article").get("articleId"), s_comment
								.getArticle()
								.getArticleId()));
					}

				}

				return predicate;
			}
		}, PageRequest.of(page - 1, PageSize, direction, propoties));
	}

	@Override
	public Long getTotal(Comment comment) {
		return commentRepository.count(new Specification<Comment>() {
			@Override
			public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder
					criteriaBuilder) {
				Predicate predicate = criteriaBuilder.conjunction();
				if (comment != null) {
					if (comment.getState() != null) {
						predicate.getExpressions().add(criteriaBuilder.equal(root.get("state"), comment.getState()));
					}
					if (comment.getArticle() != null && comment.getArticle().getArticleId() != null) {
						predicate.getExpressions().add(criteriaBuilder.equal(root.get("article").get("articleId"),
								comment.getArticle().getArticleId()));
					}
				}
				return predicate;
			}
		});
	}

	@Override
	public Comment getById(Integer commentId) {
		return this.commentRepository.getOne(commentId);
	}

	@Override
	public void delete(Integer id) {
		this.commentRepository.deleteById(id);
	}

	@Override
	public void deleteByArticleId(Integer articleId) {
		this.commentRepository.deleteByArticleId(articleId);
	}
}

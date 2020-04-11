package com.myshare.code.service.impl;

import com.myshare.code.entity.Link;
import com.myshare.code.repository.LinkRepository;
import com.myshare.code.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

	@Autowired
	private LinkRepository linkRepository;


	@Override
	public List<Link> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
		return this.linkRepository.findAll(PageRequest.of(page - 1, pageSize, direction, properties)).getContent();
	}

	@Override
	public Long getcount() {
		return this.linkRepository.count();
	}

	@Override
	public void save(Link link) {
		this.linkRepository.save(link);
	}

	@Override
	public void deleteById(Integer id) {
		this.linkRepository.deleteById(id);
	}

	@Override
	public List<Link> listAll(Sort.Direction direction, String... properties) {
		Sort sort = Sort.by(direction, properties);
		return this.linkRepository.findAll();
	}

	@Override
	public Link getById(Integer id) {
		return this.linkRepository.getOne(id);
	}
}

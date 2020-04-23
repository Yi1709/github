package com.lzw.blog.service.impl;

import com.lzw.blog.NotFoundException;
import com.lzw.blog.pojo.Blog;
import com.lzw.blog.pojo.Comment;
import com.lzw.blog.pojo.Type;
import com.lzw.blog.repository.BlogRepository;
import com.lzw.blog.repository.CommentRepository;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.CommentService;
import com.lzw.blog.util.MarkDownUtil;
import com.lzw.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/21:18
 * @Description:
 */
@Service
@Transactional
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;


	@Override
	public Blog getBlog(Long id) {
		return this.blogRepository.getOne(id);
	}

	@Override
	@Transactional
	public Blog getBlogContent(Long id) {
		Blog blog = this.blogRepository.getOne(id);
		if (blog == null) {
			throw new NotFoundException("该博客不存在!");
		}
		Blog blog1 = new Blog();
		BeanUtils.copyProperties(blog, blog1);
		String content = blog1.getContent();
		blog1.setContent(MarkDownUtil.markdownToHtmlExtensions(content));
		this.blogRepository.updateViews(id);
		return blog1;
	}

	@Override
	public Page<Blog> listBolg(Pageable pageable, BlogQuery blog) {
		return this.blogRepository.findAll(new Specification<Blog>() {
			@Override
			public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery,
			                             CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if (!StringUtils.isEmpty(blog.getTitle())) {
					predicate.getExpressions().add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
				}
				if (blog.getTypeId() != null) {
					predicate.getExpressions().add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
				}
				if (blog.isRecommended()) {
					predicate.getExpressions().add(cb.equal(root.<Boolean>get("recommended"), blog.isRecommended()));
				}
				return predicate;
			}
		}, pageable);

	}

	@Override
	public Page<Blog> listBolg(Pageable pageable, Long tagId) {
		return this.blogRepository.findAll(new Specification<Blog>() {
			@Override
			public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery,
			                             CriteriaBuilder cb) {
				Join join = root.join("tags");
				Predicate predicate = cb.equal(join.get("id"), tagId);
				return predicate;
			}
		}, pageable);
	}

	@Override
	public Blog saveBlog(Blog blog) {
		if (blog.getId() == null) {
			blog.setCreateTime(new Date());
			blog.setUpdateTime(new Date());
			blog.setViews(50 + new Random().nextInt(230));
		} else {
			Blog oldBlog = this.blogRepository.getOne(blog.getId());
			blog.setUpdateTime(new Date());
			blog.setCreateTime(oldBlog.getCreateTime());
			blog.setViews(oldBlog.getViews());
		}
		return this.blogRepository.save(blog);
	}

	@Override
	public Blog updateBlog(Long id, Blog blog) {
		Blog oldBlog = this.blogRepository.getOne(id);
		if (oldBlog == null) {
			throw new NotFoundException("该博客不存在!");
		}
		BeanUtils.copyProperties(blog, oldBlog);
		return this.blogRepository.save(oldBlog);
	}

	@Override
	public void deleteBlog(Long id) {
		this.blogRepository.deleteById(id);
	}

	@Override
	public Page<Blog> listBlog(Pageable pageable) {
		return this.blogRepository.findAll(pageable);
	}

	@Override
	public List<Blog> listRecommendBlogTop(Integer size) {
		return this.blogRepository.findTop(PageRequest.of(0, size, Sort.Direction.DESC, "updateTime"));
	}

	@Override
	public Map<String, List<Blog>> archiveBlogs() {
		List<String> years = this.blogRepository.findGroupYear();
		Map<String, List<Blog>> map = new HashMap<>();
		for (String year : years) {
			map.put(year, this.blogRepository.findByYear(year));
		}
		return map;
	}

	@Override
	public Long countBlog() {
		return this.blogRepository.count();
	}

	@Override
	public Page<Blog> listBlog(Pageable pageable, String query) {
		return this.blogRepository.findByQuery(query, pageable);
	}
}

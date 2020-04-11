package com.myshare.code.service.impl;

import com.myshare.code.entity.ArcType;
import com.myshare.code.entity.Article;
import com.myshare.code.lucene.ArticleIndex;
import com.myshare.code.repository.ArticleRepository;
import com.myshare.code.run.StartRunner;
import com.myshare.code.service.ArcTypeService;
import com.myshare.code.service.ArticleService;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArcTypeService arcTypeService;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	private RedisSerializer serializer = new StringRedisSerializer(); //前缀相同的资源

	@Autowired
	@Lazy
	private StartRunner startRunner;

	@Autowired
	private ArticleIndex articleIndex;



	@Override
	public List<Article> list(Article s_article, String nickname, String s_bpublishDate, String s_epublishDate,
	                          Integer page, Integer
			                          pageSize, Sort.Direction direction, String... properties) {
		Page<Article> articlePage = this.articleRepository.findAll(new Specification<Article>() {
			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				return getPredicate(root, cb, s_bpublishDate, s_epublishDate, nickname, s_article);
			}
		}, PageRequest.of(page - 1, pageSize, direction, properties));
		return articlePage.getContent();
	}

	@Override
	public Long getcount(Article s_article, String nickname, String s_bpublishDate, String s_epublishDate) {
		Long count = articleRepository.count(new Specification<Article>() {
			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				return getPredicate(root, cb, s_bpublishDate, s_epublishDate, nickname, s_article);
			}
		});
		return count;
	}

	private Predicate getPredicate(Root<Article> root, CriteriaBuilder cb, String s_bpublishDate, String
			s_epublishDate, String nickname,
	                               Article s_article) {
		Predicate predicate = cb.conjunction();
		if (!StringUtils.isBlank(s_bpublishDate)) {
			predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("publishDate").as(String.class),
					s_bpublishDate));
		}
		if (!StringUtils.isBlank(s_epublishDate)) {
			predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("publishDate").as(String.class),
					s_bpublishDate));
		}
		if (!StringUtils.isBlank(nickname)) {
			predicate.getExpressions().add(cb.like(root.get("user").get("nickname"), "%" + nickname + "%"));

		}
		if (s_article != null) {
			if (!StringUtils.isBlank(s_article.getName())) {
				predicate.getExpressions().add(cb.like(root.get("name"), "%" + s_article.getName() + "%"));
			}
			if (s_article.isHot()) {
				predicate.getExpressions().add(cb.equal(root.get("isHot"), 1));
			}
			if (s_article.getArcType() != null && s_article.getArcType().getArcTypeId() != null) {
				predicate.getExpressions().add(cb.equal(root.get("arcType").get("arcTypeId"), s_article.getArcType()
						.getArcTypeId
								()));
			}
			if (s_article.getUser() != null && s_article.getUser().getUserId() != null) {
				predicate.getExpressions().add(cb.equal(root.get("user").get("userId"), s_article.getUser().getUserId
						()));
			}
			if (s_article.getState() != null) {
				predicate.getExpressions().add(cb.equal(root.get("state"), s_article.getState()));
			}
			if (!s_article.isUserful()) {
				predicate.getExpressions().add(cb.equal(root.get("isUserful"), false));
			}
		}
		return predicate;
	}

	@Override
	public void save(Article article) {
		//将审核通过的资源存入redis
		if (article.getState() == 2) {
			redisTemplate.setKeySerializer(serializer);
			redisTemplate.opsForValue().set("article_" + article.getArticleId(), article);
			startRunner.loadData();
		}
		this.articleRepository.save(article);
	}

	@Override
	public void delete(Integer id) {
		redisTemplate.opsForList().remove("allarticleId", 0, id);
		int arcTypeId = this.articleRepository.getArcTypeIdByArticleId(id);
		redisTemplate.opsForList().remove("article_type_" + arcTypeId, 0, id);
		redisTemplate.delete("article_" + id);
		this.articleRepository.deleteById(id);
	}

	@Override
	public Article getById(Integer id) {
		if (redisTemplate.hasKey("article_" + id)) {
			Article article = (Article) redisTemplate.opsForValue().get("article_" + id);
			return article;
		} else {
			Article article = this.articleRepository.getOne(id);
			if (article.getState() == 2) {
				redisTemplate.setKeySerializer(serializer);
				redisTemplate.opsForValue().set("article_" + article.getArticleId(), article);
			}
			return article;
		}

	}

	@Override
	public Map<String, Object> list(String type, Integer page, Integer pageSize) {
		//1.初始化redis模型
		redisTemplate.setKeySerializer(serializer);
		ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
		ListOperations<Object, Object> opsForList = redisTemplate.opsForList();

		Map<String, Object> resultmap = new HashMap<>();
		List<Article> tempList = new ArrayList<>();
		//2.判断redis中有没有数据
		boolean flag = redisTemplate.hasKey("allarticleId");
		//3.从数据库中查询
		if (!flag) {
			//3.1遍历资源列表，
			List<Article> listStatePass = new ArrayList<>();
			for (Article article : listStatePass) {
				//3.2将每个资源放入redis
				opsForValue.set("article_" + article.getArticleId(), article);
				//3.3将每一个源id放入redis的allarticleId列表
				opsForList.rightPush("allarticleId", article.getArticleId());
				//3.4遍历资源类型列表，将该资源推入相应的redis资源类型列表
				List<ArcType> arcTypeList = this.arcTypeService.listAll(Sort.Direction.DESC, "sort");
				for (ArcType arcType : arcTypeList) {
					if (article.getArcType().getArcTypeId() == arcType.getArcTypeId()) {
						opsForList.rightPush("article_type_" + arcType.getArcTypeId(), article.getArticleId());
					}
				}
			}
		}

		//4.分页资源列表，返回当前页
		long start = (page - 1) * pageSize;
		long end = page * pageSize - 1;
		List idList;
		long count;
		if ("all".equals(type)) {
			idList = opsForList.range("allarticleId", start, end);
			count = opsForList.size("allarticleId");
		} else {
			idList = opsForList.range("article_type_" + type, start, end);
			count = opsForList.size("article_type_" + type);
		}

		for (Object id : idList) {
			tempList.add((Article) opsForValue.get("article_" + id));
		}
		resultmap.put("data", tempList);
		resultmap.put("count", count);

		return resultmap;
	}

	@Override
	public List<Article> listStatePass() {
		return this.articleRepository.findAll(new Specification<Article>() {
			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				predicate.getExpressions().add(cb.equal(root.get("state"), 2));
				return predicate;
			}
		}, Sort.by(Sort.Direction.DESC, "publishDate"));
	}

	@Override
	public void updateClick(Integer articleId) {
		this.articleRepository.updateClick(articleId);
		Article article = this.articleRepository.getOne(articleId);
		if (article.getState() == 2) {
			redisTemplate.setKeySerializer(serializer);
			redisTemplate.opsForValue().set("article_" + article.getArticleId(), article);
		}
	}

	@Override
	public Integer todayPubish() {
		return this.articleRepository.todayPubish();
	}

	@Override
	public Integer todayNoAudit() {
		return this.articleRepository.todayNoAudit();
	}

	@Override
	public List<Article> getNewArticle(Integer n) {
		return this.articleRepository.getNewArticle(n);
	}

	@Override
	public List<Article> getHotArticle(Integer n) {
		return this.articleRepository.getHotArticle(n);
	}

	@Override
	public List<Article> getRandomArticle(Integer n) {
		return articleRepository.getRandomArticle(n);
	}
}

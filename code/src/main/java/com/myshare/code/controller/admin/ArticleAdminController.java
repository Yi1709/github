package com.myshare.code.controller.admin;

import ch.qos.logback.classic.turbo.TurboFilter;
import com.myshare.code.entity.Article;
import com.myshare.code.entity.Message;
import com.myshare.code.lucene.ArticleIndex;
import com.myshare.code.service.ArticleService;
import com.myshare.code.service.CommentService;
import com.myshare.code.service.MessageService;
import com.myshare.code.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import javax.print.DocFlavor;
import java.util.*;

@RestController
@RequestMapping("/admin/article")
public class ArticleAdminController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private ArticleIndex articleIndex;

	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageService messageService;

	/**
	 * 根据条件分别查询资源列表
	 *
	 * @param s_article
	 * @param nickname
	 * @param publishDates
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = "分页查询资源")
	public Map<String, Object> list(Article s_article,
	                                @RequestParam(value = "nickname", required = false) String nickname,
	                                @RequestParam(value = "publishDate", required = false) String publishDates,
	                                @RequestParam(value = "page", required = false) Integer page,
	                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		String s_bpublishDate = null;//开始时间
		String s_epublishDate = null; //结束时间
		if (!StringUtils.isBlank(publishDates)) {
			String[] str = publishDates.split(" - ");
			s_bpublishDate = str[0];
			s_epublishDate = str[1];
		}
		map.put("data", this.articleService.list(s_article, nickname, s_bpublishDate, s_epublishDate, page, pageSize,
				Sort
						.Direction.DESC, "publishDate"));
		map.put("total", this.articleService.getcount(s_article, nickname, s_bpublishDate, s_epublishDate));
		map.put("errorNo", 0);
		return map;

	}

	/**
	 * 删除资源
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = "删除资源")
	public Map<String, Object> delete(@RequestParam(value = "articleId", required = true) String articleIds) {
		Map<String, Object> map = new HashMap<>();
		String[] articleIdsStr = articleIds.split(",");
		for (int i = 0; i < articleIdsStr.length; i++) {
			// TODO: 2020/4/6  删除评论
			this.commentService.deleteByArticleId(Integer.parseInt(articleIdsStr[i]));
			this.articleService.delete(Integer.parseInt(articleIdsStr[i]));
			articleIndex.deleteIndex(articleIdsStr[i]);
		}
		map.put("errorNo", 0);
		return map;
	}

	/**
	 * 查看或审核修改数据
	 */
	@RequestMapping("/findById")
	@RequiresPermissions(value = "查看或审核修改资源")
	public Map<String, Object> toModifyArticlePage(Integer articleId) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> tempMap = new HashMap<>();
		Article article = this.articleService.getById(articleId);
		tempMap.put("articleId", article.getArticleId());
		tempMap.put("name", article.getName());
		tempMap.put("arcType", article.getArcType().getArcTypeId());
		tempMap.put("points", article.getPoints());
		tempMap.put("content", article.getContent());
		tempMap.put("download", article.getDownload());
		tempMap.put("password", article.getPassword());
		tempMap.put("click", article.getClick());
		tempMap.put("keywords", article.getKeywords());
		tempMap.put("description", article.getDescription());
		map.put("data", tempMap);
		map.put("errorNo", 0);
		return map;
	}

	/**
	 * 审核资源
	 */
	@RequestMapping("/updateState")
	@RequiresPermissions(value = "审核资源")
	public Map<String, Object> updateState(Article article) {
		Map<String, Object> map = new HashMap<>();
		Article newArticle = this.articleService.getById(article.getArticleId());
		newArticle.setCheckDate(new Date());
		Message message = new Message();
		message.setUser(newArticle.getUser());
		message.setPublishDate(new Date());
		if (article.getState() == 2) {
			message.setContent("【<font color='#00ff7f' >审核成功</font>】您发布的【"+newArticle.getName()+"】审核成功");
			newArticle.setState(2);
		} else if (article.getState() == 3) {
			message.setContent("【<font color='red'>审核失败</font>】您发布的【"+newArticle.getName()+"】资源审核失败！");
			message.setCause(newArticle.getReason());
			newArticle.setState(3);
			newArticle.setReason(article.getReason());
		}
		messageService.save(message);
		this.articleService.save(newArticle);
		if (newArticle.getState() == 2) {
			newArticle.setContentNoTag(StringUtil.html(newArticle.getContent()));
			articleIndex.addIndex(newArticle);  //添加索引
			redisTemplate.opsForList().leftPush("allarticleId", newArticle.getArticleId());
			redisTemplate.opsForList().leftPush("article_type_" + newArticle.getArcType().getArcTypeId(), newArticle
					.getArticleId());
		}
		map.put("errorNo", 0);
		return map;
	}

	/**
	 * 生成所有资源帖子索引
	 */
	@ResponseBody
	@RequestMapping(value = "/genAllIndex")
	@RequiresPermissions(value = "生成所有资源帖子索引")
	public boolean genAllIndex() {
		List<Article> articleList = articleService.listStatePass();
		if (articleList == null || articleList.size() == 0) {
			return false;
		}
		for (Article article : articleList) {
			try {

				article.setContentNoTag(StringUtil.html(article.getContent()));
				articleIndex.addIndex(article);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * 修改热门资源状态
	 */
	@RequestMapping("/updateHotState")
	@RequiresPermissions("修改热门资源状态")
	public Map<String, Object> updateHotState(Integer articleId, boolean isHot) {
		Article oldArticle = this.articleService.getById(articleId);
		oldArticle.setHot(isHot);
		this.articleService.save(oldArticle);
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	/**
	 * 修改热门资源状态
	 */
	@RequestMapping("/updateFreeState")
	@RequiresPermissions("修改免费资源状态")
	public Map<String, Object> updateFreeState(Integer articleId, boolean isFree) {
		Article oldArticle = this.articleService.getById(articleId);
		oldArticle.setFree(isFree);
		this.articleService.save(oldArticle);
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}

}

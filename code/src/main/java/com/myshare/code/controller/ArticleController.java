package com.myshare.code.controller;

import com.myshare.code.entity.Article;
import com.myshare.code.lucene.ArticleIndex;
import com.myshare.code.service.ArcTypeService;
import com.myshare.code.service.ArticleService;
import com.myshare.code.util.Consts;
import com.myshare.code.util.HTMLUtil;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import sun.awt.ModalityListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 资源控制器
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArcTypeService arcTypeService;

	@Autowired
	private ArticleIndex articleIndex;


	/**
	 * 按资源类型分页查询资源列表
	 *
	 * @param type
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/{type}/{currentPage}")
	public ModelAndView index(@PathVariable(value = "type", required = false) String type, @PathVariable(value =
			"currentPage", required =
			false) Integer currentPage) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		//类型的html代码
		List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
		modelAndView.addObject("arcTypeStr", HTMLUtil.getArcTypeStr(type, arcTypeList));

		//资源列表
		Map<String, Object> map = this.articleService.list(type, currentPage, Consts.PAGE_SIZE);
		modelAndView.addObject("articleList", map.get("data"));

		//分页html代码
		modelAndView.addObject("pageStr", HTMLUtil.getPagetion("/article/" + type, Integer.parseInt(String.valueOf(map
						.get("count"))), currentPage,
				"该分类还没有数据..."));

		return modelAndView;

	}


	@RequestMapping("/search")
	public ModelAndView search(String keywords, @RequestParam(value = "page", required = false) Integer page) throws
			Exception {
		if (page == null) {
			page = 1;
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		//类型的html代码
		List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
		modelAndView.addObject("arcTypeStr", HTMLUtil.getArcTypeStr("all", arcTypeList));

		//资源列表
		List<Article> articleList = articleIndex.search(keywords);
		Integer toIndex = articleList.size() >= page * Consts.PAGE_SIZE ? page * Consts.PAGE_SIZE : articleList.size();
		modelAndView.addObject("articleList", articleList.subList((page - 1) * Consts.PAGE_SIZE, toIndex));
		modelAndView.addObject("articleTotal", articleList.size());

		//分页html代码
		int totalPage = articleList.size() % Consts.PAGE_SIZE == 0 ? (arcTypeList.size() / Consts.PAGE_SIZE) :
				(arcTypeList.size() / Consts.PAGE_SIZE) + 1;
		String msg = "没有关键字是\"<font style=\"border: 0px;color:red;font-weight:bold;padding-left:3px;" +
				"padding-right:3px;\">" + keywords + "</font>\"的相关资源，请联系站长!";
		String targetUrl = "/article/search/?keywords=" + keywords;
		modelAndView.addObject("pageStr", HTMLUtil.getPagetion2(targetUrl, totalPage, page, msg));

		return modelAndView;
	}


	/**
	 * 资源详情
	 */
	@RequestMapping(value = "/detail/{articleId}")
	public ModelAndView detail(@PathVariable(value = "articleId", required = false) String articleId) throws
			IOException, org.apache.lucene.queryparser.classic.ParseException {
		ModelAndView mav = new ModelAndView();
		String replace = articleId.replace(".html", "");
		articleService.updateClick(Integer.parseInt(replace));
		Article article = articleService.getById(Integer.parseInt(replace));
		if (article.getState() != 2) {
			return null;
		}
		mav.addObject("article", article);
		//类型的html代码
		List arcTypeList = arcTypeService.listAll(Sort.Direction.ASC, "sort");
		mav.addObject("arcTypeStr", HTMLUtil.getArcTypeStr(article.getArcType().getArcTypeId().toString(),
				arcTypeList));
		//通过lucene 查找相似资源
		List<Article> articleList = articleIndex.searchNoHighLighter(article.getName().replace("视频", "").replace("教程",
				"").replace("下载", "").replace("" +
				"PDF", ""));

		if (articleList != null && articleList.size() >= 0) {
			mav.addObject("similarityArticleList", articleList);
		}
		mav.setViewName("detail");


		return mav;
	}

	/**
	 * 判断资源是否免费
	 */
	@ResponseBody
	@RequestMapping("/isfree")
	public boolean isfree(Integer articleId) {
		Article article = this.articleService.getById(articleId);
		return article.isFree();
	}


}

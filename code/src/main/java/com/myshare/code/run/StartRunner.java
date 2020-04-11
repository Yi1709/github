package com.myshare.code.run;

import com.myshare.code.service.ArcTypeService;
import com.myshare.code.service.ArticleService;
import com.myshare.code.service.LinkService;
import com.myshare.code.util.Consts;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * 服务器启动时，将数据加载入application中
 */
@Component("StartRunner")
public class StartRunner implements CommandLineRunner {

	@Autowired
	private ServletContext application;

	@Autowired
	private ArcTypeService arcTypeService;

	@Autowired
	private LinkService linkService;

	@Autowired
	@Lazy
	private ArticleService articleService;

	@Override
	public void run(String... args) throws Exception {
		loadData();
	}

	/**
	 * 加载数据到application缓存中
	 */
	public void loadData() {
		application.setAttribute(Consts.ARC_TYPE_LIST, arcTypeService.listAll(Sort.Direction.ASC, "sort"));
		//10条最新资源
		application.setAttribute(Consts.NEW_ARTICLE, articleService.getNewArticle(10));
		//10条热门资源
		application.setAttribute(Consts.CLICK_ARTICLE, articleService.getHotArticle(10));
		//10随机资源
		application.setAttribute(Consts.RANDOM_ARTICLE, articleService.getRandomArticle(10));
		application.setAttribute(Consts.LINK_LIST, linkService.listAll(Sort.Direction.ASC, "sort"));

	}
}

package com.lzw.blog.service.impl;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.entity.BlogType;
import com.lzw.blog.entity.Blogger;
import com.lzw.blog.entity.Link;
import com.lzw.blog.service.BlogService;
import com.lzw.blog.service.BlogTypeService;
import com.lzw.blog.service.BloggerService;
import com.lzw.blog.service.LinkService;
import com.lzw.blog.util.Consts;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/23/22:54
 * @Description:
 */
@Component
public class InitComponent implements ServletContextListener, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void contextInitialized(ServletContextEvent sce) {
		//将博客类别放入缓存中
		ServletContext application = sce.getServletContext();
		BlogTypeService blogTypeService = (BlogTypeService) applicationContext.getBean("blogTypeService");
		List<BlogType> blogTypeList = blogTypeService.countList();
		application.setAttribute(Consts.BLOG_TYPE_COUNT_LIST, blogTypeList);

		//博主信息
		BloggerService bloggerService = (BloggerService) applicationContext.getBean("bloggerService");
		Blogger blogger = bloggerService.find();
		blogger.setPassword(null);
		application.setAttribute(Consts.BLOGGER, blogger);

		//年月分类的博客数量
		BlogService blogService = (BlogService) applicationContext.getBean("blogService");
		List<Blog> blogs = blogService.countList();
		application.setAttribute(Consts.BLOG_COUNT_LIST, blogs);

		//友情链接
		LinkService linkService = (LinkService) applicationContext.getBean("linkService");
		List<Link> list = linkService.list(null);
		application.setAttribute(Consts.LINK_LIST, list);


	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
}

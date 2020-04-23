package com.lzw.blog.service;

import com.lzw.blog.pojo.Blog;
import com.lzw.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/21:16
 * @Description:
 */
public interface BlogService {

	Blog getBlog(Long id);

	Blog getBlogContent(Long id);

	Page<Blog> listBolg(Pageable pageable, BlogQuery blog);

	Page<Blog> listBolg(Pageable pageable, Long tagId);

	Blog saveBlog(Blog blog);

	Blog updateBlog(Long id, Blog blog);

	void deleteBlog(Long id);

	Page<Blog> listBlog(Pageable pageable);

	List<Blog> listRecommendBlogTop(Integer size);

	Map<String, List<Blog>> archiveBlogs();

	Long countBlog();

	Page<Blog> listBlog(Pageable pageable, String query);
}

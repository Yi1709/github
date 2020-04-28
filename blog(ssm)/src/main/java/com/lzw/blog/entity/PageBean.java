package com.lzw.blog.entity;

import java.io.Serializable;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/16:40
 * @Description:
 */
public class PageBean implements Serializable {

	private int page; //当前页数
	private int pageSize; //页面大小
	private int start; //从第几条数据开始

	public PageBean(int page, int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return (this.page-1)*pageSize;
	}
}

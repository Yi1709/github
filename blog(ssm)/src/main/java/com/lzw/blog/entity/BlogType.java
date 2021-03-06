package com.lzw.blog.entity;

import java.io.Serializable;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/15:59
 * @Description:
 */
public class BlogType implements Serializable {

	private Integer id;
	private String typeName;
	private Integer orderNo;
	//该类型下博客的数据
	private Integer blogCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}
}

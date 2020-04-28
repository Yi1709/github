package com.lzw.blog.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/22:21
 * @Description:
 */
public class Blog implements Serializable {

	private Integer id;
	private String title;
	private String summary; //摘要
	private Date releaseDate; //发表时间
	private Integer clickHit;
	private Integer replyHit;
	private String content;
	private BlogType blogType;
	private String keyWord;

	private String contentNoTag; //纯文本格式，方便lucene查询

	private String releaseDateStr; //方便操作

	private Integer blogCount;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Integer getClickHit() {
		return clickHit;
	}

	public void setClickHit(Integer clickHit) {
		this.clickHit = clickHit;
	}

	public Integer getReplyHit() {
		return replyHit;
	}

	public void setReplyHit(Integer replyHit) {
		this.replyHit = replyHit;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BlogType getBlogType() {
		return blogType;
	}

	public void setBlogType(BlogType blogType) {
		this.blogType = blogType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getContentNoTag() {
		return contentNoTag;
	}

	public void setContentNoTag(String contentNoTag) {
		this.contentNoTag = contentNoTag;
	}

	public String getReleaseDateStr() {
		return releaseDateStr;
	}

	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}

	public Integer getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}
}

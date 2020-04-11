package com.myshare.code.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "article")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "hander", "fieldHandler"})
public class Article implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer articleId;

	@NotEmpty(message = "资源名称不能为空")
	@Column(length = 200)
	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishDate;

	@Transient
	private String publishDateStr;

	@ManyToOne
	@JoinColumn(name = "userId")
	public User user;

	@ManyToOne
	@JoinColumn(name = "arcTypeId")
	private ArcType arcType;

	private boolean isFree;

	private Integer points;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String content;

	@Transient
	private String contentNoTag; //lucene分词中用到

	@Column(length = 200)
	private String download;

	@Column(length = 10)
	private String password;

	private boolean isHot=false;

	private Integer state;

	@Column(length = 200)
	private  String reason;


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private  Date checkDate;

	private  Integer  click;

	@Column(length = 200)
	private String keywords;

	@Column(length = 200)
	private String description;

	private boolean isUserful=true; //是否为免费资源

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishDateStr() {
		return publishDateStr;
	}

	public void setPublishDateStr(String publishDateStr) {
		this.publishDateStr = publishDateStr;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArcType getArcType() {
		return arcType;
	}

	public void setArcType(ArcType arcType) {
		this.arcType = arcType;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean free) {
		isFree = free;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentNoTag() {
		return contentNoTag;
	}

	public void setContentNoTag(String contentNoTag) {
		this.contentNoTag = contentNoTag;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isHot() {
		return isHot;
	}

	public void setHot(boolean hot) {
		isHot = hot;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUserful() {
		return isUserful;
	}

	public void setUserful(boolean userful) {
		isUserful = userful;
	}
}


package com.myshare.code.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UserDownload")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "hander", "fieldHandler"})
public class UserDownload implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userDownloadId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date downloadDate;

	@ManyToOne
	@JoinColumn(name = "articleId")
	private Article article;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public Integer getUserDownloadId() {
		return userDownloadId;
	}

	public void setUserDownloadId(Integer userDownloadId) {
		this.userDownloadId = userDownloadId;
	}

	public Date getDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

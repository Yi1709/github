package com.myshare.code.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论实体类
 *
 * @author bzd
 */
@Entity
@Table(name = "comment")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "hander", "fieldHandler"})
public class Comment implements Serializable {


	private static final long serialVersionUID = 1259531200374106152L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentId;          //评论id

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date commentDate;  //评论时间
	@ManyToOne
	@JoinColumn(name = "articleId")
	private Article article; //评论文章id

	@Column(length = 1000)
	private String content;//评论内容

	private Integer state;//评论状态 0未审核 1 审核通过 2审核驳回
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user; //评论用户Id

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

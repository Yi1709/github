package com.lzw.blog.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/11:22
 * @Description:
 */
@Entity
@Table(name = "t_tag")
public class Tag implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "标签名称不能空哟!")
	private String name;

	@ManyToMany(mappedBy = "tags")
	private List<Blog> blogs=new ArrayList<>();

	public Tag() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	@Override
	public String toString() {
		return "Tag{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}

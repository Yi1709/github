package com.lzw.blog.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/11:20
 * @Description:
 */
@Entity
@Table(name = "t_type")
public class Type implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "分类名称不能为空!")
	private String name;

	@OneToMany(mappedBy = "type")
	private List<Blog> blogs = new ArrayList<>();

	public Type() {
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
		return "Type{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}

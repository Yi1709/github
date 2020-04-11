package com.myshare.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "arcType")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "hander","fieldHandler"})
public class ArcType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer arcTypeId;

	@NotEmpty(message = "资源类型不能为空")
	@Column(length = 200)
	private String arcTypeName;

	@Column(length = 1000)
	private String remark;

	private Integer sort;

	public Integer getArcTypeId() {
		return arcTypeId;
	}

	public void setArcTypeId(Integer arcTypeId) {
		this.arcTypeId = arcTypeId;
	}

	public String getArcTypeName() {
		return arcTypeName;
	}

	public void setArcTypeName(String arcTypeName) {
		this.arcTypeName = arcTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}

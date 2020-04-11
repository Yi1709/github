package com.myshare.code.service;

import com.myshare.code.entity.UserDownload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface UserDownloadService {

	public Integer getCountByUserIdAAndArticleId(Integer userId, Integer articleId);

	/**
	 * 分页查询某个用户下载的所有资源
	 */
	public Page<UserDownload> list(Integer userId, Integer page, Integer pageSize, Sort.Direction direction,
	                               String... properties);

	/**
	 * 统计某个用户下载的资源数
	 */
	public Long getCount(Integer userId);

	/**
	 * 添加或修改
	 */
	public void save(UserDownload userDownload);
}

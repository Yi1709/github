package com.lzw.blog.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/25/22:15
 * @Description:
 */
public class StringUtil {

	/**
	 * 在字符串前后加百分号
	 *
	 * @param str
	 * @return
	 */
	public static String formatLike(String str) {
		if (StringUtils.isNotBlank(str)) {
			return "%" + str + "%";
		} else {
			return null;
		}
	}

	/**
	 * 过滤空格
	 */
	public static List<String> filterWhite(List<String> list) {
		List<String> resultList = new ArrayList<>();
		for (String s : list) {
			if (StringUtils.isNotBlank(s)) {
				resultList.add(s);
			}
		}
		return resultList;
	}
}

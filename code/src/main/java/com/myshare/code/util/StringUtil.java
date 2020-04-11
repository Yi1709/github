package com.myshare.code.util;

import net.bytebuddy.implementation.attribute.AnnotationAppender;

import java.util.Random;

/**
 * 字符串操作工具类
 */
public class StringUtil {

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static boolean notEmpty(String str) {
		if (str != null && !str.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 生成6位随机数
	 */
	public static String getSixRandom() {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	/**
	 * 去除html标签
	 */
	public static String html(String content) {
		//p标签换成行
		content = content.replaceAll("<p . *?>", "\r\n");
		//<br>替换成换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		//去掉其他<>的东西
		content = content.replaceAll("\\<. *?>", "");
		//去掉空格
		content = content.replaceAll("", "");
		return content;
	}

	/**
	 * 转义大于小于
	 */
	public static String esc(String content) {
		return content.replace("<", "&lt;").replace(">", "gt;");
	}

}

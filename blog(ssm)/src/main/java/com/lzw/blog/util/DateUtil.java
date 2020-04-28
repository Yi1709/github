package com.lzw.blog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: lzw
 * @Date: 2020/04/26/22:27
 * @Description:
 */
public class DateUtil {

	/**
	 * 得到当前日期的字符串
	 */
	public static String getCurrentDateStr() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		return simpleDateFormat.format(date);
	}

	/**
	 * 字符转日期
	 */
	public static Date formatString(String str, String pattern) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = simpleDateFormat.parse(str);
		return date;
	}

	/**
	 * 日期转字符
	 */
	public static String formatDate(Date date, String pattern) throws ParseException {
		String result = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		if (date != null) {
			result = simpleDateFormat.format(date);
		}

		return result;
	}
}

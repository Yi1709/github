package com.lzw.blog.util;

import com.lzw.blog.pojo.User;
import org.springframework.util.DigestUtils;

/**
 * @Auther: lzw
 * @Date: 2020/04/20/15:26
 * @Description:
 */
public class MD5Util {
	public static String md5(String password) {
		String result = "";
		result = DigestUtils.md5DigestAsHex(password.getBytes());
		return result;
	}

	public static void main(String[] args) {
		String password = "123456";
		System.out.println(MD5Util.md5(password));
	}
}

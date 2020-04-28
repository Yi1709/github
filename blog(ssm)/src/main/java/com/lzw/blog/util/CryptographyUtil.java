package com.lzw.blog.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/14:19
 * @Description: md5加密
 */
public class CryptographyUtil {

	public static String salt = "lzw";

	public static String md5(String str, String salt) {
		String result = new Md5Hash(str, salt).toString();
		return result;
	}

	public static void main(String[] args) {
		String password = "123456";
		System.out.println(md5(password, salt));
	}
}

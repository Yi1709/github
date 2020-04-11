package com.myshare.code.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密工具类
 */
public class CryptographyUtil {
	public final static String SALT = "code";

	public static String md5(String str, String salt) {
		String password = new Md5Hash(str, salt).toString();
		return password;
	}

	public static void main(String[] args) {
		String password="123456";
		System.out.println(CryptographyUtil.md5(password,SALT ));
	}

}

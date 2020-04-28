package com.lzw.blog.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: lzw
 * @Date: 2020/04/24/16:55
 * @Description: 写入response的工具类
 */
public class ResponseUtil {

	public static void write(HttpServletResponse response, Object o) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		printWriter.println(o.toString());
		printWriter.flush();
		printWriter.close();
	}
}

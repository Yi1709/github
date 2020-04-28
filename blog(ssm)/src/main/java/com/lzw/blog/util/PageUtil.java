package com.lzw.blog.util;

/**
 * @Auther: lzw
 * @Date: 2020/04/27/17:33
 * @Description:
 */
public class PageUtil {
	/*		<li>
	            <a href="/index.html?page=1&">首页</a>
                <li class="disabled"><a href="#">上一页</a></li>
                <li class="active"><a href="#">1</a></li>
                <li class="disabled"><a href="#">下一页</a></li>
                <li><a href="/index.html?page=1&">尾页</a>
             </li>     */

	/**
	 * 拼接分页代码
	 */
	public static String getPagination(String targetUrl, long totalNum, int currentPage, int pageSize, String param) {
		//总共页数
		if (totalNum == 0) {
			return "未查询到数据";
		}
		Long totalPage = totalNum % pageSize == 0 ?(totalNum / pageSize):(totalNum / pageSize)+1L;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li ><a href='" + targetUrl + "?page=1&" + param + "'>首页</a>");
		if (currentPage > 1) {  //当前页不是第一页显示上一页
			pageCode.append("<li><a href='" + targetUrl + "?page=" + (currentPage - 1) + "&" + param + "'>上一页</a" +
					"></li" +
					">");
		} else {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}
		//显示页数
		for (int i = 1; i <= totalPage; i++) {
			if (i == currentPage) {
				pageCode.append("<li class='active'><a href='" + targetUrl + "?page=" + i + "&" + param + "'>" + i +
						"</a></li>");
			} else {
				pageCode.append("<li><a href='" + targetUrl + "?page=" + i + "&" + param + "'>" + i + "</a></li>");
			}
		}
		//下一页
		if (currentPage < totalPage) {  //当前页不是最后一页显示下一页
			pageCode.append("<li><a href='" + targetUrl + "?page=" + (currentPage + 1) + "&" + param + "'>下一页</a" +
					"></li" +
					">");
		} else {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		}

		pageCode.append("<li><a href='" + targetUrl + "?page=" + totalPage + "&" + param + "'>尾页</a></li>");

		/*<li class="active"><a href="#">1</a></li>
                <li class="disabled"><a href="#">下一页</a></li>
                <li><a href="/index.html?page=1&">尾页</a>*/
		return pageCode.toString();
	}
}

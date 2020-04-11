package com.myshare.code.util;

import com.myshare.code.entity.ArcType;
import com.myshare.code.entity.Comment;
import jdk.internal.dynalink.beans.StaticClass;

import java.util.List;

/**
 * HTML
 * 工具类
 */
public class HTMLUtil {
	/**
	 * 拼接类型列表的编码
	 *
	 * @param type        点击眼色不同
	 * @param arcTypeList
	 * @return
	 */
	public static String getArcTypeStr(String type, List<ArcType> arcTypeList) {
		StringBuffer arcTypeCode = new StringBuffer();
		if ("all".equals(type)) { //判断当前类型是不是首页
			arcTypeCode.append("<li class=\"layui-hide-xs layui-this\"><a href=\"/\">首页</a></li>");
		} else {
			arcTypeCode.append("<li><a href=\"/\">首页</a></li>");
		}

		for (ArcType arcType : arcTypeList) {
			if (type.equals(arcType.getArcTypeId().toString())) {
				arcTypeCode.append("<li class=\"layui-hide-xs layui-this\"><a href=\"/article/" +
						arcType.getArcTypeId() + "/1" + "\">" + arcType.getArcTypeName() + "</a></li>");
			} else {
				arcTypeCode.append("<li><a href=\"/article/" +
						arcType.getArcTypeId() + "/1" + "\">" + arcType.getArcTypeName() + "</a></li>");
			}
		}
		return arcTypeCode.toString();
	}

	/**
	 * 拼接分页代码
	 *
	 * @param targetUrl   请求路径
	 * @param count       总记录数
	 * @param currentPage 当前页
	 * @param msg         没查到时提示的信息
	 * @return
	 */
	public static String getPagetion(String targetUrl, int count, int currentPage, String msg) {
		//总页数
		int totalPage = count % Consts.PAGE_SIZE == 0 ? count / Consts.PAGE_SIZE : (count / Consts.PAGE_SIZE) + 1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<div class=\"laypage-main\">");
		if (totalPage > 0) {
			pageCode.append("<a href=\"" + targetUrl + "/1\">首页</a>");
		}
		if (currentPage != 1) {
			pageCode.append
					("<a style=\"display: inline-block;\" href=\"" + targetUrl + "/" + (currentPage - 1) + "\">上一页</a" +
							">");
		}
		for (int i = currentPage - 3; i <= currentPage + 3; i++) {
			if (i < 1 || i > totalPage) {
				continue;
			}
			if (i == currentPage) {
				pageCode.append("<span class=\"laypage-curr\">" + i + "</span>");
			} else {
				pageCode.append("<a href=\"" + targetUrl + "/" + i + "\">" + i + "</a>");
			}
		}
		if (currentPage < totalPage) {
			pageCode.append
					("<a style=\"display: inline-block;\" href=\"" + targetUrl + "/" + (currentPage + 1) + "\">下一页</a" +
							">");
		}
		if (totalPage > 0) {
			pageCode.append("<a href=\"" + targetUrl + "/" + totalPage + "\">尾页</a>");
		} else {
			pageCode.append("<span>" + msg + "</span>");
		}
		pageCode.append("</div>");
		return pageCode.toString();
	}

	/**
	 * lucene查询分页代码
	 *
	 * @param targetUrl   请求路径
	 * @param count       总记录数
	 * @param currentPage 当前页
	 * @param msg         没查到时提示的信息
	 * @return
	 */
	public static String getPagetion2(String targetUrl, int count, int currentPage, String msg) {
		int totalPage = count % Consts.PAGE_SIZE == 0 ? (count / Consts.PAGE_SIZE) : (count / Consts.PAGE_SIZE) + 1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<div class=\"laypage-main\">");
		if (totalPage > 0) {
			pageCode.append("<a href=\"" + targetUrl + "\">首页</a>");
		}
		if (totalPage != 1) {
			pageCode.append
					("<a style=\"display:inline-block;\" href=\"" + targetUrl + "&page=" + (currentPage - 1) + "\">上一页</a>");
		}

		for (int i = currentPage - 3; i < currentPage + 3; i++) {
			if (i < 1 || i > totalPage) {
				continue;
			}
			if (i == currentPage) {
				pageCode.append("<span class=\"laypage-curr\">" + i + "</span>");
			} else {
				pageCode.append("<a href=\"" + targetUrl + "&page=" + i + "\">" + i + "</a>");
			}
		}

		if (currentPage < totalPage) {
			pageCode.append
					("<a style=\"display:inline-block;\" href=\"" + targetUrl + "&page=" + (currentPage + 1) + "\">下一页</a>");
		}
		if (totalPage > 0) {
			pageCode.append("<a href =\" " + targetUrl + "&page=" + totalPage + "\">尾页</a>");
		} else {
			pageCode.append("<span>" + msg + "</span> ");
		}

		pageCode.append("</div>");

		return pageCode.toString();
	}

	/**
	 * 拼接评论的代码
	 */
	public static String commentPageStr(List<Comment> commentList) {
		StringBuffer commentCode = new StringBuffer();
		if (commentList == null || commentList.size() == 0) {
			return "";
		}
		for (Comment comment : commentList) {
			commentCode.append("<li class=\" jieda-daan\">\n" +
					"<div class = \" detail-about detail-about-reply\">\n" +
					"<a class =\"fly-avatar\" href=\"\" >\n" +
					"<img src=\"/static/img/" + comment.getUser().getHeadPortrait() + "\" alt=\"" + comment.getUser()
					.getNickname() + "\" />\n" +
					"          </a>\n"+
					"    <div class=\"fly-detail-user\">\n"+
					"            <a  href=''class='fly-link'>\n"+
					"              <cite>"+comment.getUser().getNickname()+"</cite>\n");
			if (comment.getUser().isVip()) {
				commentCode.append("<i class='iconfont icon-renzheng' title='会员认证'></i>\n" +
						"<i class='layui-badge fly-badge-vip'>VIP" + comment.getUser().getVipGrade() + "</i>\n");
			}
			commentCode.append("</a>\n");
			//评论者和作者为一个人
			if (comment.getUser().getUserId() == comment.getArticle().getUser().getUserId()) {
				commentCode.append("<span>(作者)</span>\n");
			}
			commentCode.append("</div>\n\n");
			commentCode.append("<div class ='detail-hits'>\n" +
					" <span>" + DateUtil.formatDate(comment.getCommentDate(), "yyyy-MM-dd HH:mm") +
					"</span>\n" +
					"</div>\n" +
					"</div>\n");
			commentCode.append("<div class='detail-body jieda-body photos'>\n" +
					"<p>" + comment.getContent() + "</p>\n" +
					"</div>\n" +
					"</li>");
		}
		return commentCode.toString();
	}
}

















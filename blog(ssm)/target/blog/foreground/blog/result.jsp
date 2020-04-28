<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="data_list">
    <div class="data_list_title">
        <h4 style="text-align:center;"><img
                src="${pageContext.request.contextPath}/static/images/search_icon.png">搜索&nbsp;<font
                color="red">${q}</font>&nbsp;的结果&nbsp;(总共搜索到&nbsp;${resultTotal}&nbsp;条记录)
        </h4>
    </div>
    <div class="datas">
        <ul>
            <c:choose>
                <c:when test="${blogList.size()==0}">
                    <div align="center" style="padding-top: 20px;">
                        未查询到结果,请换个关键字看看
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="blog" items="${blogList}">
                        <li style="padding-top: 20px;">
                             <h3>
                            	<a href="${pageContext.request.contextPath}/blog/articles/${blog.id}.html"
                                   target="_blank">${blog.title}</a>
                            </h3>
                            <h5 >摘要:${blog.content}...</h5>
                           <span>发布日期:${blog.releaseDateStr}</span>
                        </li>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
    ${pageCode}
</div>

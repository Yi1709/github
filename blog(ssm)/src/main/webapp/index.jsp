<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap3/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/blog.css">
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/jquery-1.11.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/bootstrap3/js/bootstrap.min.js"></script>

    <style type="text/css">
        body {
            padding-top: 10px;
            padding-bottom: 40px;
            background-color: #F8F8FF;
            font-family: microsoft yahei;
        }

        .container {
            width: 1200px;
        }
    </style>
</head>

<body>
<div class="container">
    <jsp:include page="foreground/common/head.jsp"></jsp:include>
    <jsp:include page="foreground/common/menu.jsp"></jsp:include>

    <div class="row">
        <div class="col-md-9">
            <jsp:include page="${mainPage}"></jsp:include>
        </div>

        <div class="col-md-3">
            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/user_icon.png"/>
                    博主信息
                </div>
                <div class="user_image">
                    <img src="${pageContext.request.contextPath}/static/userImages/${blogger.imageName}"/>
                </div>
                <div class="nickName"><span>昵称:</span>${blogger.nickName}</div>
                <div class="userSign"><span>个人简介:</span>${blogger.sign}</div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/byType_icon.png"/>
                    日志类别
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="blogTypeCount" items="${blogTypeCountList}">
                            <li><span><a href="
                            ${pageContext.request.contextPath}/index.html?typeId=${blogTypeCount.id}">
                                    ${blogTypeCount.typeName}(
                                    ${blogTypeCount.blogCount})
                            </a>
                            </span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/byDate_icon.png"/>
                    文章日期
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach var="blogCount" items="${blogCountList}">
                            <li><span><a href="${pageContext.request.contextPath}/index.html?releaseDateStr
                            =${blogCount.releaseDateStr}
">${blogCount.releaseDateStr}(${blogCount.blogCount})</a>
                            </span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/static/images/link_icon.png"/>
                    友情链接
                </div>
                <div class="datas">
                    <ul>
                        <c:forEach items="${linkList }" var="link">
                            <li><span><a href="${link.linkUrl }" target="_blank">${link.linkName }</a></span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="foreground/common/footer.jsp"/>
</div>
</body>
</html>
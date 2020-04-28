<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/static/images/blog_show_icon.png"/>&nbsp;博客信息
    </div>
    <div>
        <div class="blog_title"><h3><strong>${blog.title}</strong></h3></div>
        <div style="padding-left: 370px;padding-bottom: 20px;padding-top: 10px;">
            <div class="bshare-custom">
                <a title="分享到qq空间" class="bshare-qzone"></a>
                <a title="分享到微信" class="bshare-weixin"></a>
                <a title="分享到新浪微博" class="bshare-sinaminiblog"></a>
                <a title="分享给腾讯朋友" class="bshare-qqxiaoyou"></a>
                <a title="更多平台" class="bshare-more bshare-more-icon more-style-addthis"></a>
                <script type="text/javascript" charset="UTF-8"
                        src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=&amp;lang=zh"></script>
                <script type="text/javascript" charset="UTF-8" src="http://static.bshare.cn/b/bshareC0.js"></script>
            </div>
        </div>
        <div style="text-align: center" class="blog_info">
            发布时间:【<fmt:formatDate value="${blog.releaseDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>】
            &nbsp;&nbsp;博客类型:${blog.blogType.typeName}&nbsp;&nbsp;阅读量:${blog.clickHit}&nbsp;&nbsp;阅读量:${blog.replyHit}
        </div>
        <hr>
        <div id="article" class="blog_content">
            ${blog.content}
        </div>

        <hr>
        <div>
            <font><strong>关键字:</strong></font>
            <c:choose>
                <c:when test="${blog.keyWord}==null">
                    &nbsp;&nbsp;无
                </c:when>
                <c:otherwise>
                    <form id="queryForm" action="${pageContext.request.contextPath}/blog/q.html" method="post">
                        <input type="hidden" id="q1" name="q"/>
                        <c:forEach var="keyWord" items="${keyWords}">
                            <a href="javascript:query1('${keyWord}')" target="_blank">${keyWord}</a> &nbsp;&nbsp;
                        </c:forEach>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="blog_content">
            ${pageCode}
        </div>
    </div>
</div>

<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/static/images/comment_icon.png">
        评论信息
        <c:if test="${commentList.size()>10}">
            <a href="javascript:showOtherComment();" style="float: right;padding-right: 40px;">显示所有评论</a>
        </c:if>
    </div>
    <div class="commentDatas">
        <c:choose>
            <c:when test="${commentList.size()==0}">暂无评论</c:when>
            <c:otherwise>
                <c:forEach var="comment" items="${commentList}" varStatus="status">
                    <c:choose>
                        <c:when test="${status.index<10}">
                            <div class="comment">
                                 <span>
                                   <font> ${status.index+1}楼&nbsp;&nbsp;&nbsp;&nbsp;
                                     ${comment.userIp}:&nbsp;&nbsp;</font>
                                     ${comment.content}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                     <fmt:formatDate value="${comment.commentDate}" type="date"
                                                     pattern="yyyy-MM-dd HH:mm"/>
                                 </span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="otherComment">
                                <div class="comment">
                                    <span>
                                        <font> ${status.index+1}楼&nbsp;&nbsp;&nbsp;&nbsp;
                                     ${comment.userIp}:&nbsp;&nbsp;</font>
                                     ${comment.content}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                     <fmt:formatDate value="${comment.commentDate}" type="date"
                                                     pattern="yyyy-MM-dd HH:mm"/>
                                    </span>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/static/images/publish_comment_icon.png">
        发表评论
    </div>
    <div class="publish_comment">
        <div>
            <textarea rows="3" style="width: 100%" id="content" name="content" placeholder="不说两句吗?"></textarea>
        </div>
        <div class="verCode">
            验证码:<input type="text" name="imageCode" id="imageCode" size="10" onkeydown="if (Event.keyCode==13)
            submitData()"/>
            &nbsp;<img src="${pageContext.request.contextPath}/image.jsp" name="randomImage" id="randomImage"
                       title="换一张试试" onclick="javascript:loadImage();" width="60" height="20" border="1"
                       align="absmiddle">
            <a href="javascript:loadImage();">换一张试试??</a>
        </div>

        <div class="publishButton">
            <button class="btn btn-primary" type="button" onclick="submitData()">发表评论</button>
        </div>
    </div>

</div>

<script type="text/javascript">
    function showOtherComment() {
        $(".otherComment").show();
    }

    function loadImage() {
        $("#randomImage").attr("src", '${pageContext.request.contextPath}/image.jsp?' + Math.random());
    }

    function submitData() {
        var content = $("#content").val();
        var imageCode = $("#imageCode").val();
        if (content == null || content == '') {
            alert("请填写评论内容!");
        } else if (imageCode == null || imageCode == '') {
            alert("请填写验证码!");
        } else {
            $.post('${pageContext.request.contextPath}/comment/save.do', {
                'content': content,
                'imageCode': imageCode,
                'blog.id':${blog.id}
            }, function (result) {
                if (result.success) {
                    window.location.reload();
                    alert("评论已成功提交，审核通过后显示");
                } else {
                    alert(result.errorInfo);
                }
            }, "json");
        }
    }

    function query1(keyword) {
        $("#q1").val(keyword);
        $("#queryForm").submit();
    }
</script>
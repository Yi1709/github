<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>评论审核页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0//themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
</head>

<body style="margin: 1px; font-family: 'microsoft yahei'">
<table id="dg" title="评论审核" class="easyui-datagrid" fitColumns="true" pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/admin/comment/list.do?state=0" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="20" align="center">编号</th>
        <th field="blog" width="200" align="center" formatter="formatBlogTitle">博客标题</th>
        <th field="userIp" width="100" align="center">用户IP</th>
        <th field="content" width="200" align="center">评论内容</th>
        <th field="commentDate" width="70" align="center">评论时间</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a href="javascript:commentReview(1);" class="easyui-linkbutton" iconCls="icon-ok">审核通过</a>
    <a href="javascript:commentReview(2);" class="easyui-linkbutton" iconCls="icon-no">审核不通过</a>
</div>
<script type="text/javascript">
    function formatBlogTitle(val, row) {
        return "<a target='_blank' href='${pageContext.request.contextPath}/blog/articles/" + val.id + ".html'>" +
            val.title + "</a>";
    }


    function commentReview(state) {
        var selectionRows = $("#dg").datagrid("getSelections");
        if (selectionRows.length == 0) {
            $.messager.alert("系统提示", "请选择您要审核的评论!");
            return;
        }
        var idsStr = [];
        for (var i = 0; i < selectionRows.length; i++) {
            idsStr.push(selectionRows[i].id);
        }
        var ids = idsStr.join(",");
        $.messager.confirm("系统提示", "您确定要审核这<font color='red' '>" + selectionRows.length + "</font>条评论吗?", function (r) {
            if (r) {
                $.post("${pageContext.request.contextPath}/admin/comment/review.do", {ids: ids,state:state}, function
                    (result) {
                    if (result) {
                        $.messager.alert("系统提示", "评论审核成功!");
                        $("#dg").datagrid("reload");
                    } else {
                        $.messager.alert("系统提示", "评论审核失败!");
                    }
                }, "json");
            }
        });

    }
</script>
</body>
</html>

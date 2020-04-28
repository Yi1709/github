<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>评论管理页面</title>
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
<table id="dg" title="评论管理" class="easyui-datagrid" fitColumns="true" pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/admin/comment/list.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="20" align="center">编号</th>
        <th field="blog" width="200" align="center" formatter="formatBlogTitle">博客标题</th>
        <th field="userIp" width="100" align="center">用户IP</th>
        <th field="content" width="200" align="center">评论内容</th>
        <th field="commentDate" width="70" align="center">评论时间</th>
        <th field="state" width="70" align="center" formatter="formatState">评论状态</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a href="javascript:deleteComment();" class="easyui-linkbutton" iconCls="icon-remove">删除</a>
</div>
<script type="text/javascript">
    function formatBlogTitle(val, row) {
        return "<a target='_blank' href='${pageContext.request.contextPath}/blog/articles/" + val.id + ".html'>" +
            val.title + "</a>";
    }

    function formatState(val, row) {
        if (val == 0) {
            return "待审核";
        } else if (val == 1) {
            return "审核通过";
        } else {
            return "审核不通过";
        }
    }

    function deleteComment() {
        var selectionRows = $("#dg").datagrid("getSelections");
        if (selectionRows.length == 0) {
            $.messager.alert("系统提示", "请选择您要删除的评论!");
            return;
        }
        var idsStr = [];
        for (var i = 0; i < selectionRows.length; i++) {
            idsStr.push(selectionRows[i].id);
        }
        var ids = idsStr.join(",");
        $.messager.confirm("系统提示", "您确定要删除这<font color='red' '>" + selectionRows.length + "</font>条评论吗?", function (r) {
            if (r) {
                $.post("${pageContext.request.contextPath}/admin/comment/delete.do", {ids: ids}, function (result) {
                    if (result) {
                        $.messager.alert("系统提示", "评论删除成功!");
                        $("#dg").datagrid("reload");
                    } else {
                        $.messager.alert("系统提示", "评论删除失败!");
                    }
                }, "json");
            }
        });
    }

</script>
</body>
</html>

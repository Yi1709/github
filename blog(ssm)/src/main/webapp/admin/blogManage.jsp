<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>博客管理页面</title>
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
<table id="dg" title="博客管理" class="easyui-datagrid" fitcolumns="true" pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/admin/blog/list.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="20px;" align="center">编号</th>
        <th field="title" width="200px;" align="center" formatter="formatTitle">标题</th>
        <th field="releaseDate" width="50px;" align="center">日期</th>
        <th field="blogType" width="50px;" align="center" formatter="formatBlogType">博客类别</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openBlogModifyTab()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
        <a href="javascript:deleteBlog()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
    </div>
    <div>
        &nbsp;标题:&nbsp;<input type="text" id="s_title" size="20" onkeydown="if (Event.keyCode=13)searchBlog()">
        <a href="javascript:searchBlog();" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>
<script type="text/javascript">
    function formatBlogType(val, row) {
        return val.typeName;
    }

    function searchBlog() {
        $("#dg").datagrid("load", {"title": $("#s_title").val()});
    }

    function openBlogModifyTab() {
        var selectionRows = $('#dg').datagrid("getSelections");
        if (selectionRows.length != 1) {
            $.messager.alert("系统提示", "请选择一条您需要修改的数据!");
            return;
        } else {
            var row = selectionRows[0];
            window.parent.openTab("修改博客", "modifyBlog.jsp?id=" + row.id, "icon-writeblog");
        }
    }

    function deleteBlog() {
        var selectionRows = $("#dg").datagrid("getSelections");
        if (selectionRows.length == 0) {
            $.messager.alert("系统提示", "请选择您要删除的数据!");
            return;
        }
        var idsStr = [];
        for (let i = 0; i < selectionRows.length; i++) {
            idsStr.push(selectionRows[i].id);
        }
        var ids = idsStr.join(",");
        $.messager.confirm("系统提示", "您确定要删除这<font color='red'>" + selectionRows.length + "</font>条数据吗?", function (r) {
            if (r) {
                $.post("${pageContext.request.contextPath}/admin/blog/delete.do", {ids: ids}, function (result) {
                    if (result) {
                        $.messager.alert("系统提示", "删除成功!");
                        $("#dg").datagrid("reload");
                    } else {
                        $.messager.alert("系统提示", "删除失败!");
                    }
                }, "json");
            }
        });
    }

    function formatTitle(val, row) {
        return "<a target='_blank' href='${pageContext.request.contextPath}/blog/articles/" + row.id + ".html'>" + val
            + "</a>";
    }
</script>
</body>
</html>

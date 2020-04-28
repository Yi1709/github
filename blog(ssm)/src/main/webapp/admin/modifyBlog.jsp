<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>写博客页面</title>
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
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/ueditor1_4_3_3/ueditor.config.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/ueditor1_4_3_3/ueditor.all.min.js"></script>
    <!--加载语言-->
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/ueditor1_4_3_3/lang/zh-cn/zh-cn.js"></script>
</head>
<body style="margin: 10px; font-family: 'microsoft yahei'">
<div id="p" class="easyui-panel" title="修改博客" style="padding: 10px;">
    <table cellspacing="20px">
        <tr>
            <td width="80px">博客标题:</td>
            <td><input type="text" id="title" name="title" style="width: 400px;"/></td>
        </tr>
        <tr>
            <td>所属类别:</td>
            <td>
                <select class="easyui-combobox" id="blogTypeId" name="blogType.id" style="width: 150px;"
                        editable="false" panelHeight="auto">
                    <option value="">请选择博客类别</option>
                    <c:forEach var="blogType" items="${blogTypeCountList}">
                        <option value="${blogType.id}">${blogType.typeName}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>博客内容:</td>
            <td>
                <script type="text/plain" id="editor" style="width: 100%;height: 500px;"></script>
            </td>
        </tr>
        <tr>
            <td>关键字:</td>
            <td><input type="text" id="keyWord" name="keyWord" style="width: 400px;"/>&nbsp;(关键字多以空格隔开)</td>
        </tr>
        <tr>
            <td></td>
            <td><a href="javascript:submitData();" class="easyui-linkbutton"
                   data-options="iconCls:'icon-submit'">发布博客</a></td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    var ue = UE.getEditor("editor");
    ue.addListener("ready", function () {
        //ajax请求数据
        UE.ajax.request("${pageContext.request.contextPath}/admin/blog/findById.do", {
            method: "Post",
            async: false,
            data: {"id": "${param.id}"},
            onsuccess: function (result) {
                result = eval("(" + result.responseText + ")");
                $("#title").val(result.title);
                $("#keyWord").val(result.keyWord);
                $("#blogTypeId").combobox("setValue", result.blogType.id);
                UE.getEditor("editor").setContent(result.content);
            }
        });
    });

</script>
<script type="text/javascript">
    function submitData() {
        var title = $('#title').val();
        var blogTypeId = $('#blogTypeId').combobox("getValue");
        var content = UE.getEditor("editor").getContent();
        var summary = UE.getEditor("editor").getContentTxt().substr(0, 155);
        var keyWord = $('#keyWord').val();

        if (title == null || title == '') {
            $.messager.alert("系统提示", "请输入博客标题!");
        } else if (blogTypeId == null || blogTypeId == '') {
            $.messager.alert("系统提示", "请输入博客类型!");
        } else if (content == null || content == '') {
            $.messager.alert("系统提示", "请输入博客内容!");
        } else if (keyWord == null || keyWord == '') {
            $.messager.alert("系统提示", "请输入博客内容!");
        } else {
            $.post("${pageContext.request.contextPath}/admin/blog/save.do", {
                'id': '${param.id}',
                'title': title,
                'blogType.id': blogTypeId,
                'content': content,
                'contentNoTag': UE.getEditor("editor").getContentTxt(),
                'summary': summary,
                'keyWord': keyWord
            }, function (result) {
                if (result) {
                    $.messager.alert("系统提示", "发布成功!");
                } else {
                    $.messager.alert("系统提示", "发布失败!");
                }
            }, "json");
        }
    }
</script>
</body>
</html>

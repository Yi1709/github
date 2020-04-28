<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>修改个人信息页面</title>
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
<div id="p" class="easyui-panel" title="修改个人信息" style="padding: 10px">
    <form id="form1" action="${pageContext.request.contextPath}/admin/blogger/save.do" method="post"
          enctype="multipart/form-data">
        <input type="hidden" id="id" name="id" value="${currentUser.id}">
        <input type="hidden" id="profile" name="profile">
        <table cellspacing="20px">
            <tr>
                <td width="80px">用户名</td>
                <td><input type="text" id="userName" name="userName" style="width:200px;"
                           value="${currentUser.userName}" readonly="readonly"/></td>
            </tr>
            <tr>
                <td width="80px">昵称</td>
                <td><input type="text" id="nickName" name="nickName" style="width:200px;"
                           value="${currentUser.nickName}"/></td>
            </tr>
            <tr>
                <td width="80px">个性签名</td>
                <td><input type="text" id="sign" name="sign" style="width:400px;" value="${currentUser.sign}"/></td>
            </tr>
            <tr>
                <td width="80px">个人头像</td>
                <td><input type="file" id="imageFile" name="imageFile" style="width:400px;"/></td>
            </tr>
            <tr>
                <td>个人简介</td>
                <td>
                    <script type="text/plain" id="editor" style="width:100%;height:500px;"></script>
                </td>
            </tr>

            <tr>
                <td></td>
                <td><a href="javascript:submitData()" class="easyui-linkbutton" data-options="iconCls:'icon-submit'">提交修改</a>
                </td>
            </tr>

        </table>
    </form>
</div>
<script type="text/javascript">
    var ue = UE.getEditor("editor");
    ue.addListener("ready", function () {
        UE.ajax.request("${pageContext.request.contextPath}/admin/blogger/find.do", {
            method: "post",
            async: false,
            data: {},
            onsuccess: function (result) {
                result = eval("(" + result.responseText + ")");
                UE.getEditor("editor").setContent(result.profile);
            }
        });
    });


</script>

<script type="text/javascript">
    //个人信息提交
    function submitData() {
        //取值
        var nickName = $("#nickName").val();
        var sign = $("#sign").val();
        var profile = UE.getEditor("editor").getContent();
        if (nickName == null || nickName == "") {
            $.messager.alert("系统提示", "请输入昵称");
        } else if (sign == null || sign == "") {
            $.messager.alert("系统提示", "请输入个性签名");
        } else if (profile == null || profile == "") {
            $.messager.alert("系统提示", "请输入个人简介");
        } else {
            $("#profile").val(profile);
            $("#form1").submit();
        }
    }
</script>
</body>
</html>

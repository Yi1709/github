<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Lzw的博客后台管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
    <style type="text/css">
        body {
            font-family: 'microsoft yahei';
        }
    </style>
</head>

<body class="easyui-layout">
<div region="north" style="height: 78px; background-color: #E0ECFF">
    <table style="padding: 5px" width="100%">
        <tr>
            <td width="50%">
                <h2>Lzw的博客后台管理</h2>
            </td>
            <td valign="bottom" align="right" width="50%">
                <font size="3">&nbsp;&nbsp;<strong>欢迎：</strong>${currentUser.userName }</font>
            </td>
        </tr>
    </table>
</div>
<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <div title="首页" data-options="iconCls:'icon-home'">
            <div align="center" style="padding-top: 100px"><font color="red" size="10">欢迎使用Lzw的博客后台管理</font></div>
        </div>
    </div>
</div>
<div region="west" style="width: 200px" title="导航菜单" split="true">
    <div class="easyui-accordion" data-options="fit:true,border:false">
        <div title="常用操作" data-options="selected:true,iconCls:'icon-item'" style="padding: 10px">
            <a href="javascript:openTab('写博客','writeBlog.jsp','icon-writeblog')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-writeblog'" style="width: 150px">写博客</a>
            <a href="javascript:openTab('评论审核','commentReview.jsp','icon-review')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-review'" style="width: 150px">评论审核</a>
        </div>
        <div title="博客管理" data-options="iconCls:'icon-bkgl'" style="padding:10px;">
            <a href="javascript:openTab('写博客','writeBlog.jsp','icon-writeblog')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-writeblog'" style="width: 150px;">写博客</a>
            <a href="javascript:openTab('博客信息管理','blogManage.jsp','icon-bkgl')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-bkgl'" style="width: 150px;">博客信息管理</a>
        </div>
        <div title="博客类别管理" data-options="iconCls:'icon-bklb'" style="padding:10px">
            <a href="javascript:openTab('博客类别信息管理','blogTypeManage.jsp','icon-bklb')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-bklb'" style="width: 150px;">博客类别信息管理</a>
        </div>
        <div title="评论管理" data-options="iconCls:'icon-plgl'" style="padding:10px">
            <a href="javascript:openTab('评论审核','commentReview.jsp','icon-review')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-review'" style="width: 150px">评论审核</a>
            <a href="javascript:openTab('评论信息管理','commentManage.jsp','icon-plgl')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-plgl'" style="width: 150px;">评论信息管理</a>
        </div>
        <div title="个人信息管理" data-options="iconCls:'icon-grxx'" style="padding:10px">
            <a href="javascript:openTab('修改个人信息','modifyInfo.jsp','icon-grxxxg')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-grxxxg'" style="width: 150px;">修改个人信息</a>
        </div>
        <div title="系统管理" data-options="iconCls:'icon-system'" style="padding:10px">
            <a href="javascript:openTab('友情链接管理','linkManage.jsp','icon-link')" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-link'" style="width: 150px">友情链接管理</a>
            <a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
            <a href="javascript:refreshSystem()" class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-refresh'" style="width: 150px;">刷新系统缓存</a>
            <a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'"
               style="width: 150px;">安全退出</a>
        </div>
    </div>
</div>
<div region="south" style="height: 25px;padding: 5px" align="center">
    Copyright ©2019 Johnny的个人博客系统 版权所有:)
</div>
<div id="dlg" class="easyui-dialog" style="width: 400px;height: 200px;padding: 10px 20px;" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>用户名:</td>
                <td><input type="text" id="userName" name="userName" readonly value="${currentUser.userName} "
                           style="width: 200px;"></td>
            </tr>
            <tr>
                <td>新密码:</td>
                <td><input type="text" id="newPassword" name="newPassword" class="easyui-validatebox" required="true"
                           style="width: 200px;"></td>
            </tr>
            <tr>
                <td>新密码:</td>
                <td><input type="text" id="newPassword2" name="newPassword2" class="easyui-validatebox" required="true"
                           style="width: 200px;"></td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:updatePassword();" class="easyui-linkbutton" iconCls="icon-ok"  >修改</a>
    <a href="javascript:closePassword();" class="easyui-linkbutton" iconCls="icon-cancel" >关闭</a>
</div>

<script type="text/javascript">
    var url;

    function openTab(text, url, iconCls) {
        if ($("#tabs").tabs("exists", text)) {
            $("#tabs").tabs("select", text);
        } else {
            var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/admin/" + url + "'></iframe>";
            $("#tabs").tabs("add", {
                title: text,
                iconCls: iconCls,
                closable: true,
                content: content
            });
        }
    }

    function refreshSystem() {
        $.post("${pageContext.request.contextPath}/admin/system/refreshSystem.do", {}, function (result) {
            if (result) {
                $.messager.alert("系统提示", "刷新系统缓存成功!");
            }
        }, "json");
    }

    function openPasswordModifyDialog() {
        $("#dlg").dialog("open").dialog("setTitle", "修改密码");
        url = "${pageContext.request.contextPath}/admin/blogger/updatePassword.do?id=${currentUser.id}";
    }

    function updatePassword() {
        $("#fm").form("submit", {
            url: url,
            onSubmit: function () {
                var newPassword = $("#newPassword").val();
                var newPassword2 = $("#newPassword2").val();
                if (!$(this).form("validate")) {
                    return false;
                }
                if (newPassword != newPassword2) {
                    $.messager.alert("系统提示", "两次输入的密码不一致，请重新输入!");
                    return false;
                }
                return true;
            },
            success: function (result) {
                var result = eval("(" + result + ")");
                if (result) {
                    $.messager.alert("系统提示", "修改密码成功!,下次登录生效");
                    closePassword();
                } else {
                    $.messager.alert("系统提示", "修改密码失败!");
                    return;
                }
            }
        });
    }

    function resetValue() {
        $("#newPassword").val('');
        $("#newPassword2").val('');
    }

    function closePassword() {
        resetValue();
        $("#dlg").dialog("close");
    }

    function logout() {
        $.messager.confirm("系统提示","您确定要退出系统吗?",function (r) {
            if (r){
                window.location.href="${pageContext.request.contextPath}/admin/blogger/logout.do";
            }
        })
    }
</script>
</body>
</html>

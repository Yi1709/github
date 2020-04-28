<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>友情链接管理页面</title>
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
<body style="margin: 1px;font-family: 'Microsoft YaHei'">
<table id="dg" title="友情链接管理" class="easyui-datagrid" fitcolumns="true" pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/admin/link/list.do" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="20" align="center">编号</th>
        <th field="linkName" width="100" align="center">友情链接名称</th>
        <th field="linkUrl" width="100" align="center">友情链接地址</th>
        <th field="orderNo" width="100" align="center">排序序号</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a href="javascript:openLinkAdd();" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    <a href="javascript:openLinkModify();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
    <a href="javascript:deleteLink();" class="easyui-linkbutton" iconCls="icon-edit" plain="true">删除</a>
</div>
<div id="dlg" class="easyui-dialog" style="width: 500px;height: 200px;padding: 10px 20px;" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" action="#" method="post">
        <table cellspacing="8px">
            <tr>
                <td>友情链接名称:</td>
                <td><input type="text" id="linkName" name="linkName" class="easyui-validatebox" required></td>
            </tr>
            <tr>
                <td>友情链接地址:</td>
                <td><input type="text" id="linkUrl" name="linkUrl" class="easyui-validatebox" required></td>
            </tr>
            <tr>
                <td>友情链接排序:</td>
                <td><input type="text" id="orderNo" name="orderNo" class="easyui-validatebox" required
                           style="width: 80px;">&nbsp;&nbsp;(类别序号需从小到大)
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveLink();" class="easyui-linkbutton" iconCls="icon-ok">添加</a>
    <a href="javascript:closeLinkDialog()" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
</div>


<script type="text/javascript">
    var url;

    function openLinkAdd() {
        $("#dlg").dialog("open").dialog("setTitle", "添加友情链接信息");
        url = "${pageContext.request.contextPath}/admin/link/save.do";
    }

    //修改
    function openLinkModify() {
        var selectedRows = $('#dg').datagrid("getSelections");
        if (selectedRows.length != 1) {
            $.messager.alert("系统提示", "请选择一条要编辑的数据!");
            return;
        }
        var row = selectedRows[0];
        $("#dlg").dialog("open").dialog("setTitle", "修改友情链接信息");
        $('#fm').form("load", row);
        url = "${pageContext.request.contextPath}/admin/link/save.do?id=" + row.id;
    }

    function saveLink() {
        $("#fm").form("submit", {
            url: url,
            onSubmit: function () {
                return $(this).form("validate");
            },
            success: function (result) {
                var result = eval('(' + result + ')');
                if (result.success) {
                    $.messager.alert("系统提示", "保存成功!");
                    resetValue();
                    //关闭对话框
                    $("#dlg").dialog("close");
                    //刷新查询结果
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "保存失败");
                    return;
                }
            }
        })
    }

    //重置弹出的对话框
    function resetValue() {
        $("#typeName").val("");
        $("#orderNo").val("");
    }

    //关闭对话框
    function closeLinkDialog() {
        $("#dlg").dialog("close");
        resetValue();
    }

    //删除
    function deleteLink(){
        var selectedRows = $("#dg").datagrid("getSelections");
        if(selectedRows.length==0){
            $.messager.alert("系统提示","请至少选择一条要删除的数据");
            return;
        }
        var strIds = [];
        for(var i=0;i<selectedRows.length;i++){
            strIds.push(selectedRows[i].id);
        }
        var ids = strIds.join(",");
        $.messager.confirm("系统提示","您确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗?",function(r){
            if(r){
                $.post("${pageContext.request.contextPath}/admin/link/delete.do",{ids:ids},function(result){
                        if(result.success){
                            $.messager.alert("系统提示","删除成功");
                            $("#dg").datagrid("reload");
                        }else{
                            $.messager.alert("系统提示","删除失败");
                        }
                },"json");
            }
        })
    }

</script>
</body>
</html>

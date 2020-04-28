<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div class="col-md-12">

    <nav class="navbar navbar-default">
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <div class="container-fluid">
                <ul class="nav navbar-nav">
                    <li><a class="navbar-brand" href="${pageContext.request.contextPath}/index.html"
                           onclick="changeClass(this)">博客首页</a></li>
                    <li><a class="navbar-brand"
                           href="${pageContext.request.contextPath}/blogger/aboutMe.html"
                           onclick="changeClass(this)">关于博主</a></li>
                    <li><a class="navbar-brand" href="${pageContext.request.contextPath}/login.jsp"
                           onclick="changeClass(this)">登录后台</a></li>
                </ul>

            <form action="${pageContext.request.contextPath}/blog/q.html" class="navbar-form navbar-right"
                  role="search" method="post" onsubmit="return checkData()">
                <div class="form-group">
                    <input type="text" id="q" name="q" value="${q}" class="form-control" placeholder="请输入要查询的关键字">
                </div>
                <button type="submit" class="btn btn-default">搜索</button>
            </form>
        </div><!-- /.container-fluid -->
        </div><!-- /.navbar-collapse -->

    </nav>
</div>

<script type="text/javascript">
    function checkData() {
        var q = document.getElementById("q").value.trim();
        if (q == null || q == "") {
            alert("请输入您要查询的关键字！");
            return false;
        } else {
            return true;
        }
    }
</script>
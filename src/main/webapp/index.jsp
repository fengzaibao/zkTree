<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联接zk服务器</title>
<link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery文件，务必bootstarp.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
<!-- 最新的Bootstrarp 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>
	<form action="zookeeper.do" method="post" style="margin-left:500px;margin-top:200px;">
		<input type="hidden" name="op" value="login"/>
		<div class="form-group">
			<label for="connectString" stype="display:inline;">zk服务器地址</label>
			<input type="text" name="connectString" value="node1:2181,node2:2181,node3:2181" class="form-control" id="connectString" style="display:inline;width:300px;" autocomplete="off"/><br/>
		</div>
		<div class="form-group">
			<label for="sessionTimeout" stype="display:inline;">联接超时时间</label>
			<input type="text" name="sessionTimeout" value="1000" class="form-control" id="sessionTimeout" style="display:inline;width:200px;" autocomplete="off"/><br/>
		</div>
		<input type="submit" value="登录" class="btn btn-primary"/>
	</form>
</body>
</html>
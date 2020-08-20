<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>zookeeper后台管理</title>

<% 
	String path=request.getContextPath();//项目上下文路径就是(项目名)
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">
 <style type="text/css">
table {
    border-collapse: collapse;
    margin: 0 auto;
    text-align: center;
}
 
table td, table th {
    border: 1px solid #cad9ea;
    color: #666;
    height: 30px;
}
 
table thead th {
    background-color: #CCE8EB;
    width: 100px;
}
 
table tr:nth-child(odd) {
    background: #fff;
}
 
table tr:nth-child(even) {
    background: #F5FAFA;
}
</style>
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.7.0/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.7.0/themes/icon.css">
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.7.0/themes/color.css">
<script type="text/javascript" src="jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript">
//页面一加载完成，则发出请求：
	//1.显示数
	//2.显示当前节点 / 的内容
	$(function(){
		var path="/";
		$("#currentPath").html(path);
		//jquery的ajax请求
		$.ajax({
			type:"POST",
			url:"zookeeper.do",
			data:"op=listTree&path="+path,
			dataType:"JSON",
			success:function(data){
				if(data.code==1){
				 showTree("zktree",data.obj);
				 showNodeInfo(path);
				}
				
			}
		});
	})
	
	function showNodeInfo(path){
		$("#currentPath").html(path);
		$.ajax({
			type:"POST",
			url:"zookeeper.do",
			data:"op=showNodeInfo&path="+path,
			dataType:"JSON",
			success:function(data){
				clearAll();
				if(data==undefined||data.code==0){
					alert(data.errorMsg);
					return;
				}
				showData(data.obj);
			}
		})
  }
	function clearAll(){
	      $("#nodeContent").html("");
	      $("#numChildren").html("");
	      $("#czxid").html("");
	      $("#ctime").html("");
	      $("#mzxid").html("");
	      $("#mtime").html("");
	      $("#pzxid").html("");
	      $("#cversion").html("");
	      $("#dataVersion").html("");
	      $("#aclVersion").html("");
	      $("#nodetype").html("");
	      $("#clientId").html("");
	      $("#dataLength").html("");
	      $("#clientId").html("");
	  }
	   
	function showData(  obj ){
		//alert(obj.nodeContent );
		if (obj.nodeContent != undefined) {
			$("#nodeContent").html(obj.nodeContent);
		}
		$("#numChildren").html(obj.numChildren);
		$("#czxid").html(obj.czxid);
		$("#ctime").html(obj.ctime);
		$("#mzxid").html(obj.mzxid);
		$("#mtime").html(obj.mtime);
		$("#pzxid").html(obj.pzxid);
		$("#cversion").html(obj.cversion);
		$("#dataVersion").html(obj.dataVersion);
		$("#aclVersion").html(obj.aclVersion);
		$("#nodetype").html(obj.nodetype);
		$("#clientId").html(obj.clientId);
		$("#dataLength").html(obj.dataLength);
		$("#clientId").html(obj.clientId);
	}

	function showTree(treeId,treeData){
		$('#'+treeId).tree({
			data:treeData,
			onClick:function(node){
				//id  text   attributes target
				//1.调用showNodeInfo
				var path=node.attributes.fullPath;
				showNodeInfo(path);
				//查找显示  path下的子节点，并追加到树中
				$.ajax({
					type:"POST",
					url:"zookeeper.do",
					data:"op=listTree&path="+path,
					dataType:"JSON",
					success:function(data){
						if(data.code==0){
							return;
						}
						//清空原节点下的子节点
						//取出被点节点的子节点列表
						var children=$("#"+treeId).tree("getChildren",node.target);
						for(var i=0;i<children.length;i++){
						  $("#"+treeId).tree("remove",children[i]);
						}
						$("#"+treeId).tree("reload",node.target);
						var treeData=data.obj; //node.target 存的是当前被点的节点    将刚查出来的treeData 这个json的列表数据放到 node.target   子节点数据追加到 node.target对应的节点
					
						$('#'+treeId).tree("append",{
							parent:node.target,
							data:treeData
						});
					}
				})
			}
		})
  }
</script>

</head>
<body  class="easyui-layout">
	<div data-options="region:'west',split:true" title="节点列表" style="width:200px;">
	   <div class="easyui-panel" style="padding:5px">
         <ul class="easyui-tree" id="zktree">
          
         </ul>
       </div>
	</div>
    <div data-options="region:'center',title:'节点详情'">
        当前路径为:<span id="currentPath"></span> <br />
		节点内容: <span id="nodeContent"></span>
		<hr />
		<table border="1" padding="0px" margin="0px" width="90%">
								<tr>
									<td>子节点数</td>
									<td><span id="numChildren"></span></td>
									<td>数据长度(字节)</td>
									<td><span id="dataLength"></span></td>
								</tr>
								<tr>
									<td>创建znode的事务id czxid</td>
									<td><span id="czxid"></span></td>
									<td>创建znode的时间 ctime</td>
									<td><span id="ctime"></span></td>
								</tr>
								<tr>
									<td>更新znode的事务id mzxid</td>
									<td><span id="mzxid"></span></td>
									<td>更新znode的时间 mtime</td>
									<td><span id="mtime"></span></td>
								</tr>
								<tr>
									<td>更新或删除本节点或子节点的事务id pzxid</td>
									<td><span id="pzxid"></span></td>
									<td>子节点数据更新次数 cversion</td>
									<td><span id="cversion"></span></td>
								</tr>
								<tr>
									<td>本节点数据更新次数 dataVersion</td>
									<td><span id="dataVersion"></span></td>
									<td>节点ACL(授权信息)的更新次数 aclVersion</td>
									<td><span id="aclVersion"></span></td>
								</tr>
								<tr>
									<td>节点类型</td>
									<td><span id="nodetype"></span></td>
									<td>创建客户端id</td>
									<td><span id="clientId"></span></td>
								</tr>


		</table>
    </div>
</body>
</html>
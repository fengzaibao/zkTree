package com.yc.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import service.ZKHelper;
import web.model.JsonModel;

@WebServlet("/zookeeper.do")
public class ZookeeperServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String op=request.getParameter("op");
			if("login".equals(op)){
				loginOp(request,response);
			}else if("listTree".equals(op)){
				listTreeOp(request,response);
			}else if("showNodeInfo".equals(op)){
				showNodeInfoOp(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("500.jsp");
		}
	}


	private void showNodeInfoOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path=request.getParameter("path");
		ZooKeeper zk=validateZk(request.getSession());
		JsonModel jm=new JsonModel();
		
		Stat stat=new Stat();
		try {
			byte[] bytes=zk.getData(path, false, stat);//bytes文本内容      stat节点属性
			
			Map<String,String> node=new HashMap<String,String>();
			jm.setCode(1);
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strs=null;
		    try {
				strs=new String(bytes,"utf-8");
			} catch (Exception e) {
				strs=new String("");
			}
			node.put("nodeContent",strs);
			node.put("czxid",Long.toHexString(stat.getCzxid()));
			node.put("ctime",df.format(new Timestamp(stat.getCtime())));
			node.put("mzxid",Long.toHexString(stat.getMzxid()));
			node.put("mtime",df.format(new Timestamp(stat.getMtime() )));
			node.put("pzxid",Long.toHexString(stat.getPzxid()));
			node.put("cversion",stat.getCversion()+"");
			node.put("dataVersion",stat.getVersion()+"");
			node.put("aclVersion",stat.getAversion()+"");
			if(stat.getEphemeralOwner()==0){
				node.put("nodetype","持久节点");
			}else{
				node.put("nodetype","临时节点");
				node.put("clientId",Long.toHexString(stat.getEphemeralOwner()));
			}
			    node.put("dataLength",stat.getDataLength()+"");
				node.put("numChildren",stat.getNumChildren()+"");
				
				
				jm.setObj(node);
				
		} catch (KeeperException | InterruptedException e) {
			//e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.getMessage());
		}
		super.writeJson(jm, response);
	}

	private ZooKeeper validateZk(HttpSession session){
		ZooKeeper zk=(ZooKeeper) session.getAttribute("zk");
		if(zk==null&&zk.getState()!=ZooKeeper.States.CONNECTED){
			String connectString=(String) session.getAttribute("connectString");
			int  sessionTimeout=Integer.parseInt(session.getAttribute("sessionTimeout").toString());
			ZKHelper ZkHelper=new ZKHelper(connectString,sessionTimeout);
			try {
				zk=ZkHelper.connect();
				
				session.setAttribute("zk", zk);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return zk;
	}
	private void listTreeOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path=request.getParameter("path");
		ZooKeeper zk=validateZk(request.getSession());		
		JsonModel jm=new JsonModel();
		try {
			List<String> children=zk.getChildren(path, true);//text存节点名  节点全路径
			jm.setCode(1);//jm ,obj
			//easyui的树组件的数据是什么格式，怎么用
			//[ { id:1,text:xxx,children:[],attributes:{key:value}}]
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			for(int i=0;i<children.size();i++){
				Map<String,Object> node=new HashMap<String,Object>();
				node.put("id",i+"");
				node.put("text", children.get(i));
				Map<String,String> attributes=new HashMap<String,String>();
				if("/".equals(path)){
					attributes.put("fullPath", path+children.get(i));
				}else{
					attributes.put("fullPath", path+"/"+children.get(i));
				}
				node.put("attributes", attributes);
				list.add(node);
			}
			jm.setObj(list);
			
		} catch (Exception e) {//有异常情况：网络不好或者权限问题
			// TODO Auto-generated catch block
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.getMessage());
		} 
		super.writeJson(jm, response);
	}


	private void loginOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String connectString=request.getParameter("connectString");
		int sessionTimeout=Integer.parseInt(request.getParameter("sessionTimeout"));
		HttpSession session=request.getSession();
		session.setAttribute("connectString",connectString);
		session.setAttribute("sessionTimeout",sessionTimeout);
		ZKHelper zkHelper=new ZKHelper(connectString,sessionTimeout);
		try {
			ZooKeeper zk=zkHelper.connect();
			session.setAttribute("zk", zk);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		response.sendRedirect("back/main.jsp");
	}

}

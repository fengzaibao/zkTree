package com.yc.service;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void go404(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect("404.jsp");
	}
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected <T> T parseMap(HttpServletRequest request,Class<T> c) throws Exception{
		T t=c.newInstance();
		//1.从request中取出map
		Map<String,String[]> map=request.getParameterMap();
		//entry条目的set集合
		Set<Entry<String,String[]>> set=map.entrySet();
		Iterator <Entry<String,String[]>> ite=set.iterator();
		//取出c的方泿 
		Method[]ms=c.getMethods();
		while( ite.hasNext()){
			Entry<String,String[]> entry=ite.next();
			String key=entry.getKey();
			String[] values=entry.getValue();
			String value=null;
			if(values.length!=1){
				continue;
			}
			value=values[0];
			//System.out.println("传鿒的参数"+key+",值为"+value);
			Method m=findMethod(ms,key);
			if(m==null){
				continue;
			}
			//System.out.println("找到要激活的方法:"+m.getName());
			String typeName=m.getParameterTypes()[0].getName();
			//System.out.println(typeName);
			if ("java.lang.Integer".equals(typeName)  ||"int".equals(typeName)) {
				m.invoke(t, Integer.parseInt(value));
			} else if ("java.lang.Double".equals(typeName) ||"double".equals(typeName)) {
				m.invoke(t, Double.parseDouble(value));
			} else if ("java.lang.Float".equals(typeName)|| "float".equals(typeName)) {
				m.invoke(t, Float.parseFloat(value));
			} else if ("java.lang.Long".equals(typeName)|| "long".equals(typeName)) {
				m.invoke(t, Long.parseLong(value));
			} else {
				m.invoke(t, value);
			}
		
		
		}
		return t;
	}
    
	private Method findMethod(Method[] ms, String key) {
		for(Method m:ms){
			String methodName="set"+key;
			if(m.getName().equalsIgnoreCase(methodName)){
				return m;
			}
		}
		return null;
	}

	void writeJson(Object obj, HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		Gson g=new Gson();
		String s=g.toJson(obj);
		out.println(s);
		out.flush();
		out.close();
	}
	

}

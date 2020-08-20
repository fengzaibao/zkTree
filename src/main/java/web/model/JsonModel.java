package web.model;

import java.io.Serializable;

public class JsonModel implements Serializable{

	private static final long serialVersionUID = -835030376696748920L;
	
	private Integer code; //表示程序运行后的结果状态 成功:1  失败:0  ->可以拓展更多的响应码
	private String errorMsg;//失败时的消息
	private Object obj;//存结果数据 list  obj -> list<T>
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	@Override
	public String toString() {
		return "JsonModel [code=" + code + ", errorMsg=" + errorMsg + ", obj=" +obj + "]";
	}
	
}

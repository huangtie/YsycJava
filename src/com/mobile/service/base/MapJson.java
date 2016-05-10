package com.mobile.service.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapJson {

	private boolean success;
	private String errorCode;
	private String errorMsg;
	private Map<String, Object> data;

	public MapJson()
	{
		this.setSuccess(false);
		this.setErrorCode("9999");
		this.setErrorMsg("内部错误");
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getErrorCode(){
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	private void nullSet(Map<String, Object> m) {
		Iterator<Entry<String, Object>> i = m.entrySet().iterator();
		while (i.hasNext()) {// 只遍历一次,速度快
			Entry<String, Object> e = i.next();
			if (e.getValue() == null || e.getValue().equals("null")) {
				e.setValue("");
			}
			// System.out.println(e.getKey()+"="+e.getValue());
			// System.out.println(e.setValue(""));//返回value的值
		}
	}

	public void setData(Map<String, Object> data) {
		if (data != null) {
			nullSet(data);
		}
		this.data = data;
	}

}

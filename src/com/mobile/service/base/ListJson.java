package com.mobile.service.base;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ListJson {

	private boolean success;
	private String errorCode;
	private String errorMsg;
	private Long count;
	private List<Map<String, Object>> data;

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

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		if (data != null && !data.isEmpty()) {
			for (Map<String, Object> map : data) {
				nullSet(map);
			}
		}
		this.data = data;
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

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}

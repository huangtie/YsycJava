package com.mobile.service.base;

import java.util.List;

public class ListStringJson {

	private boolean _success;
	private String error_msg;
	private Long count;
	private List<String> data;
	public boolean is_success() {
		return _success;
	}
	public void set_success(boolean _success) {
		this._success = _success;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	
}

package com.mobile.service.base;

import javax.servlet.http.HttpServletRequest;

public class CommonParam {
	private String c;  //登录标识
	private String udid; //设备唯一标识
	private String ip;  //用户访问IP地址
	private String version; //app版本号
	private String model; //设备型号(如 iPad mini2)
	private String system; //设备系统版本号
	private String from; //0 iOS,1 安卓
	
	public CommonParam (HttpServletRequest request){
		this.c = request.getParameter("c");
		this.udid = request.getParameter("udid");
		this.ip = request.getParameter("ip");
		this.version = request.getParameter("version");
		this.model = request.getParameter("model");
		this.system = request.getParameter("system");
		this.from = request.getParameter("from");
	}
	
	public String getC(){
		return c;
	}
	
	public void setC(String c) {
		this.c = c;
	}
	
	public String getUdid(){
		return udid;
	}
	
	public void setUdid(String udid) {
		this.udid = udid;
	}
	
	public String getIp(){
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	public String getVersion(){
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getModel(){
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getSysteml(){
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getFrom(){
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
}

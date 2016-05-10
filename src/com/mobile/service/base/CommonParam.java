package com.mobile.service.base;

import javax.servlet.http.HttpServletRequest;

public class CommonParam {
	private String c;  //��¼��ʶ
	private String udid; //�豸Ψһ��ʶ
	private String ip;  //�û�����IP��ַ
	private String version; //app�汾��
	private String model; //�豸�ͺ�(�� iPad mini2)
	private String system; //�豸ϵͳ�汾��
	private String from; //0 iOS,1 ��׿
	
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

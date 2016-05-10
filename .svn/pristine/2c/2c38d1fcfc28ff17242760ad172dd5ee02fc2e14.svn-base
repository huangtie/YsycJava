package com.mobile.bean;

import net.sf.json.JSONObject;

public class Group {
	public String group_name;
	public String group_id;
	public String image;
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public static void main(String[] args) {
		Group group=new Group();
		group.setGroup_id("12");
		group.setGroup_name("我是一个么");
		group.setImage("http://www.baidu.com");
		JSONObject json = JSONObject.fromObject(group);  
		System.out.println(json.toString());
		
	}
}

package com.mobile.service.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Convert {

	/**
	 * 把发起人id以逗号分开，并加上站点标示
	 * @param from 站点标示
	 * @param list 发起人id
	 * @return
	 */
	public static String listToString(String from, List<String> list){
		String result = "";
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				result += "UU" + from + "UU" + list.get(i) + ",";
			}
		}
		return result;
	}
	
	public static String stringToString(String from, String id){
		String result = "UU" + from + "UU" + id;
		return result;
	}
	
	/**
	 * 把发起人名字以逗号分开，并加上站点标示
	 * @param list 发起人名字
	 * @return
	 */
	public static String listToString(List<String> list){
		String result = "";
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				result += list.get(i) + ",";
			}
		}
		return result;
	}
	
	public static String thumbnailToOriginal(String thumbnail){
		try{
			if(thumbnail != null && !thumbnail.equals("")){
				int start = thumbnail.lastIndexOf("-");
				int end = thumbnail.lastIndexOf(".");
				String pre = thumbnail.substring(0, start);
				String suf = thumbnail.substring(end);
				thumbnail = pre + suf;
			}
		} catch(Exception e){
			
		}
		return thumbnail;
	}
	
	/**
	 * 把null转成空
	 * @param list 发起人名字
	 * @return
	 */
	public static String nullToString(String value){
		
		return value == null ? "" : value;
	}
	
	/**
	 * 把null转成空
	 * @param list 发起人名字
	 * @return
	 */
	public static String nullToString(Object value){
		return value == null ? "" : value.toString();
	}
	
	/**
	 * 把null转成空
	 * @param list 发起人名字
	 * @return
	 */
	public static String nullToString1(Object value){
		if(value == null || value.equals("")){
			return "";
		}
		return value.toString().substring(0, value.toString().length() - 1);
		
	}
	
	/**
	 * 把null转成0
	 * @param list 发起人名字
	 * @return
	 */
	public static Integer nullToInt(Object value){
		int result = 0;
		if(value == null){
			return 0;
		}
		try{
			result = Integer.parseInt(value.toString());
		} catch(Exception e){
			result = 0;
		}
		return result;
	}
	
	
	/**
	 * 把null转成0
	 * @param list 发起人名字
	 * @return
	 */
	public static Integer nullToInt(Integer value){
		return value == null ? 0 : value;
	}
	
	/**
	 * 格式化日期(yyyy-MM-dd)
	 * @param value 待格式化的字符串
	 * @return
	 */
	public static Date stringToDate(String value){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date result = null;
		try {
			result = format.parse("" + year + "-" + value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 计算过期时间(yyyy-MM-dd HH:mm:ss)
	 * @param value 待格式化的字符串
	 * @return
	 */
	public static String dateToExpire(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,30);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = format.format(cal.getTime());
		return result;
	}
	
	/**
	 * 计算过期时间(yyyy-MM-dd HH:mm:ss)
	 * @param value 待格式化的字符串
	 * @return
	 */
	public static String[] cToUserId(String c){
		String[] temp = c.split("[+]");
		System.out.println(temp.toString());
		return temp;
	}
	
	/**
	 * 计算过期时间(yyyy-MM-dd HH:mm:ss)
	 * @param value 待格式化的字符串
	 * @return
	 */
	public static boolean dateThan(String expireTime){
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date result = null;
		try {
			result = format.parse(expireTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.getTime().before(result);
	}
	
	/**
	 * 计算过期时间(yyyy-MM-dd HH:mm:ss)
	 * @param value 待格式化的字符串
	 * @return
	 */
	public static String typeToType(String type){
		String result = "";
		if(type != null){
			if(type.equals("登山")){
				result = "登山|穿越|徒步|爬山|探路";
			}
			else if(type.equals("骑行")){
				result = "骑行|自行车|骑车";		
			}
			else if(type.equals("摄影")){
				result = "摄影|行摄";
			}
			else if(type.equals("自驾")){
				result = "自驾";
			}
			else if(type.equals("滑雪")){
				result = "滑雪|单板";
			}
			else if(type.equals("聚会")){
				result = "聚会|休闲";
			}
		}
		return result;
	}
	
	public static String touyeToTourye(String siteId){
		if(siteId.equals("touye")){
			return "tourye";
		}
		return siteId;
	}
	public static String toUtf8(String old) throws UnsupportedEncodingException{
		String newStr=new String(old.getBytes("ISO-8859-1"), "utf-8");
		return newStr;
	}
	public static String togb2312(String old) throws UnsupportedEncodingException{
		String newStr=new String(old.getBytes("ISO-8859-1"), "gb2312");
		return newStr;
	}
	public static String togbk(String old) throws UnsupportedEncodingException{
		String newStr=new String(old.getBytes("ISO-8859-1"), "gbk");
		return newStr;
	}
	public static void main(String[] args) {
		String a="%E6%94%B6%E4%BF%A1%E4%BA%BA%E6%89%8B%E6%9C%BA%E5%8F%B7%E6%94%B6%E4%BF%A1%E4%BA%BA%E6%89%8B%E6%9C%BA%E5%8F%B7";
//		String a="%E5%93%88%E5%93%88";
//		String newStr=new String(java.net.URLDecoder.decode(a));
		String newStr = null;
		try {
			newStr = java.net.URLDecoder.decode(a,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(newStr);
//		try {
//			System.out.println(new String(newStr.getBytes("ISO-8859-1"), "gb2312"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}

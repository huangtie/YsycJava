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
	 * �ѷ�����id�Զ��ŷֿ���������վ���ʾ
	 * @param from վ���ʾ
	 * @param list ������id
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
	 * �ѷ����������Զ��ŷֿ���������վ���ʾ
	 * @param list ����������
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
	 * ��nullת�ɿ�
	 * @param list ����������
	 * @return
	 */
	public static String nullToString(String value){
		
		return value == null ? "" : value;
	}
	
	/**
	 * ��nullת�ɿ�
	 * @param list ����������
	 * @return
	 */
	public static String nullToString(Object value){
		return value == null ? "" : value.toString();
	}
	
	/**
	 * ��nullת�ɿ�
	 * @param list ����������
	 * @return
	 */
	public static String nullToString1(Object value){
		if(value == null || value.equals("")){
			return "";
		}
		return value.toString().substring(0, value.toString().length() - 1);
		
	}
	
	/**
	 * ��nullת��0
	 * @param list ����������
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
	 * ��nullת��0
	 * @param list ����������
	 * @return
	 */
	public static Integer nullToInt(Integer value){
		return value == null ? 0 : value;
	}
	
	/**
	 * ��ʽ������(yyyy-MM-dd)
	 * @param value ����ʽ�����ַ���
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
	 * �������ʱ��(yyyy-MM-dd HH:mm:ss)
	 * @param value ����ʽ�����ַ���
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
	 * �������ʱ��(yyyy-MM-dd HH:mm:ss)
	 * @param value ����ʽ�����ַ���
	 * @return
	 */
	public static String[] cToUserId(String c){
		String[] temp = c.split("[+]");
		System.out.println(temp.toString());
		return temp;
	}
	
	/**
	 * �������ʱ��(yyyy-MM-dd HH:mm:ss)
	 * @param value ����ʽ�����ַ���
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
	 * �������ʱ��(yyyy-MM-dd HH:mm:ss)
	 * @param value ����ʽ�����ַ���
	 * @return
	 */
	public static String typeToType(String type){
		String result = "";
		if(type != null){
			if(type.equals("��ɽ")){
				result = "��ɽ|��Խ|ͽ��|��ɽ|̽·";
			}
			else if(type.equals("����")){
				result = "����|���г�|�ﳵ";		
			}
			else if(type.equals("��Ӱ")){
				result = "��Ӱ|����";
			}
			else if(type.equals("�Լ�")){
				result = "�Լ�";
			}
			else if(type.equals("��ѩ")){
				result = "��ѩ|����";
			}
			else if(type.equals("�ۻ�")){
				result = "�ۻ�|����";
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

package com.mobile.service.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {

	/**
	 * 是否为空
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(Object value){
		if(value == null || String.valueOf(value).trim().equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是电话号码
	 * @param value
	 * @return
	 */
	public static boolean isTelephone(Object value){
		if(String.valueOf(value).length() != 11){
			return false;
		}
		try{
			Long.parseLong(String.valueOf(value));
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 是否是整数
	 * @param value
	 * @return
	 */
	public static boolean isInt(Object value){
		try{
			Integer.parseInt(String.valueOf(value));
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 是否是钱数
	 * @param value
	 * @return
	 */
	public static boolean isMoney(Object value){
		try{
			Double.parseDouble(String.valueOf(value));
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 是否是生日
	 * @param value
	 * @return
	 */
	public static boolean isBirth(Object value){
		try{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.parse(String.valueOf(value));
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 是否是邮件
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String value){
		Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		Matcher m = p.matcher(value);
		boolean b = m.matches();
		if(b){
			return true;
		}
		else{
			return false;
		}
	}
}

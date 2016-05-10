package com.mobile.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

public class tools {
	public static String CorpID="yy90013";
	public static String Pwd="239863";
	public static String msgUrl="http://ipyy.net/WS/Send.aspx"; 
	public static String sendMobilePhoneMsg(String content,String phoneNo){
		//http://ipyy.net/WS/Send.aspx?CorpID=yy90013&Pwd=239863&Mobile=18910330915&Content=啦啦啦啦啦啦，我是卖报的小行家&Cell=&SendTime=20131210133910
		String returnStr=null;
		//send msg to phone 
		 String urlStr = msgUrl;
		 StringBuilder urlBur = new StringBuilder(urlStr);
		 URL url = null;
		 try {
			 urlBur.append("?CorpID=").append(CorpID);
			 urlBur.append("&Pwd=").append(Pwd);
			 urlBur.append("&Mobile=").append(phoneNo);
			 urlBur.append("&Content=").append(content);
			 urlBur.append("&Cell=").append("");
			 urlBur.append("&SendTime=");
			 url = new URL(urlBur.toString());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		 InputStream in = null;
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 try {
		 URLConnection urlConn = url.openConnection();
		 urlConn.connect();
		 in = urlConn.getInputStream();
		 IOUtils.copy(in, out);
		 out.flush();
		 returnStr=out.toString();
		 } catch (IOException e) {
		 e.printStackTrace();
		 } finally {
		 if (in != null) {
		 try {
		 in.close();
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
		 }
		 }
		return returnStr;
	}
	
	public static String getNewTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
		return df.format(new Date());
		
	}
	public static String getNewTimelink(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");//设置日期格式
		return df.format(new Date());
	}
	public static String getcurrentTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		return df.format(new Date());
	}
	public static String simpletime(){
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");//设置日期格式
		return new Date().toString();
		
	}
	/**
	 * 获取随机数
	 * @return
	 */
	public static String getrandomnum(){
		return Long.toString(Math.round(Math.random()*8999+1000));
	}
 
	public static void main(String[] args) {
		
		
//		String returnStr=tools.sendMobilePhoneMsg("您的来信验证码为:8923", "18910330915");
//		System.out.println(returnStr);
//		System.out.println(System.currentTimeMillis());
//		String verificationcode=Math.round(Math.random()*10000)+"";
//		while (verificationcode.length()<4) {
//			verificationcode=Math.round(Math.random()*10000)+"";
//		}
//		System.out.println(verificationcode.length());
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");//设置日期格式
//		System.out.println(df.format("1387432449083"));
		
		String ts[]=new String[]{"1395061112367"}; 
//		String beginDate="1387432449083";
		for (int i = 0; i < ts.length; i++) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String sd = sdf.format(new Date(Long.parseLong(ts[i])));
			System.out.println(sd);
		}
		
//		System.out.println(getrandomnum());
	}
}

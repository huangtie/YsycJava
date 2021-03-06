package com.mobile.service.util;

public interface Constants {

	//页面显示的条数
	public final static int pageSize = 10;
	
	//页面显示的条数
	public final static int pageSize50 = 50;
	
	//加密串
	public final static String ENCODE_KEY = "1234567890123456";
	
	public final static String AESKEY = "1234567891234567";
	
	//头像本地路径
	public static String ImageFaceLocalPath = "d:\\webnew\\UpLoadFiles\\FaceImage";
	
	//说说图片本地路径
	public static String ImageTalkLocalPath = "d:\\webnew\\UpLoadFiles\\TalkImage";
	
	//图集图片本地路径
	public static String ImageImagesLocalPath = "d:\\webnew\\UpLoadFiles\\Images";
	
	//拍品图片本地路径
	public static String ImageGoodsLocalPath = "d:\\webnew\\UpLoadFiles\\GoodImage";
	
	//身份证图片本地路径
	public static String ImageIdcarLocalPath = "d:\\webnew\\UpLoadFiles\\IdcarImage";
	
	
	//头像图片路径
	public static String ImageFacePath = "http://yishangyacang.top/UpLoadFiles/FaceImage/";
	
	//图集内容图片路径
	public static String ImageImagesPath = "http://yishangyacang.top/UpLoadFiles/Images/";
	
	//说说图片路径
	public static String ImageTalkPath = "http://yishangyacang.top/UpLoadFiles/TalkImage/";
	
	//拍品图片路径
	public static String ImageGoodPath = "http://yishangyacang.top/UpLoadFiles/GoodImage/";
	
	//身份证图片路径
	public static String ImageIdcarPath = "http://yishangyacang.top/UpLoadFiles/IdcarImage/";
	
	
 
	//图集分享页面路径
	public static String ShareHtmlPath = "http://yishangyacang.top/html/share.aspx?bookid=";
	
	//新闻分享页面路径
	public static String ShareNewsHtmlPath = "http://yishangyacang.top/html/shareNews.aspx?newsID=";
	
	//图片保存的根路径
 
	public static String BASE_PATH = "D:/apache-tomcat-6.0.35/webapps/V1/static/media/";
//	public static String BASE_PATH = "F:/apache-tomcat-6.0.35/webapps/V1/static/media/";
	
	
	//图片的访问根路径
	public static String IMG_URL="/sjwlv3/image/";
	//v1
	public static String HOST_PATH = "http://114.112.174.165:8080/V1/static/media/";
//	public static String HOST_PATH = "http://192.168.1.101:8080/V1/static/media/";
	//默认头像缩略图
	public static String DEFAULT_USER_URL_NAME = "detaultUser-thumbnail.jpg";
	//默认头像图片
	public static String DEFAULT_USER_ORI_URL_NAME = "detaultUser.jpg";
	
	//根访问路径
	//v1
//	public static String HOST_BASE = "http://192.168.1.101:8080/V1"; 
	public static String HOST_BASE = "http://114.112.174.165:8080/V1"; 
	//头像宽
	public static int IMAGE_HEAD_WIDTH = 160;
	
	//头像高
	public static int IMAGE_HEAD_HEIGHT = 160;
	
	//列表页图片宽
	public static int IMAGE_LIST_WIDTH = 80;
	
	//列表页图片高
	public static int IMAGE_LIST_HEIGHT = 80;
	
	//瀑布式排列图片宽
	public static int IMAGE_PULL = 120;
	
	//内容页图片宽
	public static int IMAGE_CONTENT = 240;
	
	//地点的头部
	public static String DETAIL_HEAD_LOCATION = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=321\" />" +
			"<meta charset=\"utf-8\" /></head><body style=\"overflow-x:hidden;width:300px;margin:0;padding:10px;line-height:25px\">";
	
	//活动详细的头部
	public static String DETAIL_HEAD = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=321\" />" +
			"<meta charset=\"utf-8\" /><script type=\"text/javascript\">function scale(url){Ni.openImage(url);}function skip(url){Ni.openUrl(url);}</script></head><body style=\"overflow-x:hidden;width:320px;margin:0;padding:0\">";
	
	//活动详细的头部IOS
	public static String DETAIL_HEAD_IOS = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=321\" />" +
			"<meta charset=\"utf-8\" /><script type=\"text/javascript\">var Ni={};Ni.openImage = function(url){ window.location=\"fb/image/\" + url;};Ni.openUrl=function(url) {window.location=url;};function scale(url){Ni.openImage(url);}function skip(url){Ni.openUrl(url);}</script></head><body style=\"overflow-x:hidden;width:320px;margin:0;padding:0\">";
	
	//详细尾部
	public static String DETAIL_FOOTER = "</body></html>";
}

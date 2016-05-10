package com.mobile.service.util;

public interface Constants {

	//ҳ����ʾ������
	public final static int pageSize = 10;
	
	//ҳ����ʾ������
	public final static int pageSize50 = 50;
	
	//���ܴ�
	public final static String ENCODE_KEY = "1234567890123456";
	
	public final static String AESKEY = "1234567891234567";
	
	//ͷ�񱾵�·��
	public static String ImageFaceLocalPath = "d:\\webnew\\UpLoadFiles\\FaceImage";
	
	//˵˵ͼƬ����·��
	public static String ImageTalkLocalPath = "d:\\webnew\\UpLoadFiles\\TalkImage";
	
	//ͼ��ͼƬ����·��
	public static String ImageImagesLocalPath = "d:\\webnew\\UpLoadFiles\\Images";
	
	//��ƷͼƬ����·��
	public static String ImageGoodsLocalPath = "d:\\webnew\\UpLoadFiles\\GoodImage";
	
	//����֤ͼƬ����·��
	public static String ImageIdcarLocalPath = "d:\\webnew\\UpLoadFiles\\IdcarImage";
	
	
	//ͷ��ͼƬ·��
	public static String ImageFacePath = "http://yishangyacang.top/UpLoadFiles/FaceImage/";
	
	//ͼ������ͼƬ·��
	public static String ImageImagesPath = "http://yishangyacang.top/UpLoadFiles/Images/";
	
	//˵˵ͼƬ·��
	public static String ImageTalkPath = "http://yishangyacang.top/UpLoadFiles/TalkImage/";
	
	//��ƷͼƬ·��
	public static String ImageGoodPath = "http://yishangyacang.top/UpLoadFiles/GoodImage/";
	
	//����֤ͼƬ·��
	public static String ImageIdcarPath = "http://yishangyacang.top/UpLoadFiles/IdcarImage/";
	
	
 
	//ͼ������ҳ��·��
	public static String ShareHtmlPath = "http://yishangyacang.top/html/share.aspx?bookid=";
	
	//���ŷ���ҳ��·��
	public static String ShareNewsHtmlPath = "http://yishangyacang.top/html/shareNews.aspx?newsID=";
	
	//ͼƬ����ĸ�·��
 
	public static String BASE_PATH = "D:/apache-tomcat-6.0.35/webapps/V1/static/media/";
//	public static String BASE_PATH = "F:/apache-tomcat-6.0.35/webapps/V1/static/media/";
	
	
	//ͼƬ�ķ��ʸ�·��
	public static String IMG_URL="/sjwlv3/image/";
	//v1
	public static String HOST_PATH = "http://114.112.174.165:8080/V1/static/media/";
//	public static String HOST_PATH = "http://192.168.1.101:8080/V1/static/media/";
	//Ĭ��ͷ������ͼ
	public static String DEFAULT_USER_URL_NAME = "detaultUser-thumbnail.jpg";
	//Ĭ��ͷ��ͼƬ
	public static String DEFAULT_USER_ORI_URL_NAME = "detaultUser.jpg";
	
	//������·��
	//v1
//	public static String HOST_BASE = "http://192.168.1.101:8080/V1"; 
	public static String HOST_BASE = "http://114.112.174.165:8080/V1"; 
	//ͷ���
	public static int IMAGE_HEAD_WIDTH = 160;
	
	//ͷ���
	public static int IMAGE_HEAD_HEIGHT = 160;
	
	//�б�ҳͼƬ��
	public static int IMAGE_LIST_WIDTH = 80;
	
	//�б�ҳͼƬ��
	public static int IMAGE_LIST_HEIGHT = 80;
	
	//�ٲ�ʽ����ͼƬ��
	public static int IMAGE_PULL = 120;
	
	//����ҳͼƬ��
	public static int IMAGE_CONTENT = 240;
	
	//�ص��ͷ��
	public static String DETAIL_HEAD_LOCATION = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=321\" />" +
			"<meta charset=\"utf-8\" /></head><body style=\"overflow-x:hidden;width:300px;margin:0;padding:10px;line-height:25px\">";
	
	//���ϸ��ͷ��
	public static String DETAIL_HEAD = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=321\" />" +
			"<meta charset=\"utf-8\" /><script type=\"text/javascript\">function scale(url){Ni.openImage(url);}function skip(url){Ni.openUrl(url);}</script></head><body style=\"overflow-x:hidden;width:320px;margin:0;padding:0\">";
	
	//���ϸ��ͷ��IOS
	public static String DETAIL_HEAD_IOS = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=321\" />" +
			"<meta charset=\"utf-8\" /><script type=\"text/javascript\">var Ni={};Ni.openImage = function(url){ window.location=\"fb/image/\" + url;};Ni.openUrl=function(url) {window.location=url;};function scale(url){Ni.openImage(url);}function skip(url){Ni.openUrl(url);}</script></head><body style=\"overflow-x:hidden;width:320px;margin:0;padding:0\">";
	
	//��ϸβ��
	public static String DETAIL_FOOTER = "</body></html>";
}
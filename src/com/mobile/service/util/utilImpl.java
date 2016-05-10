package com.mobile.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class utilImpl implements Constants {
	/**
	 * 周边足迹范围默认为5000，单位米
	 */
	private static double Dist = 5000d;
	private final static double PI = 3.14159265358979323; // 圆周率
	private final static double R = 6371229; // 地球的半径

	public static double getDistance(double longt1, double lat1, double longt2,
			double lat2) {
		double x, y, distance;
		x = (longt2 - longt1) * PI * R
				* Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * R / 180;
		distance = Math.hypot(x, y);
		return distance;
	}

	// public static void main(String[] args) {
	// double dist=getDistance(116.431890,39.968281,116.430981,39.967114);
	// System.out.println("dist="+dist);
	// }
	public static double getDist() {
		return Dist;
	}

	public static void setDist(double dist) {
		Dist = dist;
	}

	public static double getPi() {
		return PI;
	}

	public static double getR() {
		return R;
	}

	// 把新的集合数据加入到旧的集合里面，若相同就不加了
	public static List<Map<String, Object>> addListByRemoveOver(
			List<Map<String, Object>> p, List<Map<String, Object>> c) {
		boolean ifOver = false;// 判断新的对象是否在旧的集合里有存在
		for (Map<String, Object> m : c) {
			ifOver = false;
			for (Map<String, Object> cm : p) {
				if (m.get("id").equals(cm.get("id"))) {
					ifOver = true;
				}
			}
			if (ifOver == false) {
				p.add(m);
			}
		}
		return p;
	}

	public static void MyFunction(String topic, String deviceId, String message) {

		HttpClient httpclient = new DefaultHttpClient();

		// 你的URL

		HttpPost httppost = new HttpPost(
				"http://42.121.13.62/send_mqtt/send_mqtt.php");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			// Your DATA
			nameValuePairs.add(new BasicNameValuePair("target", topic + "/"
					+ deviceId));
			nameValuePairs.add(new BasicNameValuePair("message", message));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response;

			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public static void MyFunction() {

		HttpClient httpclient = new DefaultHttpClient();

		// 你的URL

		HttpPost httppost = new HttpPost(
				"http://localhost/tokudu-PhpMQTTClient-ba4e494/send_mqtt.php");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			// Your DATA
			nameValuePairs.add(new BasicNameValuePair("target",
					"tokudu/60f5c97edbb7f903"));

			nameValuePairs.add(new BasicNameValuePair("message",
					"eoeAndroid.com is Cool!"));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response;

			response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
		MyFunction();
	}

}

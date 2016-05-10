package com.mobile.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class MyHttpClient {
	
	public static String getHtml(String host, String url) throws Exception{
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpHost targetHost = new HttpHost(host);
		HttpGet httpPost = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 此处重定向处理 此处还未验证
			String newUri = httpResponse.getLastHeader("Location").getValue();
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(newUri);
			httpResponse = httpClient.execute(targetHost, httpGet);
		}

		// Get hold of the response entity
		HttpEntity entity = httpResponse.getEntity();
		StringBuffer sb = new StringBuffer();
		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
//			InputStream is = entity.getContent();
//			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//			String temp = null;
//			
//			while((temp = br.readLine()) != null){
//				sb = sb.append(temp);
//			}
//			br.close();
//			is.close();
			String charSet = "";
//
			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			charSet = EntityUtils.getContentCharSet(entity);
//
			System.out.println("In header: " + charSet);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				m.find();
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			result = new String(bytes, charSet);
			result = sb.toString();//).getBytes(), charSet);
		}

		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String postHtmlRedirect(String host, String url, Map<String, String> params) throws Exception{
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//		AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//		
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpHost targetHost = new HttpHost(host);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ){
			String key = ite.next();
			String value = params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			Header headers[] = httpResponse.getAllHeaders();
			int ii = 0;
			String secureCode = "";
			String account = "";
			while (ii < headers.length) {
				if(ii == 8){
					HeaderElement[] headElements = headers[ii].getElements();
					if(headElements.length > 0){
						account = headElements[0].getName() + "=" + headElements[0].getValue();
					}
				}
				if(ii == 9){
					HeaderElement[] headElements = headers[ii].getElements();
					if(headElements.length > 0){
						secureCode = headElements[0].getName() + "=" + headElements[0].getValue();
					}
				}
				++ii;
			}
			// 此处重定向处理 此处还未验证
			String newUri = httpResponse.getLastHeader("Location").getValue();
			
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(newUri);

			httpGet.addHeader("Cookie", secureCode + ";" + account);
			httpResponse = httpClient.execute(targetHost, httpGet);
		}
		// Get hold of the response entity
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
			String charSet = "";
			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			charSet = EntityUtils.getContentCharSet(entity);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				m.find();
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			result = new String(bytes, charSet);
		}
		httpClient.getConnectionManager().shutdown();
		return result;
	}
	

	
	public static String getCookie(String host, String url, Map<String, String> params) throws Exception{
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//		AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//		
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpHost targetHost = new HttpHost(host);
		HttpPost httpPost = new HttpPost(url);
		String secureCode = "";
		String account = "";
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ){
			String key = ite.next();
			String value = params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			Header headers[] = httpResponse.getAllHeaders();
			int ii = 0;

			while (ii < headers.length) {
				if(ii == 8){
					HeaderElement[] headElements = headers[ii].getElements();
					if(headElements.length > 0){
						account = headElements[0].getName() + "=" + headElements[0].getValue();
					}
				}
				if(ii == 9){
					HeaderElement[] headElements = headers[ii].getElements();
					if(headElements.length > 0){
						secureCode = headElements[0].getName() + "=" + headElements[0].getValue();
					}
				}
				++ii;
			}
			result = secureCode + ";" + account;
		}
		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String postHtml(String host, String url, Map<String, String> params) throws Exception{
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpHost targetHost = new HttpHost(host);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ){
			String key = ite.next();
			String value = params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			Header headers[] = httpResponse.getAllHeaders();
			int ii = 0;
			while (ii < headers.length) {
				System.out.println(headers[ii].getName() + ": "
						+ headers[ii].getValue());
				++ii;
			}
			// 此处重定向处理 此处还未验证
			String newUri = httpResponse.getLastHeader("Location").getValue();
			System.out.println(newUri);
			
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(newUri);
			httpResponse = httpClient.execute(targetHost, httpGet);
		}

		// Get hold of the response entity
		HttpEntity entity = httpResponse.getEntity();

		Header headers[] = httpResponse.getAllHeaders();
		int ii = 0;
		while (ii < headers.length) {
			System.out.println(headers[ii].getName() + ": "
					+ headers[ii].getValue());
			++ii;
		}

		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
			String charSet = "";

			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			charSet = EntityUtils.getContentCharSet(entity);

			System.out.println("In header: " + charSet);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				m.find();
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			result = new String(bytes, charSet);
		}

		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String postHtml(String host, String url, String cookie, Map<String, String> params) throws Exception{
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//		AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//		
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpHost targetHost = new HttpHost(host);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ){
			String key = ite.next();
			String value = params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
		httpPost.addHeader("Cookie", cookie);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			Header headers[] = httpResponse.getAllHeaders();
			int ii = 0;
			while (ii < headers.length) {
				System.out.println(headers[ii].getName() + ": "
						+ headers[ii].getValue());
				++ii;
			}
			// 此处重定向处理 此处还未验证
			String newUri = httpResponse.getLastHeader("Location").getValue();
			System.out.println(newUri);
			
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(newUri);
			httpResponse = httpClient.execute(targetHost, httpGet);
		}

		// Get hold of the response entity
		HttpEntity entity = httpResponse.getEntity();
		Header headers[] = httpResponse.getAllHeaders();
		int ii = 0;
		while (ii < headers.length) {
			System.out.println(headers[ii].getName() + ": "
					+ headers[ii].getValue());
			++ii;
		}

		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
			String charSet = "";

			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			charSet = EntityUtils.getContentCharSet(entity);

			System.out.println("In header: " + charSet);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				m.find();
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			result = new String(bytes, charSet);
		}
		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String postDiscuzHtml(String host, String url, String cookie, Map<String, String> params) throws Exception{
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//		AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//		
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpHost targetHost = new HttpHost(host);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ){
			String key = ite.next();
			String value = params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
		httpPost.addHeader("Cookie", cookie);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			Header headers[] = httpResponse.getAllHeaders();
			int ii = 0;
			while (ii < headers.length) {
				System.out.println(headers[ii].getName() + ": "
						+ headers[ii].getValue());
				++ii;
			}
			// 此处重定向处理 此处还未验证
			String newUri = httpResponse.getLastHeader("Location").getValue();
			System.out.println(newUri);
			
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(newUri);
			httpResponse = httpClient.execute(targetHost, httpGet);
		}

		// Get hold of the response entity
		HttpEntity entity = httpResponse.getEntity();
		Header headers[] = httpResponse.getAllHeaders();
		int ii = 0;
		while (ii < headers.length) {
			System.out.println(headers[ii].getName() + ": "
					+ headers[ii].getValue());
			++ii;
		}

		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
			String charSet = "";

			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			charSet = EntityUtils.getContentCharSet(entity);

			System.out.println("In header: " + charSet);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				m.find();
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			result = new String(bytes, charSet);
		}
		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String getDiscuzLoginFormhash(String host, String url){
		String loginFormhash=null;  
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//			AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//	        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//	        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//			
//			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			HttpHost targetHost = new HttpHost(host);
	        HttpResponse response = null;  
	        HttpEntity entity = null;  
	        StringBuffer sb=null;  
	        int pos;
	        HttpGet httpGet = new HttpGet(url);  
	        response=httpClient.execute(targetHost, httpGet);  
	        entity = response.getEntity();  
	        //输出页面内容  
	        if (entity != null) {  
	            String charset = EntityUtils.getContentCharSet(entity);  
	            InputStream is = entity.getContent();  
	            sb = new StringBuffer();  
	            BufferedReader br = new BufferedReader(new InputStreamReader(is,  
	                    charset));  
	            String line = null;  
	            while ((line = br.readLine()) != null) {  
	                sb.append(line);
	                System.out.println(line);
	            }  
	            is.close();  
	            //System.out.println(sb);  
	        }  
	        pos = sb.indexOf("name=\"formhash\" value=");    
	        // System.out.println(sb);    
	        // 找出这个 formhash 的内容，这是登录用的 formhash    
	        loginFormhash = sb.substring(pos + 23, pos + 23 + 8);   
	        System.out.println(loginFormhash);
		} catch(Exception e){
			e.printStackTrace();
		}
		return loginFormhash;
	}
	
	public static String getDiscuzCookie(String host, String url, Map<String, String> params) throws Exception{
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//		AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//		
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpHost targetHost = new HttpHost(host);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Iterator<String> ite = params.keySet().iterator(); ite.hasNext(); ){
			String key = ite.next();
			String value = params.get(key);
			nvps.add(new BasicNameValuePair(key, value));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		HttpResponse httpResponse = httpClient.execute(targetHost, httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
		}
		Header headers[] = httpResponse.getAllHeaders();
		int ii = 0;

		while (ii < headers.length) {
			HeaderElement[] headElements = headers[ii].getElements();
			for(int j = 0; j < headElements.length; j++){
				System.out.println(headElements[j].getName());
				System.out.println(headElements[j].getValue());
				if(headElements[j].getName().startsWith("Ryow_") || headElements[j].getName().startsWith("RKvU_") || headElements[j].getName().startsWith("uchome_")){
					result += headElements[j].getName() + "=" + headElements[j].getValue() + ";";
				}
				if(headElements[j].getName().startsWith("PHPSESSID")){
					result += headElements[j].getName() + "=" + headElements[j].getValue() + ";";
				}
			}
//			System.out.println(headElements[0].getName());
//			System.out.println(headElements[0].getValue());
//			if(headElements[0].getName() != null && headElements[0].getName().equals("PHPSESSID")){
//				result = headElements[0].getValue();
//			}
			++ii;
			
			
			
			//Ryow_c470_visitedfid=2D351D19D1815; Ryow_c470_sina_bind_957163=-1; Ryow_c470_forum_lastvisit=D_2_1327844635; Ryow_c470_smile=1D1; Ryow_c470_saltkey=68V9e8Kb; Ryow_c470_lastvisit=1328097213; PHPSESSID=49c2633dee58ad487fe65ab9a887bc1f; Ryow_c470_home_readfeed=1328100813; Ryow_c470_auth=9f76QErzdEifPxp8bzk8VBimPbxmrcp4oJvN301XfTm73hXcq4a9oHQUbw%2Fpz2qexF%2BDwP3vV2wYdmIJ0Fl4D2fFcHE; Ryow_c470_nofocus_0=1; Ryow_c470_ulastactivity=e4a50yrtYch9vEaLcJveKKan9IhpY6dRVEUtvFSglfl0sIrGz%2F79; Ryow_c470_mobile=no; Ryow_c470_onlineusernum=5467; Ryow_c470_home_diymode=1; Ryow_c470_sid=vvOjU6; Hm_lvt_d115ff5d8612d97fba465e8c30ea73b7=1328104492352; Hm_lpvt_d115ff5d8612d97fba465e8c30ea73b7=1328104492352; pgv_pvi=3443418441; pgv_info=ssi=s9384904425; __utma=224529112.220867854.1325508317.1326532742.1328104068.8; __utmb=224529112.4.10.1328104068; __utmc=224529112; Ryow_c470_sendmail=1; __utmz=224529112.1328104068.8.8.utmcsr=bbs.lvye.cn|utmccn=(referral)|utmcmd=referral|utmcct=/; Ryow_c470_lastact=1328104494%09home.php%09spacecp; Ryow_c470_connect_is_bind=0; Ryow_c470_checkpm=1; Ryow_c470_noticeTitle=1; __utma=224529112.220867854.1325508317.1326532742.1328104068.8; __utmb=224529112.5.10.1328104068; __utmc=224529112; __utmz=224529112.1328104068.8.8.utmcsr=bbs.lvye.cn|utmccn=(referral)|utmcmd=referral|utmcct=/
					
					
		}
		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static String getDiscuzHtml(String host, String url, String cookie) throws Exception{
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpHost proxy = new HttpHost("10.1.180.24", 3128, "http");
//		AuthScope authscope=new AuthScope("10.1.180.24", 3128);
//        Credentials credentials=new NTCredentials("heybc", "he150693", null, null);
//        httpClient.getCredentialsProvider().setCredentials(authscope,credentials);
//		
//		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpHost targetHost = new HttpHost(host);
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Cookie", cookie);
		HttpResponse httpResponse = httpClient.execute(targetHost, httpGet);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 此处重定向处理 此处还未验证
			String newUri = httpResponse.getLastHeader("Location").getValue();
			httpClient = new DefaultHttpClient();
			httpGet = new HttpGet(newUri);
			httpResponse = httpClient.execute(targetHost, httpGet);
		}

		// Get hold of the response entity
		HttpEntity entity = httpResponse.getEntity();
		//StringBuffer sb = new StringBuffer();
		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
//			InputStream is = entity.getContent();
//			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//			String temp = null;
//			
//			while((temp = br.readLine()) != null){
//				sb = sb.append(temp);
//			}
//			br.close();
//			is.close();
			String charSet = "";
//
			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			charSet = EntityUtils.getContentCharSet(entity);
//
			System.out.println("In header: " + charSet);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				m.find();
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			result = new String(bytes, charSet);
			//result = sb.toString();//).getBytes(), charSet);
		}

		httpClient.getConnectionManager().shutdown();
		return result;
	}
	
	public static void main(String [] args) throws ClientProtocolException, IOException{  
        DefaultHttpClient httpclient = new DefaultHttpClient();// 得到httpclient实例  
        HttpResponse response = null;  
        HttpEntity entity = null;  
        StringBuffer sb=null;  
        int pos;  
        String loginFormhash=null;  
        String post_formhash=null;  
        String username="admin";  
        String password="3215661";  
          
          
        //得到login formhash  
        HttpGet httpget = new HttpGet("http://www.ohjie.cc");  
        response=httpclient.execute(httpget);  
        entity = response.getEntity();  
        //输出页面内容  
        if (entity != null) {  
            String charset = EntityUtils.getContentCharSet(entity);  
            InputStream is = entity.getContent();  
            sb = new StringBuffer();  
            BufferedReader br = new BufferedReader(new InputStreamReader(is,  
                    charset));  
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            is.close();  
            //System.out.println(sb);  
        }  
        pos = sb.indexOf("name=\"formhash\" value=");    
        // System.out.println(sb);    
        // 找出这个 formhash 的内容，这是登录用的 formhash    
        loginFormhash = sb.substring(pos + 23, pos + 23 + 8);   
        System.out.println(loginFormhash);  
          
     // 登陆请求  
        HttpPost httpost = new HttpPost("http://www.ohjie.cc/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes");  
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
        nvps.add(new BasicNameValuePair("username", username));  
        nvps.add(new BasicNameValuePair("password", password));  
        nvps.add(new BasicNameValuePair("formhash", loginFormhash));  
        httpost.setEntity(new UrlEncodedFormEntity(nvps, "GB2312"));  
        response = httpclient.execute(httpost);  
        entity = response.getEntity();  
        //输出页面内容以供查看  
        if (entity != null) {  
            String charset = EntityUtils.getContentCharSet(entity);  
            InputStream is = entity.getContent();  
            sb = new StringBuffer();  
            BufferedReader br = new BufferedReader(new InputStreamReader(is,  
                    charset));  
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            is.close();  
            //打印，可以看到已经登录  
            System.out.println(sb);  
        }  
        EntityUtils.consume(entity);  
          
        //得到post  formhash  
        HttpGet httpget2 = new HttpGet("http://ohjie.cc/forum.php?mod=post&action=newthread&fid=37");  
        response=httpclient.execute(httpget2);  
        entity = response.getEntity();  
        //输出页面内容  
        if (entity != null) {  
            String charset = EntityUtils.getContentCharSet(entity);  
            InputStream is = entity.getContent();  
            sb = new StringBuffer();  
            BufferedReader br = new BufferedReader(new InputStreamReader(is,  
                    charset));  
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            is.close();  
            //打印发现没有登录，因此一下的formhash也并非发帖formhash  
            System.out.println(sb);  
        }  
        pos = sb.indexOf("name=\"formhash\" value=");    
        // System.out.println(temp);    
        // 找出这个 formhash 的内容，这是登录用的 formhash    
        post_formhash = sb.substring(pos + 23, pos + 23 + 8);   
        System.out.println(post_formhash);  
          
                 
     // 发文请求  
        HttpPost httpost2 = new HttpPost("http://bbs.ohjie.cc/forum.php?mod=post&action=newthread&fid=37&extra=&topicsubmit=yes");  
          
        httpost.addHeader("Referer", "http://bbs.ohjie.cc/forum.php?mod=post&action=newthread&fid=37&extra=&topicsubmit=yes&referer=http://bbs.ohjie.cc/forum.php?mod=forumdisplay&fid=37");  
        List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();  
        nvps2.add(new BasicNameValuePair("message", "科技爱上的科技和萨科技的哈萨克机会了卡机是"));  
        nvps2.add(new BasicNameValuePair("subject", "标题会计师的空间哈好尽快哈空间哈"));  
        nvps2.add(new BasicNameValuePair("formhash", post_formhash));  
        nvps.add(new BasicNameValuePair("allownoticeauthor", "1"));  
        nvps.add(new BasicNameValuePair("wysiwyg", "1"));  
        //nvps.add(new BasicNameValuePair("posttime", "1319100305"));  
        httpost2.setEntity(new UrlEncodedFormEntity(nvps2, "GB2312"));  
        response = httpclient.execute(httpost2);  
        entity = response.getEntity();  
        if (entity != null) {  
            String charset = EntityUtils.getContentCharSet(entity);  
            InputStream is = entity.getContent();  
            sb = new StringBuffer();  
            BufferedReader br = new BufferedReader(new InputStreamReader(is,  
                    charset));  
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            is.close();  
            System.out.println(sb);  
        }  
        EntityUtils.consume(entity);  
          
    }  
}

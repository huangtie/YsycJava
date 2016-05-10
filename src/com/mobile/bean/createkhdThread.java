package com.mobile.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.mobile.service.util.Logtool;

public class createkhdThread extends Thread {
		String userid;
		String pwd;
		public createkhdThread() {
			// TODO Auto-generated constructor stub
		}
		public createkhdThread(String userid,String pwd) {
		 this.userid=userid;
		 this.pwd=pwd;
		}
		public void run() {
//			Logtool.pringlogtolocal(userid+" login start \b ");
			StringBuilder sendmsg=new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sendmsg.append("<sent>");
			sendmsg.append("<key>client_auth</key>");
			sendmsg.append("<timestamp>"+System.currentTimeMillis()+"</timestamp>");
			sendmsg.append("<data>");
			sendmsg.append("<account>"+userid+"</account>");
			sendmsg.append("<password>"+pwd+"</password>");
			sendmsg.append("<channel>android</channel>");
			sendmsg.append("</data>");
			sendmsg.append("</sent>\b");
			Socket sock = null;
			try {
				sock = new Socket("localhost",28888);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			OutputStream out = null;
			try {
				out = sock.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InputStream sin = null;
			try {
				sin = sock.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte buf [] = new byte[512];
			buf= sendmsg.toString().getBytes();
			try {
				out.write(buf);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Logtool.pringlogtolocal("send ok ---> " + sin + "     Out: " + out);
			byte ibuf[] = new byte[512];
			int len = 0;
			try {
				len = sin.read(ibuf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Logtool.pringlogtolocal(userid+" login success \b ");
//			Logtool.pringlogtolocal(userid+" getoffline_message start \b ");
			if (ibuf!=null&&ibuf.length>0) {
				String s = new String(ibuf, 0, len-1);
				StringBuilder getallmsg=new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				getallmsg.append("<sent>");
				getallmsg.append("<key>client_get_offline_message</key>");
//				getallmsg.append("<timestamp>1386403273483</timestamp>");
				getallmsg.append("<data>");
				getallmsg.append("<account>1</account>");
				getallmsg.append("</data>");
				getallmsg.append("</sent>\b");
				
				out = null;
				try {
					out = sock.getOutputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sin = null;
				try {
					sin = sock.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				buf= getallmsg.toString().getBytes();
				try {
					out.write(buf);
//					Logtool.pringlogtolocal(userid+" getoffline_message success \b ");
				}catch (IOException e){
					e.printStackTrace();
				}
//				Logtool.pringlogtolocal("send ok ---> " + sin + "     Out: " + out);
				len = 0;
				try {
					len = sin.read(ibuf);
				} catch (IOException e) {
					e.printStackTrace();
				}
//				s = new String(ibuf, 0, len-1);
//				System.out.println(len + s);
				while (true) {
					try {
						sin = sock.getInputStream();
						try {
							len = sin.read(ibuf);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						s = new String(ibuf, 0, len-1);
//						Logtool.pringlogtolocal(userid+" get push success  \b"  );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						sin=null;
					}
				}
			}else {
				Logtool.pringlogtolocal(userid+" false  \b");
			}
//			System.out.println(len + s);
			
			
	
		}
		
 
	}
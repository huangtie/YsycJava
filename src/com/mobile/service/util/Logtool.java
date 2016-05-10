package com.mobile.service.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logtool {
	public static void pringlogtolocal(String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		// 日志输出
		String fileName = "D:" + File.separator +df.format(new Date())+".txt";
		File f = new File(fileName);
		if (f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(f, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content=tools.getNewTime()+" "+content+"\r\n";
		byte[] b = content.getBytes();
		for (int i = 0; i < b.length; i++) {
			try {
				out.write(b[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Logtool.pringlogtolocal("登陆");

	}

}

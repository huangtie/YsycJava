package com.mobile.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Captcha {
	private String phone;
	private int captcha;
	private long time;

	public Captcha(String phone) {
		this.phone = phone;
		captcha = setCapthca();
		time = getDate();
		// System.out.println(phone);
		// System.out.println(captcha);
		// System.out.println(time);
	}

	private int setCapthca() {
		int c = (int) (Math.random() * 10 + Math.random() * 100 + Math.random()
				* 1000 + Math.random() * 10000);
		return c < 1000 ? c * 10 : (c > 9999 ? c / 10 : c);
	}

	public long getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return Long.parseLong(dateFormat.format(new Date()));

	}

	public String getphone() {
		return phone;
	}

	public int getcaptcha() {
		return captcha;
	}

	public void setcaptcha(int captcha) {
		this.captcha = captcha;
	}

	public long gettime() {
		return time;
	}
}

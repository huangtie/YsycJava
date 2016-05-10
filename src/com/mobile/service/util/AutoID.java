package com.mobile.service.util;

public class AutoID {
	static int i = 0;

	public static String createId() {
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		String seq = "";
		++i;
		if (i > 999999) {
			i = 1;
		}
		Integer num = new Integer(i);

		if ((6 - num.toString().length()) == 0) {
			seq = num.toString();
		} else {
			for (int n = 0; n < (6 - num.toString().length()); n++) {
				seq = seq + "0";
			}
			seq = seq + num.toString();
		}
		// int iNumbers = Integer.parseInt("9");
		// float iPrice = Float.parseFloat(s)parseDouble(Price);
		return f.format(new java.util.Date()) + seq;
	}

	public static void main(String[] args) {
		System.out.println(AutoID.createId());
	}

}

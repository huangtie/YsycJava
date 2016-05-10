package com.mobile.service.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Getnetmd5 {
	static ByteArrayOutputStream out = null;

	public static String md5(String key) {
		// String urlStr =
		// "http://www.e-life365.cn/get_pass.ashx?upass=";//?receiver=2&sender=1&title=&type=0&content=%E5%8F%91%E5%8A%A8%E6%9C%BA%E7%9A%84&fileType=1";
		// StringBuilder urlBur = new StringBuilder(urlStr);
		// URL url = null;
		// try {
		// urlBur.append(key);
		// url = new URL(urlBur.toString());
		// } catch (MalformedURLException e1) {
		// e1.printStackTrace();
		// }
		// InputStream in = null;
		// out = new ByteArrayOutputStream();
		// try {
		// URLConnection urlConn = url.openConnection();
		// urlConn.connect();
		// in = urlConn.getInputStream();
		// IOUtils.copy(in, out);
		// out.flush();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (in != null) {
		// try {
		// in.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		//
		// return out.toString();
		return hmacSign(key, "qosjdngtoewtjd130459");
	}

	public static void main(String[] args) {
		System.out.println(Getnetmd5.md5("123123"));
	}

	public static final String ENCODE = "GBK"; // UTF-8

	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(ENCODE);
			value = aValue.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String digest(String aValue) {
		aValue = aValue.trim();
		byte value[];
		try {
			value = aValue.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			value = aValue.getBytes();
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return toHex(md.digest(value));
	}

	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

}

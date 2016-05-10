package com.mobile.service.util;

import com.alipay.sign.Base64;

//import org.apache.commons.codec.binary.Base64;


/**
 * 甯歌Base64鍔犲瘑瑙ｅ瘑瀹炵敤宸ュ叿绫�
 * 璇存槑锛�
 * 浣滆�咃細浣曟潹(heyang78@gmail.com)
 * 鍒涘缓鏃堕棿锛�2010-11-29 涓婂崍07:52:01
 * 淇敼鏃堕棿锛�2010-11-29 涓婂崍07:52:01
 */
public class Base64SecurityUtil{
    /**
     * 寰楀埌Base64鍔犲瘑鍚庣殑瀛楃涓�
     * 
     * 璇存槑锛�
     * @param originalString
     * @return
     * 鍒涘缓鏃堕棿锛�2010-11-29 涓婂崍07:53:30
     */
    public static String getEncryptString(String originalString){
        String arr = Base64.encode(originalString.getBytes());
        return new String(arr);
    }
    
    /**
     * 寰楀埌Base64瑙ｅ瘑鍚庣殑瀛楃涓�
     * 
     * 璇存槑锛�
     * @param encryptString
     * @return
     * 鍒涘缓鏃堕棿锛�2010-11-29 涓婂崍07:56:02
     */
    public static String getDecryptString(String encryptString){
        byte[] arr = Base64.decode(encryptString);
        return new String(arr);
    }
    public static void main(String[] args) {
		System.out.println(getDecryptString(getEncryptString("116掳23鈥�17,39掳54鈥�27")));
	}
}
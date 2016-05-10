package com.mobile.service.util;

import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.provider.SecureRandom;

public class Aes {
	// ����
	@SuppressWarnings("deprecation")
	public static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("KeyΪ��null");
			return null;
		}
		// �ж�Key�Ƿ�Ϊ16λ
		if (sKey.length() != 16) {
			System.out.print("Key���Ȳ���16λ");
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "�㷨/ģʽ/���뷽ʽ"
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// ʹ��CBCģʽ����Ҫһ������iv�������Ӽ����㷨��ǿ��
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		String enString = byte2hex(encrypted).toLowerCase();//new BASE64Encoder().encode(encrypted);
		//enString = URLEncoder.encode(enString);
		return enString;// �˴�ʹ��BASE64��ת�빦�ܣ�ͬʱ����2�μ��ܵ����á�
	}

	// ����
	public static String Decrypt(String sSrc, String sKey) throws Exception {
			// �ж�Key�Ƿ���ȷ
			if (sKey == null) {
				System.out.print("KeyΪ��null");
				return null;
			}
			// �ж�Key�Ƿ�Ϊ16λ
			if (sKey.length() != 16) {
				System.out.print("Key���Ȳ���16λ");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(
					"0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			//String deString = URLDecoder.decode(sSrc);
			byte[] encrypted1 = hex2byte(sSrc);//new BASE64Decoder().decodeBuffer(sSrc);// ����base64����
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
		 
	}
	


	public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    /**    
     * BASE64����   
   * @param key          
     * @return          
     * @throws Exception          
     */              
    public static byte[] decryptBASE64(String key) throws Exception {               
        return (new BASE64Decoder()).decodeBuffer(key);               
    }               
                  
    /**         
     * BASE64����   
   * @param key          
     * @return          
     * @throws Exception          
     */              
    public static String encryptBASE64(byte[] key) throws Exception {               
        return (new BASE64Encoder()).encodeBuffer(key);               
    }       
	public static void main(String[] args) throws Exception {
		/*
		 * �����õ�Key ������26����ĸ��������ɣ���ò�Ҫ�ñ����ַ�����Ȼ�����������ô�þ������˿��������
		 * �˴�ʹ��AES-128-CBC����ģʽ��key��ҪΪ16λ��
		 */
//		String cKey = "1234567890123456";
//		// ��Ҫ���ܵ��ִ�
//		String cSrc = "1";
//		System.out.println(cSrc);
//		// ����
//		long lStart = System.currentTimeMillis();
//		String enString = Aes.Encrypt(cSrc, cKey);
//		System.out.println("���ܺ���ִ��ǣ�" + enString);
//
//		long lUseTime = System.currentTimeMillis() - lStart;
//		System.out.println("���ܺ�ʱ��" + lUseTime + "����");
//		// ����
//		lStart = System.currentTimeMillis();
////		String DeString = Aes.Decrypt("56496067bb6b89e335e19e8732efaf121", cKey);
//		System.out.println("���ܺ���ִ��ǣ�" + DeString);
//		lUseTime = System.currentTimeMillis() - lStart;
//		System.out.println("���ܺ�ʱ��" + lUseTime + "����");
//		String a=" \n fdf\fdfd\n ffa������";
//		String a1=URLEncoder.encode(a);
//		System.out.println(a1);
//		System.out.println((new BASE64Encoder()).encodeBuffer(a.getBytes()));
//		System.out.println( (new BASE64Decoder()).decodeBuffer((new BASE64Encoder()).encodeBuffer(a.getBytes())));
//		 String data = Aes.encryptBASE64(" \n fd �Ǻ�/n fdf ".getBytes());     
//	        System.out.println("����ǰ��"+data);     
//	             
//	        byte[] byteArray = Aes.decryptBASE64(data);     
//	        System.out.println("���ܺ�"+new String(byteArray));
//		System.out.println(System.currentTimeMillis());
		 String aa="1111111";
		 String binqian=Aes.Encrypt(aa.toString(),
					Constants.ENCODE_KEY);
		 System.out.println(binqian);
		 
		 String bingqinhou=Aes.Decrypt(binqian, Constants.ENCODE_KEY);
		 System.out.println(bingqinhou);
	}
}

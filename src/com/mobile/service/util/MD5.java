package com.mobile.service.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
/*
 * MD5 �㷨
*/
public class MD5 {
    
    // ȫ������
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5() {
    }

    // ������ʽΪ���ָ��ַ���
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // ������ʽֻΪ����
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // ת���ֽ�����Ϊ16�����ִ�
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() �ú�������ֵΪ��Ź�ϣֵ�����byte����
            try {
				resultString = byteToString(md.digest(strObj.getBytes("unicode")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("md5="+resultString);
            try {
				resultString=Base64.encode(md.digest(strObj.getBytes("unicode")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("base64���ܺ�="+resultString);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    //����
    public static String reMD5Code(String strObj) {
        String resultString = null;
        resultString=byteToString(Base64.decode(strObj));
		System.out.println("base64���ܺ�="+resultString);
        return resultString;
    }
    public static void main(String[] args) {
//        MD5 getMD5 = new MD5();
//        String base64Str=getMD5.GetMD5Code("xjch1989");
//        reMD5Code(base64Str);
//        System.out.println(getMD5.GetMD5Code("hello\n"));
        
        //���Ե���c#
    	
        
    }
}
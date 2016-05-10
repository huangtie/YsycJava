package com.mobile.service.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;

public class PushUtil {
    //����"Java SDK ��������"�� "�ڶ��� ��ȡ����ƾ֤ "�л�õ�Ӧ�����ã��û����������滻
    private static String appId = "IOWRAEOnXz7mZh1Y5bXZk2";
    private static String appKey = "SHr8wH1FJb6D5J2vJ9hnA3";
    private static String masterSecret = "cO99JNZLtd7sT7b8dH6Ux7";
    
    private static String url = "http://sdk.open.api.igexin.com/apiex.htm";
//
//    static String CID = "e605a0db5ce3cca9b76b012978064940";
//  //�������ͷ�ʽ
//   // static String Alias = "";
//    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void main(String[] args) throws IOException {

        IGtPush push = new IGtPush(url, appKey, masterSecret);

        // ����"������Ӵ�֪ͨģ��"�������ñ��⡢���ݡ�����
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle("��ӭʹ�ø���!");
        template.setText("����һ��������Ϣ~");
        template.setUrl("http://getui.com");

        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        // ����"AppMessage"������Ϣ����������Ϣ����ģ�塢���͵�Ŀ��App�б��Ƿ�֧�����߷��͡��Լ�������Ϣ��Ч��(��λ����)
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(message);
        System.out.println("����"+ret.getResponse().toString());
    }
	
//    public static void main(String[] args) throws Exception {
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        LinkTemplate template = linkTemplateDemo();
//        SingleMessage message = new SingleMessage();
//        message.setOffline(true);
//        // ������Чʱ�䣬��λΪ���룬��ѡ
//        message.setOfflineExpireTime(24 * 3600 * 1000);
//        message.setData(template);
//        // ��ѡ��1Ϊwifi��0Ϊ���������绷���������ֻ����ڵ���������������Ƿ��·�
//        message.setPushNetWorkType(0); 
//        Target target = new Target();
//        target.setAppId(appId);
//        target.setClientId(CID);
//        //target.setAlias(Alias);
//        IPushResult ret = null;
//        try {
//            ret = push.pushMessageToSingle(message, target);
//        } catch (RequestException e) {
//            e.printStackTrace();
//            ret = push.pushMessageToSingle(message, target, e.getRequestId());
//        }
//        if (ret != null) {
//            System.out.println(ret.getResponse().toString());
//        } else {
//            System.out.println("��������Ӧ�쳣");
//        }
//    }
//    public static LinkTemplate linkTemplateDemo() {
//        LinkTemplate template = new LinkTemplate();
//        // ����APPID��APPKEY
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//        // ����֪ͨ������������
//        template.setTitle("������֪ͨ������");
//        template.setText("������֪ͨ������");
//        // ����֪ͨ��ͼ��
//        template.setLogo("icon.png");
//        // ����֪ͨ������ͼ�꣬��дͼ��URL��ַ
//        template.setLogoUrl("");
//        // ����֪ͨ�Ƿ����壬�𶯣����߿����
//        template.setIsRing(true);
//        template.setIsVibrate(true);
//        template.setIsClearable(true);
//        // ���ô򿪵���ַ��ַ
//        template.setUrl("http://www.baidu.com");
//        return template;
//    }
    
    public static void testPushxxx(){

    	
        IGtPush push = new IGtPush(url, appKey, masterSecret);

        // ����"������Ӵ�֪ͨģ��"�������ñ��⡢���ݡ�����
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle("��ӭʹ�ø���!");
        template.setText("����һ��������Ϣ~");
        template.setUrl("http://getui.com");

        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        // ����"AppMessage"������Ϣ����������Ϣ����ģ�塢���͵�Ŀ��App�б��Ƿ�֧�����߷��͡��Լ�������Ϣ��Ч��(��λ����)
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(message);
        System.out.println("����"+ret.getResponse().toString());
    }
}

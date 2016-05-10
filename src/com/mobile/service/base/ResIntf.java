package com.mobile.service.base;

import java.util.List;
import java.util.Map;

public interface ResIntf {

	public Map<String, Object> dataWithAuthorInfo(Map<String, Object> mapdata)  throws Exception;
	
	public Map<String, Object> dataWithUserInfo(Map<String, Object> mapdata , String c)  throws Exception;
	
	Map<String, Object> dataWithBookInfo(Map<String, Object> mapdata , String c , boolean isList)  throws Exception;
	
	public Map<String, Object> dataWithComment(Map<String, Object> mapdata)  throws Exception;
	
	//��ȡ˵˵����
	public Map<String, Object> dataWithTalk(Map<String, Object> mapdata)  throws Exception;
	
	//��ȡĳ��˵˵�������б�
	public List<Map <String , Object>> dataWithTalkComment(String offset , String limit , String talkID) throws Exception;
	
	//��ȡĳ��˵˵�����б�
	public List<Map <String , Object>> dataWithTalkZans(String offset , String limit , String talkID) throws Exception;
	
	//��ȡ��Ʒ��Ϣ
	public Map<String, Object> dataWithGood(Map<String, Object> mapdata , boolean isList)  throws Exception;
	
	//��ȡ�Ƿ��ѹ�����鼮
	boolean bookAlredyBuy(String bookID , String userID) throws Exception;
	
	//�Ƿ�����֤
	public boolean idcarAlredyReview(String userID) throws Exception;
}

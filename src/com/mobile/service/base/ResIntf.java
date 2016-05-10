package com.mobile.service.base;

import java.util.List;
import java.util.Map;

public interface ResIntf {

	public Map<String, Object> dataWithAuthorInfo(Map<String, Object> mapdata)  throws Exception;
	
	public Map<String, Object> dataWithUserInfo(Map<String, Object> mapdata , String c)  throws Exception;
	
	Map<String, Object> dataWithBookInfo(Map<String, Object> mapdata , String c , boolean isList)  throws Exception;
	
	public Map<String, Object> dataWithComment(Map<String, Object> mapdata)  throws Exception;
	
	//获取说说详情
	public Map<String, Object> dataWithTalk(Map<String, Object> mapdata)  throws Exception;
	
	//获取某条说说的评论列表
	public List<Map <String , Object>> dataWithTalkComment(String offset , String limit , String talkID) throws Exception;
	
	//获取某条说说的赞列表
	public List<Map <String , Object>> dataWithTalkZans(String offset , String limit , String talkID) throws Exception;
	
	//获取拍品信息
	public Map<String, Object> dataWithGood(Map<String, Object> mapdata , boolean isList)  throws Exception;
	
	//获取是否已购买该书籍
	boolean bookAlredyBuy(String bookID , String userID) throws Exception;
	
	//是否已认证
	public boolean idcarAlredyReview(String userID) throws Exception;
}

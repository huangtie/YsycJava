package com.mobile.service.shangjianwang.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MeService {
	/**
	 * �����û�����
	 */
	Boolean updatauserinfo(Map<String, Object> attrMap) throws Exception;

	/**
	 * ��֤����
	 * 
	 * @return
	 * @throws Exception
	 */
	Boolean checkPwd(Map<String, Object> attrMap) throws Exception;

	/**
	 * ͳ�Ƹ��ݵ�¼��־��ȡ������Ʒ����
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Long paidcount(Map<String, Object> attrMap) throws Exception;

	/**
	 * ���ݵ�¼��־��ȡ������Ʒ
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getupaiedsbyc(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 2.1.8. ���ݵ�¼��־��ȡ������ϸ����
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getuserinfobyc(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡ�鼮���� ����
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbooktypes() throws Exception;
	
	/**
	 * ��ȡ����鼮
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getfreebooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ͳ�����ͼ���ж��ٱ�
	 * 
	 * @return
	 * @throws Exception
	 */
	Long countfreebook(Map<String, Object> attrMap) throws Exception;

	/**
	 * ��ȡ�ѹ���
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbuyedbooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ͳ���ѹ���
	 * 
	 * @return
	 * @throws Exception
	 */
	Long countbuyedbook(Map<String, Object> attrMap) throws Exception;

	/**
	 * ��ȡ�����ܿ����飨������ѵĺ��ѹ��ģ�
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getallcanreadbooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ͳ�������ܿ����飨������ѵĺ��ѹ��ģ�
	 * 
	 * @return
	 * @throws Exception
	 */
	Long countcanreadbook(Map<String, Object> attrMap) throws Exception;

	/**
	 * 2.1.5. ��ȡ�������б�������ݹؼ��������б�
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbookcitybooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 2.1.6. ������id��ȡÿ�������ϸ�½�
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbookdetailbyid(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡ�Ƽ��鼮 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getRecommendBooks() throws Exception;

	/**
	 * ��ȡ�����鼮 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBestSellBooks() throws Exception;

	/**
	 * ��ȡ�� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getComment(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡ�� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	long getZan(Map<String, Object> attrMap) throws Exception;

	/**
	 * ���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> addComment(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * �� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> setZan(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡ����鼮 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getRelevant(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * �鼮���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addBookOrder(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ֧�� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> pay(Map<String, Object> attrMap) throws Exception;

	/**
	 * ��ȡ���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getOrders(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * �˻���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBalance(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ֵ���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addRechargeOrder(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡ�̳��鼮�б� 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAllBooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡ�̳��鼮�б� 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookDetail(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * �ѹ��鼮 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBuyBooks(Map<String, Object> attrMap)
			throws Exception;


	/**
	 * ��ѯ�����Ƿ�ע�� 2014-11-13
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	boolean checkPhone(Map<String, Object> attrMap) throws Exception;

	/**
	 * ��������¼ 2014-11-17
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> loginThird(String attrMap) throws Exception;

	/**
	 * ���ֻ� 2014-11-18
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	boolean bindingPhone(String id, String phone) throws Exception;

	/**
	 * �޸����� 2014-11-18
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	int changePWD(String id, String oldpwd, String newpwd) throws Exception;

	/**
	 * �һ����� 2014-11-18
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	int findPWD(String name, String phone, String newpwd) throws Exception;

	/**
	 * ���ݶ���ID��ȡ�������� 2015-01-21
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getOrderDetail(String order_id) throws Exception;

	/**
	 * �����û�ID��ȡ�û����� 2015-01-22
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> geturserdetailbyid(String user_id) throws Exception;

	/**
	 * �޸��û����� 2015-01-23
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploaduserinformation(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ��ȡר���Ŷ��б� 2015-01-23
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getexpertlist() throws Exception;

	/**
	 * ����ר��ID��ȡר������ 2015-01-23
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getexpertdetailbyid(String expertid) throws Exception;

	/**
	 * ��ȡ��Ѷ�б� 2015-01-24
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getnewslist(String offset, String limit)
			throws Exception;

	/**
	 * ������ѶID��ȡ��Ѷ���� 2015-01-24
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getnewsdetailbyid(String newid) throws Exception;

	/**
	 * ��ȡ��ϵ�������� 2015-01-26
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getcontactus() throws Exception;

	/**
	 * ��ȡ�ҵ��ջ���ַ�б� 2015-01-26
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getmyadress(String user_id) throws Exception;

	/**
	 * ��ȡʡ�б� 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getshenglist() throws Exception;

	/**
	 * ����ʡID��ȡ���б� 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getshilist(String code) throws Exception;

	/**
	 * ������ID��ȡ���б� 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getqulist(String code) throws Exception;

	/**
	 * �����ջ���ַ2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addadress(Map<String, Object> attrMap) throws Exception;

	/**
	 * �޸��ջ���ַ2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateadress(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * ����Ĭ�ϵ�ַ 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> setdefultadress(String userid, String adressid)
			throws Exception;

	/**
	 * ɾ���ջ���ַ 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> deleteadress(String adressid) throws Exception;

	/**
	 * �ϴ��޸�ͷ�� 2015-02-01
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploaduserfaceimage(String userid, String url) throws Exception;
	
	
	
	
	
	
	/**
	 * ��¼ 2.1
	 * 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> login(HttpServletRequest request) throws Exception;

	/**
	 * ע�� 2.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> register(HttpServletRequest request)
			throws Exception;
	
	/**
	 * �һ����� 2.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean finepassword(HttpServletRequest request)
			throws Exception;
	
	/**
	 * ��ȡ�û����� 2.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getUserInfo(HttpServletRequest request)
			throws Exception;
	
	/**
	 * �޸����� 2.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean updateUserInfo(HttpServletRequest request)
			throws Exception;
	
	/**
	 * �ϴ�ͷ�� 2.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploadPhoto(HttpServletRequest request)
			throws Exception;
	
	/**
	 * ��ȡͼ���б� 3.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBookList(HttpServletRequest request)
			throws Exception;
	
	/**
	 * ��ȡͼ������ 3.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookInformation(HttpServletRequest request)
			throws Exception;
	
	/**
	 * ��ȡͼ��������Դ 3.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookDownData(HttpServletRequest request)
			throws Exception;
	
	/**
	 * �������� 4.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean sendComment(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�����б� 4.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBookComments(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡiap�б� 5.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getiapPriceList(HttpServletRequest request) throws Exception;
	
	/**
	 * ����ͼ�� 5.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> buyWithBook(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�ѹ����ͼ�� 5.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBuyedBooks(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ��ֵ�����Ѽ�¼ 5.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getPurchaseLogs(HttpServletRequest request) throws Exception;
	
	/**
	 * �ӹ�ע 6.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> becomeFans(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�û��б� 6.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMemberList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�ҹ�ע�����б� 6.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getFansList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��˵˵ 6.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean sendTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * ɾ��˵˵ 6.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean deleteTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * ����˵˵ 6.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean commentTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * ����˵˵ 6.7
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean zanTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ˵˵���� 6.8
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getTalkDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * �鿴ĳ��˵˵������ 6.9
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getZansWithTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * �鿴ĳ��˵˵�������� 6.10
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getCommentsWithTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ˵˵�б� 6.11
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getTalkList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡͼ�������б� 7.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getGroupList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�ؼ��б� 7.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAuthorList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�ؼ����� 7.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAuthorDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�����б� 7.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getNewsList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ�������� 7.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getNewsDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ֪ͨ����(Banner) 7.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBannerList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡʡ�����б� 7.7
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getCityList(HttpServletRequest request) throws Exception;
	
	/**
	 * �����б� 8.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getGoodsList(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ��Ʒ���� 8.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getGoodDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * �������� 8.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean sendGoods(HttpServletRequest request) throws Exception;
	
	/**
	 * ���� 8.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean goodPay(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ��Ʒ�ĳ����б� 8.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getGoodPayList(HttpServletRequest request) throws Exception;
	
	/**
	 * �ɽ����� 8.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean finishGoodPay(HttpServletRequest request) throws Exception;
	
	/**
	 * ��Ʒ�ϼ�/�¼� 8.7
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean changeGoodStatus(HttpServletRequest request) throws Exception;
	
	/**
	 * �ϴ���֤ 9.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> sendIdCar(HttpServletRequest request) throws Exception;
	
	/**
	 * ��ȡ��֤״̬ 9.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getIdcarStatus(HttpServletRequest request) throws Exception;
	
}

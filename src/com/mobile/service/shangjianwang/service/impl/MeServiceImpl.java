package com.mobile.service.shangjianwang.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mobile.service.shangjianwang.dao.MeDao;
import com.mobile.service.shangjianwang.service.MeService;
import com.mobile.service.util.Aes;
import com.mobile.service.util.Constants;
import com.mobile.service.util.Convert;

public class MeServiceImpl implements MeService {

	private MeDao meDao;

	public MeDao getMeDao() {
		return meDao;
	}

	public void setMeDao(MeDao meDao) {
		this.meDao = meDao;
	}

	public Map<String, Object> login(HttpServletRequest request)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> tmp = meDao.login(request);
		if (tmp != null && tmp.size() > 0 && tmp.get("userID") != null) {
			String expireTime = Convert.dateToExpire();
			String src = tmp.get("userID") + "+" + expireTime;
			String enString = Aes.Encrypt(src, Constants.ENCODE_KEY);
			data.put("c", enString);
			data.put("userInfo", tmp);
		} else {
			data = null;
		}
		return data;
	}
	
	@Override
	public Map<String, Object> register(HttpServletRequest request)
			throws Exception {
		return meDao.register(request);
	}
	
	@Override
	public boolean finepassword(HttpServletRequest request)
			throws Exception{
		return meDao.finepassword(request);
	}
	
	@Override
	public Map<String, Object> getUserInfo(HttpServletRequest request)
			throws Exception{
		return meDao.getUserInfo(request);
	}
	
	@Override
	public boolean updateUserInfo(HttpServletRequest request)
			throws Exception{
		return meDao.updateUserInfo(request);
	}
	
	@Override
	public Map<String, Object> uploadPhoto(HttpServletRequest request)
			throws Exception{
		return meDao.uploadPhoto(request);
	}
	
	@Override
	public List<Map<String, Object>> getBookList(HttpServletRequest request)
			throws Exception{
		return meDao.getBookList(request);
	}
	
	@Override
	public Map<String, Object> getBookInformation(HttpServletRequest request)
			throws Exception{
		return meDao.getBookInformation(request);
	}
	
	@Override
	public Map<String, Object> getBookDownData(HttpServletRequest request)
			throws Exception{
		return meDao.getBookDownData(request);
	}
	
	@Override
	public boolean sendComment(HttpServletRequest request) throws Exception{
		return meDao.sendComment(request);
	}
	
	@Override
	public List<Map<String, Object>> getBookComments(HttpServletRequest request) throws Exception{
		return meDao.getBookComments(request);
	}
	
	@Override
	public List<Map<String, Object>> getiapPriceList(HttpServletRequest request) throws Exception{
		return meDao.getiapPriceList(request);
	}
	
	@Override
	public Map<String, Object> buyWithBook(HttpServletRequest request)
			throws Exception{
		return meDao.buyWithBook(request);
	}
	
	@Override
	public List<Map<String, Object>> getBuyedBooks(HttpServletRequest request) throws Exception{
	return meDao.getBuyedBooks(request);
	}
	
	@Override
	public List<Map<String, Object>> getPurchaseLogs(HttpServletRequest request) throws Exception{
	return meDao.getPurchaseLogs(request);
	}
	
	@Override
	public Map<String, Object> becomeFans(HttpServletRequest request) throws Exception{
		return meDao.becomeFans(request);
	}

	@Override
	public List<Map<String, Object>> getMemberList(HttpServletRequest request) throws Exception{
		return meDao.getMemberList(request);
	}
	
	@Override
	public List<Map<String, Object>> getFansList(HttpServletRequest request) throws Exception{
		return meDao.getFansList(request);
	}
	
	@Override
	public boolean sendTalk(HttpServletRequest request) throws Exception{
		return meDao.sendTalk(request);
	}
	
	@Override
	public boolean deleteTalk(HttpServletRequest request) throws Exception{
		return meDao.deleteTalk(request);
	}
	
	@Override
	public boolean commentTalk(HttpServletRequest request) throws Exception{
		return meDao.commentTalk(request);
	}
	
	@Override
	public boolean zanTalk(HttpServletRequest request) throws Exception{
		return meDao.zanTalk(request);
	}
	
	@Override
	public Map<String, Object> getTalkDetail(HttpServletRequest request) throws Exception{
		return meDao.getTalkDetail(request);
	}
	
	@Override
	public List<Map<String, Object>> getZansWithTalk(HttpServletRequest request) throws Exception{
		return meDao.getZansWithTalk(request);
	}
	
	@Override
	public List<Map<String, Object>> getCommentsWithTalk(HttpServletRequest request) throws Exception{
		return meDao.getCommentsWithTalk(request);
	}
	
	@Override
	public List<Map<String, Object>> getTalkList(HttpServletRequest request) throws Exception{
		return meDao.getTalkList(request);
	}
	
	@Override
	public List<Map<String, Object>> getGroupList(HttpServletRequest request) throws Exception{
		return meDao.getGroupList(request);
	}
	
	@Override
	public List<Map<String, Object>> getAuthorList(HttpServletRequest request) throws Exception{
		return meDao.getAuthorList(request);
	}
	
	@Override
	public Map<String, Object> getAuthorDetail(HttpServletRequest request) throws Exception{
		return meDao.getAuthorDetail(request);
	}
	
	@Override
	public List<Map<String, Object>> getNewsList(HttpServletRequest request) throws Exception{
		return meDao.getNewsList(request);
	}
	
	@Override
	public Map<String, Object> getNewsDetail(HttpServletRequest request) throws Exception{
		return meDao.getNewsDetail(request);
	}
	
	@Override
	public List<Map<String, Object>> getBannerList(HttpServletRequest request) throws Exception{
		return meDao.getBannerList(request);
	}
	
	@Override
	public List<Map<String, Object>> getCityList(HttpServletRequest request) throws Exception{
		return meDao.getCityList(request);
	}
	
	@Override
	public List<Map<String, Object>> getGoodsList(HttpServletRequest request) throws Exception{
		return meDao.getGoodsList(request);
	}
	
	@Override
	public Map<String, Object> getGoodDetail(HttpServletRequest request) throws Exception{
		return meDao.getGoodDetail(request);
	}
	
	@Override
	public boolean sendGoods(HttpServletRequest request) throws Exception{
		return meDao.sendGoods(request);
	}
	
	@Override
	public boolean goodPay(HttpServletRequest request) throws Exception{
		return meDao.goodPay(request);
	}
	
	@Override
	public List<Map<String, Object>> getGoodPayList(HttpServletRequest request) throws Exception{
		return meDao.getGoodPayList(request);
	}
	
	@Override
	public boolean finishGoodPay(HttpServletRequest request) throws Exception{
		return meDao.finishGoodPay(request);
	}
	
	@Override
	public boolean changeGoodStatus(HttpServletRequest request) throws Exception{
		return meDao.changeGoodStatus(request);
	}
	
	@Override
	public Map<String, Object> sendIdCar(HttpServletRequest request) throws Exception{
		return meDao.sendIdCar(request);
	}
	
	@Override
	public Map<String, Object> getIdcarStatus(HttpServletRequest request) throws Exception{
		return meDao.getIdcarStatus(request);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<Map<String, Object>> getfreebooks(Map<String, Object> attrMap)
			throws Exception {
		return meDao.getfreebooks(attrMap);
	}

	public Long countfreebook(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.countfreebook(attrMap);
	}

	public List<Map<String, Object>> getbuyedbooks(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getbuyedbooks(attrMap);
	}

	public Long countbuyedbook(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.countbuyedbook(attrMap);
	}

	public List<Map<String, Object>> getallcanreadbooks(
			Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getallcanreadbooks(attrMap);
	}

	public Long countcanreadbook(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.countcanreadbook(attrMap);
	}

	public List<Map<String, Object>> getbookcitybooks(
			Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getbookcitybooks(attrMap);
	}

	public Long countbookcitybook(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.countbookcitybook(attrMap);
	}

	public List<Map<String, Object>> getbookdetailbyid(
			Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getbookdetailbyid(attrMap);
	}

	@Override
	public List<Map<String, Object>> getbooktypes() throws Exception {
		return meDao.getbooktypes();
	}

	@Override
	public Map<String, Object> getuserinfobyc(Map<String, Object> attrMap)
			throws Exception {

		return meDao.getuserinfobyc(attrMap);
	}

	@Override
	public List<Map<String, Object>> getupaiedsbyc(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getupaiedsbyc(attrMap);
	}

	@Override
	public Long paidcount(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.paidcount(attrMap);
	}

	@Override
	public Boolean checkPwd(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.checkPwd(attrMap);
	}

	@Override
	public Boolean updatauserinfo(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.updatauserinfo(attrMap);
	}

	@Override
	public List<Map<String, Object>> getRecommendBooks() throws Exception {
		// TODO Auto-generated method stub
		return meDao.getRecommendBooks();
	}

	@Override
	public List<Map<String, Object>> getBestSellBooks() throws Exception {
		// TODO Auto-generated method stub
		return meDao.getBestSellBooks();
	}

	@Override
	public List<Map<String, Object>> getComment(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getComment(attrMap);
	}

	@Override
	public long getZan(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub

		return meDao.getZan(attrMap);
	}

	@Override
	public List<Map<String, Object>> addComment(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.addComment(attrMap);
	}

	@Override
	public List<Map<String, Object>> setZan(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.setZan(attrMap);
	}

	@Override
	public List<Map<String, Object>> getRelevant(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getRelevant(attrMap);
	}

	@Override
	public Map<String, Object> addBookOrder(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.addBookOrder(attrMap);
	}

	@Override
	public List<Map<String, Object>> pay(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.pay(attrMap);
	}

	@Override
	public List<Map<String, Object>> getOrders(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getOrders(attrMap);
	}

	@Override
	public Map<String, Object> getBalance(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getBalance(attrMap);
	}

	@Override
	public Map<String, Object> addRechargeOrder(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.addRechargeOrder(attrMap);
	}

	@Override
	public List<Map<String, Object>> getAllBooks(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getAllBooks(attrMap);
	}

	@Override
	public Map<String, Object> getBookDetail(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getBookDetail(attrMap);
	}

	@Override
	public List<Map<String, Object>> getBuyBooks(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getBuyBooks(attrMap);
	}

	@Override
	public boolean checkPhone(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		return meDao.checkPhone(attrMap);
	}

	@Override
	public Map<String, Object> loginThird(String uid) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> data = meDao.loginThird(uid);
		if (data != null && data.get("userID") != null) {
			String expireTime = Convert.dateToExpire();
			String src = data.get("userID") + "+" + expireTime;
			String enString = Aes.Encrypt(src, Constants.ENCODE_KEY);
			data.put("c", enString);
		} else {
			data = null;
		}
		return data;
	}

	@Override
	public boolean bindingPhone(String id, String phone) throws Exception {
		// TODO Auto-generated method stub
		return meDao.bindingPhone(id, phone);
	}

	@Override
	public int changePWD(String id, String oldpwd, String newpwd)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.changePWD(id, oldpwd, newpwd);
	}

	@Override
	public int findPWD(String name, String phone, String newpwd)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.findPWD(name, phone, newpwd);
	}

	@Override
	public Map<String, Object> getOrderDetail(String order_id) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getOrderDetail(order_id);
	}

	@Override
	public Map<String, Object> geturserdetailbyid(String user_id)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.geturserdetailbyid(user_id);
	}

	@Override
	public Map<String, Object> uploaduserinformation(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.uploaduserinformation(attrMap);
	}

	@Override
	public List<Map<String, Object>> getexpertlist() throws Exception {
		// TODO Auto-generated method stub
		return meDao.getexpertlist();
	}

	@Override
	public Map<String, Object> getexpertdetailbyid(String expertid)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getexpertdetailbyid(expertid);
	}

	@Override
	public List<Map<String, Object>> getnewslist(String offset, String limit)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getnewslist(offset, limit);
	}
	
	public List<Map<String, Object>> getnewsdatalist(String offset, String limit)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getnewsdatalist(offset, limit);
	}
	
	public Map<String, Object> getnewsdetail(String newsID)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getnewsdetail(newsID);
	}

	@Override
	public Map<String, Object> getnewsdetailbyid(String newid) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getnewsdetailbyid(newid);
	}
	
	public List<Map<String, Object>> getpaimailist(String offset, String limit)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getpaimailist(offset, limit);
	}
	
	public Map<String, Object> getpaimaiDetail(String paimaiID)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getpaimaiDetail(paimaiID);
	}
	

	@Override
	public Map<String, Object> getcontactus() throws Exception {
		// TODO Auto-generated method stub
		return meDao.getcontactus();
	}

	@Override
	public List<Map<String, Object>> getmyadress(String user_id)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getmyadress(user_id);
	}

	@Override
	public List<Map<String, Object>> getshenglist() throws Exception {
		// TODO Auto-generated method stub
		return meDao.getshenglist();
	}

	@Override
	public List<Map<String, Object>> getshilist(String code) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getshilist(code);
	}

	@Override
	public List<Map<String, Object>> getqulist(String code) throws Exception {
		// TODO Auto-generated method stub
		return meDao.getqulist(code);
	}

	@Override
	public Map<String, Object> addadress(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.addadress(attrMap);
	}

	@Override
	public Map<String, Object> updateadress(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.updateadress(attrMap);
	}

	@Override
	public Map<String, Object> setdefultadress(String userid, String adressid)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.setdefultadress(userid, adressid);
	}

	@Override
	public Map<String, Object> deleteadress(String adressid) throws Exception {
		// TODO Auto-generated method stub
		return meDao.deleteadress(adressid);
	}

	@Override
	public Map<String, Object> uploaduserfaceimage(String userid, String url) throws Exception {
		// TODO Auto-generated method stub
		return meDao.uploaduserfaceimage(userid, url);
	}

	public boolean isbuythisbook(String userID, String bookID) throws Exception {
		// TODO Auto-generated method stub
		return meDao.isbuythisbook(userID, bookID);
	}
	
	public boolean isfreebook(String bookID) throws Exception {
		// TODO Auto-generated method stub
		return meDao.isfreebook(bookID);
	}
	
	public List<Map<String, Object>> getallMember(String offset, String limit,String key)
			throws Exception {
		// TODO Auto-generated method stub
		return meDao.getallMember(offset, limit , key);
	}
	
}

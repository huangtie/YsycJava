package com.mobile.service.shangjianwang.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MeService {
	/**
	 * 更新用户数据
	 */
	Boolean updatauserinfo(Map<String, Object> attrMap) throws Exception;

	/**
	 * 验证密码
	 * 
	 * @return
	 * @throws Exception
	 */
	Boolean checkPwd(Map<String, Object> attrMap) throws Exception;

	/**
	 * 统计根据登录标志获取已拍作品数量
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Long paidcount(Map<String, Object> attrMap) throws Exception;

	/**
	 * 根据登录标志获取已拍作品
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getupaiedsbyc(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 2.1.8. 根据登录标志获取个人详细资料
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getuserinfobyc(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取书籍类型 新增
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbooktypes() throws Exception;
	
	/**
	 * 获取免费书籍
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getfreebooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 统计免费图书有多少本
	 * 
	 * @return
	 * @throws Exception
	 */
	Long countfreebook(Map<String, Object> attrMap) throws Exception;

	/**
	 * 获取已购买
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbuyedbooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 统计已购买
	 * 
	 * @return
	 * @throws Exception
	 */
	Long countbuyedbook(Map<String, Object> attrMap) throws Exception;

	/**
	 * 获取所有能看得书（包括免费的和已购的）
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getallcanreadbooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 统计所有能看得书（包括免费的和已购的）
	 * 
	 * @return
	 * @throws Exception
	 */
	Long countcanreadbook(Map<String, Object> attrMap) throws Exception;

	/**
	 * 2.1.5. 获取书城书的列表包括根据关键字搜索列表
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbookcitybooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 2.1.6. 跟据书id获取每本书的详细章节
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getbookdetailbyid(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取推荐书籍 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getRecommendBooks() throws Exception;

	/**
	 * 获取畅销书籍 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBestSellBooks() throws Exception;

	/**
	 * 获取评 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getComment(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取赞 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	long getZan(Map<String, Object> attrMap) throws Exception;

	/**
	 * 评论 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> addComment(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 赞 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> setZan(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取相关书籍 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getRelevant(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 书籍订单 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addBookOrder(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 支付 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> pay(Map<String, Object> attrMap) throws Exception;

	/**
	 * 获取订单 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getOrders(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 账户余额 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBalance(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 充值订单 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addRechargeOrder(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取商城书籍列表 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAllBooks(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取商城书籍列表 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookDetail(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 已购书籍 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBuyBooks(Map<String, Object> attrMap)
			throws Exception;


	/**
	 * 查询号码是否注册 2014-11-13
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	boolean checkPhone(Map<String, Object> attrMap) throws Exception;

	/**
	 * 第三方登录 2014-11-17
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> loginThird(String attrMap) throws Exception;

	/**
	 * 绑定手机 2014-11-18
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	boolean bindingPhone(String id, String phone) throws Exception;

	/**
	 * 修改密码 2014-11-18
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	int changePWD(String id, String oldpwd, String newpwd) throws Exception;

	/**
	 * 找回密码 2014-11-18
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	int findPWD(String name, String phone, String newpwd) throws Exception;

	/**
	 * 根据订单ID获取订单详情 2015-01-21
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getOrderDetail(String order_id) throws Exception;

	/**
	 * 根据用户ID获取用户详情 2015-01-22
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> geturserdetailbyid(String user_id) throws Exception;

	/**
	 * 修改用户资料 2015-01-23
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploaduserinformation(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 获取专家团队列表 2015-01-23
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getexpertlist() throws Exception;

	/**
	 * 根据专家ID获取专家详情 2015-01-23
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getexpertdetailbyid(String expertid) throws Exception;

	/**
	 * 获取资讯列表 2015-01-24
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getnewslist(String offset, String limit)
			throws Exception;

	/**
	 * 根据资讯ID获取资讯详情 2015-01-24
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getnewsdetailbyid(String newid) throws Exception;

	/**
	 * 获取联系我们内容 2015-01-26
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getcontactus() throws Exception;

	/**
	 * 获取我的收货地址列表 2015-01-26
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getmyadress(String user_id) throws Exception;

	/**
	 * 获取省列表 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getshenglist() throws Exception;

	/**
	 * 根据省ID获取市列表 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getshilist(String code) throws Exception;

	/**
	 * 根据市ID获取区列表 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getqulist(String code) throws Exception;

	/**
	 * 新增收货地址2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addadress(Map<String, Object> attrMap) throws Exception;

	/**
	 * 修改收货地址2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateadress(Map<String, Object> attrMap)
			throws Exception;

	/**
	 * 设置默认地址 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> setdefultadress(String userid, String adressid)
			throws Exception;

	/**
	 * 删除收货地址 2015-01-28
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> deleteadress(String adressid) throws Exception;

	/**
	 * 上传修改头像 2015-02-01
	 * 
	 * @param attrMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploaduserfaceimage(String userid, String url) throws Exception;
	
	
	
	
	
	
	/**
	 * 登录 2.1
	 * 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> login(HttpServletRequest request) throws Exception;

	/**
	 * 注册 2.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> register(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 找回密码 2.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean finepassword(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 获取用户资料 2.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getUserInfo(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 修改资料 2.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean updateUserInfo(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 上传头像 2.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> uploadPhoto(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 获取图集列表 3.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBookList(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 获取图集详情 3.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookInformation(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 获取图集下载资源 3.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookDownData(HttpServletRequest request)
			throws Exception;
	
	/**
	 * 发表评论 4.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean sendComment(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取评论列表 4.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBookComments(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取iap列表 5.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getiapPriceList(HttpServletRequest request) throws Exception;
	
	/**
	 * 购买图集 5.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> buyWithBook(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取已购买的图集 5.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBuyedBooks(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取充值和消费记录 5.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getPurchaseLogs(HttpServletRequest request) throws Exception;
	
	/**
	 * 加关注 6.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> becomeFans(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取用户列表 6.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMemberList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取我关注的人列表 6.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getFansList(HttpServletRequest request) throws Exception;
	
	/**
	 * 发说说 6.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean sendTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * 删除说说 6.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean deleteTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * 评论说说 6.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean commentTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * 点赞说说 6.7
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean zanTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取说说详情 6.8
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getTalkDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * 查看某条说说所有赞 6.9
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getZansWithTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * 查看某条说说所有评论 6.10
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getCommentsWithTalk(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取说说列表 6.11
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getTalkList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取图集分类列表 7.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getGroupList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取藏家列表 7.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAuthorList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取藏家详情 7.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAuthorDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取文章列表 7.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getNewsList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取新闻详情 7.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getNewsDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取通知公告(Banner) 7.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBannerList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取省市区列表 7.7
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getCityList(HttpServletRequest request) throws Exception;
	
	/**
	 * 拍卖列表 8.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getGoodsList(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取拍品详情 8.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getGoodDetail(HttpServletRequest request) throws Exception;
	
	/**
	 * 发布拍卖 8.3
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean sendGoods(HttpServletRequest request) throws Exception;
	
	/**
	 * 出价 8.4
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean goodPay(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取拍品的出价列表 8.5
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getGoodPayList(HttpServletRequest request) throws Exception;
	
	/**
	 * 成交拍卖 8.6
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean finishGoodPay(HttpServletRequest request) throws Exception;
	
	/**
	 * 拍品上架/下架 8.7
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	boolean changeGoodStatus(HttpServletRequest request) throws Exception;
	
	/**
	 * 上传认证 9.1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> sendIdCar(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取认证状态 9.2
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getIdcarStatus(HttpServletRequest request) throws Exception;
	
}

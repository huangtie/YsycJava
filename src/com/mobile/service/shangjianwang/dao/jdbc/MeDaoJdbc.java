package com.mobile.service.shangjianwang.dao.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mobile.service.base.CommonParam;
import com.mobile.service.base.PushUtil;
import com.mobile.service.base.ResIntf;
import com.mobile.service.base.Response;
import com.mobile.service.shangjianwang.dao.MeDao;
import com.mobile.service.util.Aes;
import com.mobile.service.util.ApacheAes;
import com.mobile.service.util.Base64SecurityUtil;
import com.mobile.service.util.Check;
import com.mobile.service.util.Constants;
import com.mobile.service.util.Convert;
import com.mobile.service.util.Getnetmd5;
import com.mobile.service.util.ImageUtil;

public class MeDaoJdbc implements MeDao {

	private JdbcTemplate jdbcTemplate;
	
	public static final String hostString="http://114.112.170.45:8080";

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JdbcTemplate sqljdbcTemplate;

	public JdbcTemplate getSqljdbcTemplate() {
		return sqljdbcTemplate;
	}

	public void setSqljdbcTemplate(JdbcTemplate sqljdbcTemplate) {
		this.sqljdbcTemplate = sqljdbcTemplate;
	}
	

	private Response response;
	
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Map<String, Object> Province(String ProID) throws Exception{
		String shengSQL = "select * from [T_Province] where ProID = '" + ProID + "'";
		return this.sqljdbcTemplate.queryForMap(shengSQL);
	}
	

	public Map<String, Object> login(HttpServletRequest request)
			throws Exception {
		CommonParam param = new CommonParam(request);
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");
		String userType = request.getParameter("userType");
		String userPart = request.getParameter("userPart");
		String userNick = request.getParameter("userNick");
		String sql = "";
		
		if(userType.equals("0"))
		{
			String sqlhave = "select count(*) from [user] where userTou='" + param.getUdid() + "'";
			int count = this.sqljdbcTemplate.queryForInt(sqlhave);
			if(count == 0)
			{
				Date date=new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date_str=df.format(date);
				String time = String.valueOf(System.currentTimeMillis()); 
				String sqlInser = "insert into [user] (userNick,userTou,userType,userTime) values('游客"
										+ time.substring(time.length()-4,time.length()) + "','"
										+ param.getUdid() +  "','" + userType + "','" + date_str + "')";
				
				this.sqljdbcTemplate.execute(sqlInser);						
			}
			sql = "SELECT  * from  [user]"
					+ "where userTou='"
					+ param.getUdid() + "'";
		}
		else if(userType.equals("1"))
		{
			userPassword = Base64SecurityUtil.getDecryptString(userPassword);
			userPassword = Getnetmd5.md5(userPassword);
			sql = "SELECT  * from  [user]"
					+ "where userName='"
					+ userName + "' and userPassword='" + userPassword
					+ "'";

		}
		else if(userType.equals("2") || userType.equals("3") || userType.equals("4"))
		{
			String sqlhave = "select count(*) from [user] where userPart='" + userPart + "'";
			int count = this.sqljdbcTemplate.queryForInt(sqlhave);
			if(count == 0)
			{
				Date date=new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date_str=df.format(date);
				String time = String.valueOf(System.currentTimeMillis());
				String sqlInser = "insert into [user] (userName,userNick,userPart,userType,userTime) values('第三方账号"
										+ time.substring(time.length()-4,time.length()) + "','"
										 + userNick + "','" + userPart +  "','" + userType + "','" + date_str + "')";
				
				this.sqljdbcTemplate.execute(sqlInser);						
			}
			sql = "SELECT  * from  [user]"
					+ "where userPart='"
					+ userPart + "'";
		}
		
		
		Map<String, Object> mapdata = null;
		Map<String, Object> result = null;
		List<Map<String, Object>> listData = this.sqljdbcTemplate
				.queryForList(sql);
		if (listData != null && listData.size() > 0) {
			mapdata = listData.get(0);
			result = this.response.dataWithUserInfo(mapdata , param.getC());
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> register(HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		CommonParam param = new CommonParam(request);
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");
		userPassword = Base64SecurityUtil.getDecryptString(userPassword);
		String userNick = request.getParameter("userNick");
		
		
		List<Map<String, Object>> list = this.sqljdbcTemplate
				.queryForList("select * from [user] where userName='"
						+ userName + "'");
		if (list.size() > 0) {
			return null;
		}
		String pass = Getnetmd5.md5(userPassword);
		if (pass.replaceAll(" ", "").equals("")) {
			return new HashMap<String, Object>();
		}
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		String sqlInser = "insert into [user] (userName,userNick,userType,userTime,userPassword) values('"
								+ userName + "','"
								 + userNick  +  "','" + "1" + "','" + date_str + "','" + pass + "')";
		
		
		this.sqljdbcTemplate.execute(sqlInser);		
		String sesql = "select * from [user] where username='" + userName + "'";
		Map<String, Object> mapdata = null;
		Map<String, Object> result = null;
		List<Map<String, Object>> listData = this.sqljdbcTemplate
				.queryForList(sesql);
		if (listData != null && listData.size() > 0) {
			mapdata = listData.get(0);
			result = this.response.dataWithUserInfo(mapdata , param.getC());
		}
		
		return result;
	}
	
	public boolean finepassword(HttpServletRequest request) throws Exception {
		CommonParam param = new CommonParam(request);
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");
		String userPhone = request.getParameter("userPhone");
		try {

			String sql = "select * from [user] where userName = '" + userName + "'";
			List<Map<String, Object>> listData = this.sqljdbcTemplate.queryForList(sql);
			if(listData.size() == 0)
			{
				return false;
			}
			else
			{
				Map<String, Object> map = listData.get(0);
				if(map.get("userPhone").toString().equals(userPhone))
				{
					String upSQL = "update [user] SET userPassword = '" + Getnetmd5.md5(Base64SecurityUtil.getDecryptString(userPassword)) + "' where userName = '" + userName + "'";
					sqljdbcTemplate.execute(upSQL);
					return true;
				}
				else
				{
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Map<String, Object> getUserInfo(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String userID = request.getParameter("userID");
		
		try {
			List<Map<String, Object>> list = this.sqljdbcTemplate
					.queryForList("select * from [user] where userID='"
							+ userID + "'");
			if (list.size() == 0) {
				return null;
			}
						
			return this.response.dataWithUserInfo(list.get(0) , param.getC());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean updateUserInfo(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String userNick = request.getParameter("userNick");
		String userPhone = request.getParameter("userPhone");
		String userEmail = request.getParameter("userEmail");
		String userSex = request.getParameter("userSex");
		String userSheng = request.getParameter("userSheng");
		String userShi = request.getParameter("userShi");
		String userQu = request.getParameter("userQu");
		String userSign = request.getParameter("userSign");
		
		if(userPhone == null) userPhone = "";
		if(userEmail == null) userEmail = "";
		if(userSign == null) userSign = "";
		
		try {
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String sqlstr = "update [user] set userNick = '"
					+ userNick + "' , userPhone = '"
					+ userPhone + "' , userEmail = '"
					+ userEmail + "' , userSex = '"
					+ userSex + "' , userSheng = '"
					+ userSheng + "' , userShi = '"
					+ userShi + "' , userQu = '"
					+ userQu + "' , userSign = '"
					+ userSign + "' where userID = '" + userID + "'"; 
			this.sqljdbcTemplate.execute(sqlstr);
			return true;

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Map<String, Object> uploadPhoto(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		MultipartFile contentfile = ((MultipartHttpServletRequest) request)
				.getFile("file");
		try {
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			Map<String, Object> result = new HashMap<String, Object>();
			
			String facename = this.sqljdbcTemplate.queryForMap("select userImage from [user] where userID = '" + userID + "'").get("userImage").toString();
			File dfile = null;
			if(facename.length() > 0)
			{
				dfile = new File(Constants.ImageFaceLocalPath + "/" + facename);
			}
			
			
			String name[] = ImageUtil.getName(contentfile
					.getOriginalFilename());
			String originalUrl = name[0];
			String path = Constants.ImageFaceLocalPath;
			File file = new File(path);
			if (!file.exists() || !file.isDirectory()) {
				file.mkdirs();
			}
			FileOutputStream fileOS = new FileOutputStream(path
					+ "/" + originalUrl);
			fileOS.write(contentfile.getBytes());
			fileOS.close();
			
			String sql = "update [user] set userImage = '"
					+ originalUrl + "' where userID = '" + userID + "'";
			this.sqljdbcTemplate.execute(sql);
			
			if (dfile != null && dfile.exists()) {
				dfile.delete();
			}
			result.put("userImage", Constants.ImageFacePath + originalUrl);
			return result;

		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public List<Map<String, Object>> getBookList(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String bookGroup = request.getParameter("bookGroup");
		String bookVIP = request.getParameter("bookVIP");
		String bookFree = request.getParameter("bookFree");
		String bookAuthor = request.getParameter("bookAuthor");
		String key = request.getParameter("key");
		String bookOrder = request.getParameter("bookOrder");
		String offset = request.getParameter("offset");
		if(offset == null) offset = "0";
		String limit = request.getParameter("limit");
		if(limit == null) limit = "10";
		
		String gr = "";
		String vi = "";
		String fr = "";
		String au = "";
		String ke = "";
		String or = "";
		
		if(bookGroup != null)
		{
			gr = "and bookGroup = '" + bookGroup + "'";
		}
		if(bookVIP != null)
		{
			vi = "and bookVIP = '" + bookVIP + "'";
		}
		if(bookFree != null)
		{
			fr = "and bookFree = '" + bookFree + "'";
		}
		if(bookAuthor != null)
		{
			au = "and bookAuthor = '" + bookAuthor + "'";
		}
		if(key != null)
		{
			ke = "and bookName like '%" + key + "%'";
		}
		if(bookOrder != null)
		{
			or = " order by bookDowns DESC";
		}
		else
		{
			or = " order by bookTime DESC";
		}
		
		List<Map <String , Object>> result = null;
		try {
			String sqlstr = "select top " + limit + " * from [book] where 1=1 "
					+ gr + vi + fr + au + ke
					+ "and bookID not in "
					+ "(select top " + offset + " bookID from [book] where 1=1 "
					+ gr + vi + fr + au + ke + or +")"
					+ or;
		
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sqlstr);
			result =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0; i < data.size(); i++)
			{
				result.add(this.response.dataWithBookInfo(data.get(i), param.getC(), true));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Map<String, Object> getBookInformation(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String bookID = request.getParameter("bookID");
		
		try {
			List<Map<String, Object>> list = this.sqljdbcTemplate
					.queryForList("select * from [book] where bookID='"
							+ bookID + "'");
			if (list.size() == 0) {
				return null;
			}
						
			return this.response.dataWithBookInfo(list.get(0) , param.getC() , false);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> getBookDownData(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String bookID = request.getParameter("bookID");
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String udid = param.getUdid();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		
		String veritysql = "select * from [book],[user] where [book].bookID = '"
				+ bookID
				+ "' and [user].userID = '"
				+ userID + "'";
		Map<String, Object> vData = this.sqljdbcTemplate.queryForMap(veritysql);
		
		if(vData.get("bookFree").toString().equals("1"))
		{
			if(this.response.bookAlredyBuy(bookID, userID) == false){
				result.put("code", "10010");
				result.put("msg", "图集尚未购买");
				return result;
			}
		}
		
		String sql = "if((select count(*) from [user] where userID='"
				+ userID
				+ "' and ( userUDID1 is null or userUDID1='' or userUDID1='"
				+ udid + "'))>0) begin update [user] set userUDID1='" + udid
				+ "' where userID='" + userID + "' end ";
		sql += "else if((select count(*) from [user] where userID='"
				+ userID
				+ "' and ( userUDID2 is null or userUDID2='' or userUDID2='"
				+ udid + "'))>0) begin update [user] set userUDID2='" + udid
				+ "' where userID='" + userID + "' end ";
		sql += "else if((select count(*) from [user] where userID='"
				+ userID
				+ "' and ( userUDID3 is null or userUDID3='' or userUDID3='"
				+ udid + "'))>0) begin update [user] set userUDID3='" + udid
				+ "' where userID='" + userID + "' end ";
		this.sqljdbcTemplate.execute(sql);
		
		sql = "select * from [user] where userID='" + userID
				+ "' and ( userUDID1='" + udid + "' or  userUDID2='"
				+ udid + "' or userUDID3='" + udid + "')";
		List<Map<String, Object>> list = this.sqljdbcTemplate.queryForList(sql);
		if (list == null || list.size() == 0) 
		{
			result.put("code", "10011");
			result.put("msg", "该账号已在超过三台设备下载");
			return result;
		}
		
		String picsql = "select * from [piclist] where bookID = '" + bookID + "' order by picID";
		List<Map<String, Object>> pics = this.sqljdbcTemplate.queryForList(picsql);
		
		List<Map<String, Object>> datas =  new ArrayList<Map<String, Object>>(pics.size());
		
		for(int i = 0; i < pics.size(); i ++)
		{
			Map<String, Object> image = new HashMap<String, Object>();
			image.put("dataPage", i);
			image.put("dataURL", Constants.ImageImagesPath + pics.get(i).get("picName"));
			datas.add(image);
		}
		
		result.put("success", true);
		result.put("datas", datas);
		
		return result;
	}
	
	public boolean sendComment(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String bookID = request.getParameter("bookID");
		String commentText = request.getParameter("commentText");
		commentText = java.net.URLDecoder.decode(commentText,"UTF-8");
		String commentPoint = request.getParameter("commentPoint");
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		try {
			String sql = "insert into [pinglun] (bookID,commentText,commentPoint,commentTime,userID) values ('"
					+ bookID + "','"
					+ commentText + "','"
					+ commentPoint + "','"
					+ date_str + "','"
					+ userID + "')";
			this.sqljdbcTemplate.execute(sql);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List<Map<String, Object>> getBookComments(HttpServletRequest request)
			throws Exception{
		String offset = request.getParameter("offset");
		if(offset == null) offset = "0";
		String limit = request.getParameter("limit");
		if(limit == null) limit = "10";
		String bookID = request.getParameter("bookID");
		
		List<Map <String , Object>> result = null;
		
		try{
			String where = "[user].userID = [pinglun].userID and [pinglun].bookID = '" + bookID + "'";
			String sqlstr = "select top " + limit + " * from [user],[pinglun] where "
					+ where + " and [pinglun].commentID not in (select top " 
					+ offset + " [pinglun].commentID from [user],[pinglun] where "
					+ where + " order by [pinglun].commentTime DESC ) order by [pinglun].commentTime DESC ";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sqlstr);
			result =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0; i < data.size(); i++)
			{
				result.add(this.response.dataWithComment(data.get(i)));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<Map<String, Object>> getiapPriceList(HttpServletRequest request)
			throws Exception{
		
		try{
			
			return this.sqljdbcTemplate.queryForList("select * from [iap]");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Map<String, Object> buyWithBook(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		String[] cs = null;
		
		try{
			cs = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY));
		}catch (Exception e) {
			e.printStackTrace();
			result.put("code", "10007");
			result.put("msg", "登录标识为空或出错");
			return result;
		}
		
		if(cs.length <= 0)
		{
			result.put("code", "10007");
			result.put("msg", "登录标识为空或出错");
			return result;
		}
		String userID = cs[0];
		String bookID = request.getParameter("bookID");
		
		try{
			if(this.response.bookAlredyBuy(bookID, userID) == true){
				result.put("code", "10008");
				result.put("msg", "该账户已购买过该图集");
				return result;
			}
			else
			{
				String veritysql = "select * from [user],[book] where [user].userID = '"
						+ userID
						+ "' and [book].bookID = '"
						+ bookID
						+ "'";
				Map<String, Object> data = this.sqljdbcTemplate.queryForMap(veritysql);
				int userPrice = Integer.parseInt(data.get("userPrice").toString());
				int bookPrice = Integer.parseInt(data.get("bookPrice").toString());
				if(data.get("bookFree").toString().equals("0"))
				{
					result.put("code", "10009");
					result.put("msg", "免费图集无需购买");
					return result;
				}
				else if(userPrice < bookPrice)
				{
					result.put("code", "10006");
					result.put("msg", "金币余额不足");
					return result;
				}
				else
				{
					Date date=new Date();
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date_str=df.format(date);
					
					//插入购买表
					try{

						String buysql = "insert into [purchase] (userID,bookID,purchaseTime,purchasePrice) values ('"
								+ userID + "','"
								+ bookID + "','"
								+ date_str + "','"
								+ String.valueOf(bookPrice) + "')";
						this.sqljdbcTemplate.execute(buysql);	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					//扣掉用户金币
					try{
						this.sqljdbcTemplate.execute("update [user] set userPrice = '" + String.valueOf(userPrice - bookPrice) + "' where userID = '" + userID + "'");	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					//记录日志
					String logsql = "insert into [priceLog] (logType,logRemark,logTime,logPrice,userID) values ('"
							+ "0" + "','"
							+ "购买《" + data.get("bookName")+ "》" + "','"
							+ date_str + "','"
							+ String.valueOf(bookPrice)+ "','"
							+ userID + "')";
					try{
						this.sqljdbcTemplate.execute(logsql);	
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					result.put("success", true);
					return result;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		result.put("msg", "购买失败");
		return result;
	}
	
	public List<Map<String, Object>> getBuyedBooks(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String offset = request.getParameter("offset");
		if(offset == null) offset = "0";
		String limit = request.getParameter("limit");
		if(limit == null) limit = "10";
		try{
			String sql = "select top "
					+ limit
					+ "  * from [purchase],[book] where [purchase].bookID = [book].bookID and [purchase].userID = '"
					+ userID + "' and [purchase].purchaseID not in (select top " 
					+ offset + " [purchase].purchaseID from [purchase],[book] where [purchase].bookID = [book].bookID and [purchase].userID = '"
					+ userID + "' )";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> result =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0; i < data.size(); i++)
			{
				result.add(this.response.dataWithBookInfo(data.get(i), param.getC(), true));
			}
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<Map<String, Object>> getPurchaseLogs(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String offset = request.getParameter("offset");
		if(offset == null) offset = "0";
		String limit = request.getParameter("limit");
		if(limit == null) limit = "10";
		try{
			String sql = "select top " 
					+ limit 
					+ " * from [priceLog] where userID = '"
					+ userID + "' and logID not in ( select top "
					+ offset
					+ " logID from [priceLog] where userID = '"
					+ userID + "' order by logTime DESC)"
					+  " order by logTime DESC";
			return this.sqljdbcTemplate.queryForList(sql);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> becomeFans(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String meID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String userID = request.getParameter("userID");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		
		try{
			String sqlh = "select count(*) from [fans] where fansZ = '"
					+ meID
					+ "' and fansK = '"
					+ userID + "'";
			int count = this.sqljdbcTemplate.queryForInt(sqlh);
			if(count > 0)
			{
				result.put("code", "10012");
				result.put("msg", "重复关注");
				return result;
			}
			else
			{
				String insql = "insert into [fans] (fansZ,fansK) values ('"
						+ meID
						+ "','"
						+ userID + "')";
				try{
					this.sqljdbcTemplate.execute(insql);	
					result.put("success", true);
					return result;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		

		
		return null;
	}
	
	public List<Map<String, Object>> getMemberList(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String like = "";
		String key = request.getParameter("key");
		if(key != null)
		{
			like = " and userNick like '%" + key + "%'";
		}
		String offset = request.getParameter("offset");
		if(offset == null) offset = "0";
		String limit = request.getParameter("limit");
		if(limit == null) limit = "10";
		try{
			String sql = "select top "
					+ limit 
					+ " * from [user] where 1 = 1" + like + " and "
					+ "userID not in (select top "
					+ offset 
					+ " userID from [user] where 1=1" + like + ")";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> result =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0; i < data.size(); i++)
			{
				result.add(this.response.dataWithUserInfo(data.get(i), param.getC()));
			}
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public List<Map<String, Object>> getFansList(HttpServletRequest request)
			throws Exception{
		try{
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String sql = "select * from [user] where userID in (select fansK from [fans] where fansZ = '"
					+ userID + "')";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> result =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0; i < data.size(); i++)
			{
				result.add(this.response.dataWithUserInfo(data.get(i), null));
			}
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean sendTalk(HttpServletRequest request)
			throws Exception{
		try{
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String talkText = request.getParameter("talkText");
			if(talkText == null) talkText = "";
			String talkAllLook = request.getParameter("talkAllLook");
			Date date=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str=df.format(date);
			
			String talkID;
			final String talksql = "insert into [talk] (userID,talkTime,talkText,talkAllLook,talkFrom) values ('"
					+ userID + "','"
					+ date_str + "','"
					+ talkText + "','"
					+ talkAllLook + "','"
					+ param.getModel() + "')";
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.sqljdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = null;
					ps = MeDaoJdbc.this.sqljdbcTemplate.getDataSource()
							.getConnection()
							.prepareStatement(talksql, new String[] { "talkID" });
					
					return ps;
				}
			}, keyHolder);
			
			talkID = keyHolder.getKey().toString();
			
			try {
				List<MultipartFile> files = new ArrayList<MultipartFile>();
				if (request.getClass().getName().equals("org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest")) {
					files = ((MultipartHttpServletRequest) request).getFiles("file");
				}
				if (files != null && files.size() > 0) {
					for (int i = 0; i < files.size(); i ++) {
						MultipartFile contentfile = files.get(i);
						
						if (!contentfile.isEmpty()) {
							String name[] = ImageUtil.getName(contentfile
									.getOriginalFilename());
							String originalUrl = name[0];
							String path = Constants.ImageTalkLocalPath;
							File file = new File(path);
							if (!file.exists() || !file.isDirectory()) {
								file.mkdirs();
							}
							FileOutputStream fileOS = new FileOutputStream(path
									+ "/" + originalUrl);
							fileOS.write(contentfile.getBytes());
							fileOS.close();
							
							String picsql = "insert into [talkpic] (talkID,talkImage) values ('"
									+ talkID + "','"
									+ originalUrl + "')";
							this.sqljdbcTemplate.execute(picsql);
						}
					}
				}
			} catch (Exception e) {
				this.sqljdbcTemplate.execute("delete from [talk] where talkID = '" + talkID + "'");
				e.printStackTrace();
				return false;
			}

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteTalk(HttpServletRequest request)
			throws Exception{
		try{
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String talkID = request.getParameter("talkID");
			
			String havesql = "select count(*) from [talk] where talkID = '"
					+ talkID + "' and userID = '"
					+ userID + "'";
			int have = this.sqljdbcTemplate.queryForInt(havesql);
			if(have == 0)
			{
				return false;
			}
			
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList("select * from [talkpic] where talkID = '" + talkID + "'");
			for(int i = 0 ; i < data.size() ; i ++){
				Map <String , Object> pic = data.get(i);
				File dfile = null;
				dfile = new File(Constants.ImageTalkLocalPath + "/" + pic.get("talkImage"));
				if (dfile != null && dfile.exists()) {
					dfile.delete();
				}
			}
			
			this.sqljdbcTemplate.execute("delete from [talkpic] where talkID = '" + talkID + "'");
			this.sqljdbcTemplate.execute("delete from [talk] where talkID = '" + talkID + "'");
			
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean commentTalk(HttpServletRequest request)
			throws Exception{
		try{
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String talkID = request.getParameter("talkID");
			String replayID = request.getParameter("replayID");
			if(replayID == null) replayID = "";
			String accept = request.getParameter("accept");
			if(accept == null) accept = "";
			String text = request.getParameter("text");
			Date date=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str=df.format(date);
			
			String sql = "insert into [talkCom] (userID,talkID,replayID,accept,text,time) values ('"
					+ userID + "','"
					+ talkID + "','"
					+ replayID + "','"
					+ accept + "','"
					+ text + "','"
					+ date_str + "')";
			this.sqljdbcTemplate.execute(sql);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean zanTalk(HttpServletRequest request)
			throws Exception{
		try{
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String talkID = request.getParameter("talkID");
			
			String sql = "if((select count(*) from [talkZan] where userID='"
			+ userID + "' and talkID = '"
			+ talkID + "') = 0) begin insert into [talkZan] (userID,talkID) values ('"
			+ userID + "','"
			+ talkID + "') end";
			this.sqljdbcTemplate.execute(sql);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Map<String, Object> getTalkDetail(HttpServletRequest request)
			throws Exception{
		try{
			CommonParam param = new CommonParam(request);
			String talkID = request.getParameter("talkID");
			
			String sql = "select * from [user],[talk] where [user].userID = [talk].userID"
					+ " and [talk].talkID = '"
					+ talkID + "'";
			
			Map<String, Object> data = this.sqljdbcTemplate.queryForMap(sql);
			
			if(data != null)
			{
				return this.response.dataWithTalk(data);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Map<String, Object>> getZansWithTalk(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String talkID = request.getParameter("talkID");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		return this.response.dataWithTalkZans(offset, limit, talkID);
	}
	
	public List<Map<String, Object>> getCommentsWithTalk(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String talkID = request.getParameter("talkID");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		return this.response.dataWithTalkComment(offset, limit, talkID);
	}
	
	public List<Map<String, Object>> getTalkList(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String talkAllLook = request.getParameter("talkAllLook");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		if(offset == null) offset = "0";
		if(limit == null) limit = "10";
		String userID = request.getParameter("userID");
		String anduser = "";
		if(userID != null)
		{
			anduser = " and [talk].userID = '" + userID + "' ";
		}
		
		try{
			String sql = "select top "
					+ limit
					+ " * from [talk],[user] where [talk].userID = [user].userID "
					+ anduser
					+ "and [talk].talkAllLook = '"
					+ talkAllLook
					+ "' and [talk].talkID not in ( select top "
					+ offset
					+ " [talk].talkID from [talk],[user] where [talk].userID = [user].userID " 
					+ anduser
					+ " and [talk].talkAllLook = '"
					+ talkAllLook + "' )";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> talks =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0 ; i < data.size(); i++)
			{
				talks.add(this.response.dataWithTalk(data.get(i)));
			}
			return talks;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Map<String, Object>> getGroupList(HttpServletRequest request)
			throws Exception{
		
		try{
			String sql = "select * from [typeb]";
			return this.sqljdbcTemplate.queryForList(sql);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Map<String, Object>> getAuthorList(HttpServletRequest request)
			throws Exception{
		
		String key = request.getParameter("key");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		if(offset == null) offset = "0";
		if(limit == null) limit = "10";
		
		try{
			String andKey = "";
			if(key != null)
			{
				andKey = " and authorName like '%" + key + "%' ";
			}
			
			String sql = "select top "
					+limit
					+ " * from [author] where 1=1 "
					+ andKey 
					+ "and authorID not in ( select top "
					+ offset 
					+ " authorID from [author] where 1=1 "
					+ andKey + " )";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> authors =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0;i < data.size(); i++)
			{
				authors.add(this.response.dataWithAuthorInfo(data.get(i)));
			}
			return authors;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Map<String, Object> getAuthorDetail(HttpServletRequest request)
			throws Exception{
		String authorID = request.getParameter("authorID");
		try{
			String sql = "select * from [author] where authorID = '"
					+ authorID + "'";
			return this.response.dataWithAuthorInfo(this.sqljdbcTemplate.queryForMap(sql));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String, Object>> getNewsList(HttpServletRequest request)
			throws Exception{
		String key = request.getParameter("key");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		if(offset == null) offset = "0";
		if(limit == null) limit = "10";
		
		try{
			String andKey = "";
			if(key != null)
			{
				andKey = " and newsTitle like '%" + key + "%' ";
			}
			
			String sql = "select top "
					+limit
					+ " * from [newlist] where 1=1 "
					+ andKey 
					+ "and newsID not in ( select top "
					+ offset 
					+ " newsID from [newlist] where 1=1 "
					+ andKey + " )";
			return this.sqljdbcTemplate.queryForList(sql);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public Map<String, Object> getNewsDetail(HttpServletRequest request)
			throws Exception{
		String newsID = request.getParameter("newsID");
		try{
			String sql = "select * from [newlist] where newsID = '"
					+ newsID + "'";
			
			Map<String, Object> data =  this.sqljdbcTemplate.queryForMap(sql);
			if(data != null)
			{
				data.put("newsShare", Constants.ShareNewsHtmlPath + data.get("newsID"));
			}
			
			return data;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String, Object>> getBannerList(HttpServletRequest request)
			throws Exception{
		try{
			return this.sqljdbcTemplate.queryForList("select * from [notice]");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String, Object>> getCityList(HttpServletRequest request)
			throws Exception{
		String type = request.getParameter("type");
		String code = request.getParameter("code");
		
		try{
			if(type == null || type.equals("0"))
			{
				return this.sqljdbcTemplate.queryForList("select ProID as code,ProName as name from [T_Province]");
			}
			else if(type.equals("1"))
			{
				if(code != null)
				{
					return this.sqljdbcTemplate.queryForList("select CityID as code,CityName as name from [T_City] where ProID = '" + code + "'");
				}
			}
			else if(type.equals("2"))
			{
				if(code != null)
				{
					return this.sqljdbcTemplate.queryForList("select Id as code,DisName as name from [T_District] where CityID = '" + code + "'");
				}
			}
			else if(type.equals("-1"))
			{
//				String prosql = "select ProID as code,ProName as name from [T_Province]";
//				List<Map <String , Object>> prodata = this.sqljdbcTemplate.queryForList(prosql);
//				
//				for (int i = 0 ; i < prodata.size() ; i++)
//				{
//					Map <String , Object> pro = prodata.get(i);
//					String citysql = "select CityID as code,CityName as name from [T_City] where ProID = '" + pro.get("code") + "'";
//					List<Map <String , Object>> citydata = this.sqljdbcTemplate.queryForList(citysql);
//					pro.put("sons", citydata);
//					
//					for (int j = 0 ; j < citydata.size() ; j++)
//					{
//						Map <String , Object> city = citydata.get(j);
//						String dissql = "select Id as code,DisName as name from [T_District] where CityID = '" + city.get("code") + "'";
//						city.put("dis", this.sqljdbcTemplate.queryForList(dissql));
//					}
//				}
//				return prodata;
				
				String sql = "select a.ProID,a.ProName,b.CityID,b.CityName,c.Id,c.DisName from [T_Province] a ,[T_City] b,[T_District] c where a.ProID = b.ProID and b.CityID = c.CityID";
				return this.sqljdbcTemplate.queryForList(sql);
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public List<Map<String, Object>> getGoodsList(HttpServletRequest request)
			throws Exception{
		
		String goodPeople = request.getParameter("goodPeople");
		String goodStatus = request.getParameter("goodStatus");
		String goodReview = request.getParameter("goodReview");
		String key = request.getParameter("key");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		if(offset == null) offset = "0";
		if(limit == null) limit = "10";
		
		try{
			String andpeople = "";
			if(goodPeople != null)
			{
				andpeople = " and b.goodPeople = '" + goodPeople + "' ";
			}
			String andstatus = "";
			if(goodStatus != null)
			{
				andstatus = " and b.goodStatus = '" + goodStatus + "' ";
			}
			String andreview = " and b.goodReview = '1'";
//			if(goodReview != null)
//			{
//				andreview = " and b.goodReview = '" + goodReview + "' ";
//			}
			String andkey = "";
			if(key != null)
			{
				andkey = " and (b.goodName like '%" + key + "%' or b.goodNo like '%" + key + "%') ";
			}
			
			String sql = "select top "
					+ limit
					+ " * from [typeb] a , [good] b where b.groupID = a.groupID "
					+ andpeople + andstatus + andreview + andkey
					+ " and b.goodID not in (select top "
					+ offset
					+ " b.goodID from [typeb] a , [good] b where b.groupID = a.groupID "
					+ andpeople + andstatus + andreview + andkey
					+ " order by b.goodTime DESC ) order by b.goodTime DESC";
			
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> goods =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0 ; i < data.size(); i++)
			{
				goods.add(this.response.dataWithGood(data.get(i) , true));
			}
			
			return goods;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Map<String, Object> getGoodDetail(HttpServletRequest request)
			throws Exception{
		
		String goodID = request.getParameter("goodID");
		
		try{
			
			String sql = "select * from [typeb] a , [good] b where a.groupID = b.groupID and b.goodID = '"
					+ goodID + "'";
			return this.response.dataWithGood(this.sqljdbcTemplate.queryForMap(sql), false);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean sendGoods(HttpServletRequest request)
			throws Exception{
		
		try{
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			String goodName = request.getParameter("goodName");
			String groupID = request.getParameter("groupID");
			String goodPrice = request.getParameter("goodPrice");
			String goodRemark = request.getParameter("goodRemark");
			
			Date date=new Date();
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str=df.format(date);
			
			String goodNo = "P" + System.currentTimeMillis();
			
			String goodID;
			final String goodsql = "insert into [good] (goodName,groupID,goodPrice,goodRemark,goodNo,goodPeople,goodTime) values ('"
					+ goodName + "','"
					+ groupID + "','"
					+ goodPrice + "','"
					+ goodRemark + "','"
					+ goodNo + "','"
					+ userID + "','"
					+ date_str + "')";
					
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.sqljdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = null;
					ps = MeDaoJdbc.this.sqljdbcTemplate.getDataSource()
							.getConnection()
							.prepareStatement(goodsql, new String[] { "goodID" });
					
					return ps;
				}
			}, keyHolder);
			
			goodID = keyHolder.getKey().toString();
			
			
			MultipartFile contentfile = ((MultipartHttpServletRequest) request)
					.getFile("goodImage");
			try {

				String name[] = ImageUtil.getName(contentfile
						.getOriginalFilename());
				String originalUrl = name[0];
				String path = Constants.ImageGoodsLocalPath;
				File file = new File(path);
				if (!file.exists() || !file.isDirectory()) {
					file.mkdirs();
				}
				FileOutputStream fileOS = new FileOutputStream(path
						+ "/" + originalUrl);
				fileOS.write(contentfile.getBytes());
				fileOS.close();
				
				String sql = "update [good] set goodImage = '"
						+ originalUrl + "' where goodID = '" + goodID + "'";
				this.sqljdbcTemplate.execute(sql);
				
			}catch (Exception e) {
				this.sqljdbcTemplate.execute("delete from [good] where goodID = '" + goodID + "'");
				e.printStackTrace();
				return false;
			}
			
			try {
				List<MultipartFile> files = new ArrayList<MultipartFile>();
				if (request.getClass().getName().equals("org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest")) {
					files = ((MultipartHttpServletRequest) request).getFiles("image");
				}
				if (files != null && files.size() > 0) {
					for (int i = 0; i < files.size(); i ++) {
						MultipartFile contentfiles = files.get(i);
						
						if (!contentfiles.isEmpty()) {
							String name[] = ImageUtil.getName(contentfiles
									.getOriginalFilename());
							String originalUrl = name[0];
							String path = Constants.ImageGoodsLocalPath;
							File file = new File(path);
							if (!file.exists() || !file.isDirectory()) {
								file.mkdirs();
							}
							FileOutputStream fileOS = new FileOutputStream(path
									+ "/" + originalUrl);
							fileOS.write(contentfiles.getBytes());
							fileOS.close();
							
							String picsql = "insert into [goodImage] (goodID,goodImageName) values ('"
									+ goodID + "','"
									+ originalUrl + "')";
							this.sqljdbcTemplate.execute(picsql);
						}
					}
				}
			} catch (Exception e) {
				this.sqljdbcTemplate.execute("delete from [good] where goodID = '" + goodID + "'");
				e.printStackTrace();
				return false;
			}

			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean goodPay(HttpServletRequest request)
			throws Exception{
		
		CommonParam param = new CommonParam(request);
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String goodID = request.getParameter("goodID");
		String goodPayPrice = request.getParameter("goodPayPrice");
		
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date_str=df.format(date);
		
		try{
			String goodsql = "insert into [goodPay] (goodID,userID,goodPayPrice,goodPayTime) values ('"
					+ goodID + "','"
					+ userID + "','"
					+ goodPayPrice + "','"
					+ date_str + "')";
			this.sqljdbcTemplate.execute(goodsql);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List<Map<String, Object>> getGoodPayList(HttpServletRequest request)
			throws Exception{
		String goodID = request.getParameter("goodID");
		
		try{
			String sql = "select * from [goodPay],[user] where [goodPay].userID = [user].userID and goodID = '"
						+ goodID 
						+ "' order by [goodPay].goodPayTime DESC";
			List<Map <String , Object>> data = this.sqljdbcTemplate.queryForList(sql);
			List<Map <String , Object>> pays =  new ArrayList<Map<String, Object>>(data.size());
			for(int i = 0; i < data.size() ; i++)
			{
				Map<String, Object> pay = new HashMap<String, Object>();
				pay.put("goodPayID", data.get(i).get("goodPayID"));
				pay.put("goodPayTime", data.get(i).get("goodPayTime"));
				pay.put("userID", data.get(i).get("userID"));
				pay.put("userNick", data.get(i).get("userNick"));
				pay.put("userImage", Constants.ImageFacePath + data.get(i).get("userImage"));
				
				double money = Double.parseDouble(data.get(i).get("goodPayPrice").toString()); 
				DecimalFormat df = new DecimalFormat("######0.00");   
				pay.put("goodPayPrice", df.format(money));
				
				pays.add(pay);
			}
			
			return pays;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean finishGoodPay(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String goodID = request.getParameter("goodID");
		String goodPayID = request.getParameter("goodPayID");
		
		try{
			Map<String, Object> good = this.sqljdbcTemplate.queryForMap("select * from [good] where goodID = '" + goodID + "'");
			if(!good.get("goodPeople").toString().equals(userID))
			{
				return false;
			}
			
			String finisql = "update [good] set goodPayID = '"
					+ goodPayID
					+ "', goodBuyPrice = (select goodPayPrice from [goodPay] where goodPayID = '"
					+ goodPayID + "') where goodID = '"
					+ goodID + "'";
			this.sqljdbcTemplate.execute(finisql);
			return true;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean changeGoodStatus(HttpServletRequest request)
			throws Exception{
		CommonParam param = new CommonParam(request);
		String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
		String goodID = request.getParameter("goodID");
		String status = request.getParameter("status");
		
		try{
			Map<String, Object> good = this.sqljdbcTemplate.queryForMap("select * from [good] where goodID = '" + goodID + "'");
			if(!good.get("goodPeople").toString().equals(userID) || !good.get("goodReview").toString().equals("1"))
			{
				return false;
			}
			
			String upordown = status.equals("0") ? "0" : "1";
			
			
			String finisql = "update [good] set goodStatus = '"
					+ upordown + "' where goodID = '"
					+ goodID + "'";
					
			this.sqljdbcTemplate.execute(finisql);
			return true;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
			
	public Map<String, Object> sendIdCar(HttpServletRequest request)
			throws Exception{

		try {
			
			CommonParam param = new CommonParam(request);
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			
			MultipartFile contentfile = ((MultipartHttpServletRequest) request)
					.getFile("image");
			String originalUrl = "";
			if(!contentfile.isEmpty())
			{
				String name[] = ImageUtil.getName(contentfile
						.getOriginalFilename());
				originalUrl = name[0];
				String path = Constants.ImageIdcarLocalPath;
				File file = new File(path);
				if (!file.exists() || !file.isDirectory()) {
					file.mkdirs();
				}
				FileOutputStream fileOS = new FileOutputStream(path
						+ "/" + originalUrl);
				fileOS.write(contentfile.getBytes());
				fileOS.close();
				
				Map<String, Object> facedata = this.sqljdbcTemplate.queryForMap("select idcarImage from [idcar] where userID = '" + userID + "'");
				if(facedata != null)
				{
					String facename = facedata.get("idcarImage").toString();
					File dfile = null;
					if(facename.length() > 0)
					{
						dfile = new File(Constants.ImageIdcarLocalPath + "/" + facename);
					}
					if (dfile != null && dfile.exists()) {
						dfile.delete();
					}
				}
			}
			
			String deletesql = "delete from [idcar] where userID = '" + userID + "'";
			this.sqljdbcTemplate.execute(deletesql);
			
			final String insersql = "insert into [idcar] (userID,idcarImage) values ('"
					+ userID + "','"
					+ originalUrl + "')";
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.sqljdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = null;
					ps = MeDaoJdbc.this.sqljdbcTemplate.getDataSource()
							.getConnection()
							.prepareStatement(insersql, new String[] { "idcarID" });
					
					return ps;
				}
			}, keyHolder);
			
			String idcarID = keyHolder.getKey().toString();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("idcarID", idcarID);
			result.put("idcarImage", Constants.ImageIdcarPath + originalUrl);
			result.put("idcarReview", "0");
			
			return result;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}		
			
	public Map<String, Object> getIdcarStatus(HttpServletRequest request)
			throws Exception{
		
		try{
			CommonParam param = new CommonParam(request);
			Map<String, Object> result = new HashMap<String, Object>();
			String userID = Convert.cToUserId(Aes.Decrypt(param.getC(), Constants.ENCODE_KEY))[0];
			
			int ishave = this.sqljdbcTemplate.queryForInt("select count(*) from [idcar] where userID = '" + userID + "'");
			if(ishave == 0)
			{
				result.put("idcarReview", "-1");
				return result;
			}
			
			Map<String, Object> data = this.sqljdbcTemplate.queryForMap("select * from [idcar] where userID = '" + userID + "'");
			result.put("idcarID", data.get("idcarID"));
			result.put("idcarImage", Constants.ImageIdcarPath + data.get("idcarImage"));
			result.put("idcarReview", data.get("idcarReview"));
			return result;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}		
			
			
			
			
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
			
			
	

	public List<Map<String, Object>> getfreebooks(Map<String, Object> attrMap)
			throws Exception {
		List<Map<String, Object>> listData = null;
		Integer limit = Convert.nullToInt(attrMap.get("limit"));
		Integer offset = Convert.nullToInt(attrMap.get("offset"));
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		// String sql = "select k.* from( select top "
		// + limit
		// + " i.* from ( select top "
		// + (limit + offset)
		// +
		// " g.* from(select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.description,a.press from News a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on b.product_id=a.ID where a.is_pay=0 and title_ID=0) g order by ID )i order by ID desc) k order by id  ";
		String sql = "select top "
				+ limit
				+ " a1.* from  (select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then "
				+ "'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else"
				+ " 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,case when journal_type is null then 15 else journal_type end journal_type from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) "
				+ "a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on b.product_id=a.ID where a.is_pay=0 and "
				+ "title_ID=0 and a.ParentID=3 "
				+ attrStr
				+ ") a1 where a1.id not in (select top "
				+ offset
				+ " g.id from(select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then "
				+ "'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else "
				+ "'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,case when journal_type is null then 15 else journal_type end journal_type from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left"
				+ " join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on b.product_id=a.ID where a.is_pay=0 and title_ID=0 and  a.ParentID=3 "
				+ attrStr + ") " + "g order by ID) order by id  ";
		listData = this.sqljdbcTemplate.queryForList(sql);
		return listData;
	}

	public Long countfreebook(Map<String, Object> attrMap) throws Exception {
		Long l = 0l;
		try {

			String sql = "select COUNT(*) as count from News a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b on b.product_id=a.ID where a.is_pay=0 and title_ID=0  and  a.ParentID=3 ";
			String attrStr = "";
			if (attrMap.get("type") != null) {
				attrStr = " and journal_type=" + attrMap.get("type");
			}
			Map<String, Object> map = this.sqljdbcTemplate.queryForMap(sql
					+ attrStr);
			l = Long.parseLong(map.get("count").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public List<Map<String, Object>> getbuyedbooks(Map<String, Object> attrMap)
			throws Exception {
		List<Map<String, Object>> listData = null;
		Integer limit = Convert.nullToInt(attrMap.get("limit"));
		Integer offset = Convert.nullToInt(attrMap.get("offset"));
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		// String sql = "select k.* from( select top "
		// + limit
		// + "i.* from ( select top "
		// + (limit + offset)
		// +
		// " g.* from(select a.id,a.ThbTitle as title,a.author,case when b.pic_name is "
		// +
		// "null then  'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'"
		// +
		// "+b.pic_name end faceurl,a.description, a.press from News a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  "
		// +
		// "on   b.product_id=a.ID  where a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
		// + attrMap.get("userid")
		// +
		// "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number) ) g order by ID )i order by ID desc) k order by id  ";
		String sql = "select top "
				+ limit
				+ " a1.* from  ( select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then "
				+ "'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else "
				+ "'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description, a.press,case when journal_type is null then 15 else journal_type end journal_type  from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left join "
				+ "(select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on   b.product_id=a.ID  where a.ID "
				+ "in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
				+ attrMap.get("userid")
				+ "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number) "
				+ attrStr
				+ " )"
				+ " a1  where a1.id not in (select top "
				+ offset
				+ " g.id from( select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then "
				+ "'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg'  else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end "
				+ "faceurl,a.Remark as description, a.press,case when journal_type is null then 15 else journal_type end journal_type  from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on   b.product_id=a.ID "
				+ " where a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
				+ attrMap.get("userid")
				+ "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number)"
				+ attrStr + " )  " + " g order by ID) order by id ";
		listData = this.sqljdbcTemplate.queryForList(sql);
		return listData;
	}

	public Long countbuyedbook(Map<String, Object> attrMap) throws Exception {
		Long l = 0l;
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		try {
			String sql = "select  COUNT(*) as count from News a   where a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
					+ attrMap.get("userid")
					+ "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number "
					+ attrStr + ") ";
			Map<String, Object> map = this.sqljdbcTemplate.queryForMap(sql);
			l = Long.parseLong(map.get("count").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public List<Map<String, Object>> getallcanreadbooks(
			Map<String, Object> attrMap) throws Exception {
		List<Map<String, Object>> listData = null;
		Integer limit = Convert.nullToInt(attrMap.get("limit"));
		Integer offset = Convert.nullToInt(attrMap.get("offset"));
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		// String sql = "select k.* from( select top "
		// + limit
		// + " i.* from ( select top "
		// + (limit + offset)
		// +
		// " g.* from(  select a.id,a.is_pay,a.ThbTitle as title,a.author,case when b.pic_name is null then  'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg'   else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.description,a.press from News a left join(select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on   b.product_id=a.ID  where (a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
		// + attrMap.get("userid")
		// +
		// "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number) or ID in(select ID from News where title_ID=0 and is_pay=0))  ) g order by ID )i order by ID desc) k order by id  ";
		// listData = this.sqljdbcTemplate.queryForList(sql);
		// System.out.println(sql);
		String sql = "select top "
				+ limit
				+ " a1.* from  (select a.id,a.is_pay,a.ThbTitle as title,a.author,case when b.pic_name is null then   'http://114.112.170.45:8080/UpLoadFiles/Images/802316c9"
				+ "8f9344e884ffdbe453be98ce.jpg'   else '114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,case when journal_type  is null then 15 else journal_type end journal_type  from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left join(select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b "
				+ " on   b.product_id=a.ID  where (a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
				+ attrMap.get("userid")
				+ "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number) or ID in(select ID from News where title_ID=0 and is_pay=0  and  a.ParentID=3  ))  "
				+ attrStr
				+ ") a1 where a1.id not in (select top "
				+ offset
				+ " g.id from(select a.id,a.is_pay,a.ThbTitle as title,a.author,case when b.pic_name is null then   'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg'   else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,case when journal_type is null then 15 else journal_type end journal_city  from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left join(select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b  on   b.product_id=a.ID  where (a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
				+ attrMap.get("userid")
				+ "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number) or ID in(select ID from News where title_ID=0 and is_pay=0 and  a.ParentID=3 ))"
				+ attrStr + " )   g order by ID) order by id ";
		listData = this.sqljdbcTemplate.queryForList(sql);
		return listData;
	}

	public Long countcanreadbook(Map<String, Object> attrMap) throws Exception {
		Long l = 0l;
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		try {
			String sql = "  select COUNT(*) as count from News a  where (a.ID in(select f.ProID from [dbo].[Order] o,[order_info] f where o.memberid="
					+ attrMap.get("userid")
					+ ""
					+ "  and (o.static=2 or o.static=3) and f.order_number=o.order_Number) or ID in(select ID from News where title_ID=0 and is_pay=0  and  a.ParentID=3 ))"
					+ attrStr + " ";
			Map<String, Object> map = this.sqljdbcTemplate.queryForMap(sql);
			l = Long.parseLong(map.get("count").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public List<Map<String, Object>> getbookcitybooks(
			Map<String, Object> attrMap) throws Exception {
		List<Map<String, Object>> listData = null;
		Integer limit = Convert.nullToInt(attrMap.get("limit"));
		Integer offset = Convert.nullToInt(attrMap.get("offset"));
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		if (!Check.isEmpty(attrMap.get("userid"))) {
			String sql = "select top "
					+ limit
					+ " a1.* from  (   select c.*,case when f.ProID is null then 0 else 1 end if_buy from (select a.id,a.is_pay,a.ThbTitle as title,"
					+ " a.author,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/d95694e1823440bfbac1aaa9b6a943f5.jpg'  else  "
					+ "'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,case when journal_type  is null then 15"
					+ " else journal_type end journal_type  from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],"
					+ "ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],"
					+ "ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],"
					+ "ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as"
					+ "  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left   join  (select MAX(pic_name) "
					+ "as pic_name,product_id from [product_pic] group by product_id)b on  b.product_id=a.ID where a.title_ID=0  and  a.ParentID=3  and   (a.ThbTitle like'%"
					+ attrMap.get("key")
					+ "%' or a.Description like'%"
					+ attrMap.get("key")
					+ "%')"
					+ "  ) c left join (select e.ProID from [dbo].[Order] d,dbo.order_info e  where d.memberid="
					+ attrMap.get("userid")
					+ "   and d.order_Number=e.order_number and (d.static=3 or d.static=2)) f"
					+ " on c.ID=f.ProID "
					+ attrStr
					+ " ) a1 where a1.id not in (select top "
					+ offset
					+ " g.id from(    select c.*,case when f.ProID is null then 0 else 1 end if_buy from "
					+ "(select a.id,a.is_pay,a.ThbTitle as title, a.author,case when b.pic_name is null then "
					+ "'http://114.112.170.45:8080/UpLoadFiles/Images/d95694e1823440bfbac1aaa9b6a943f5.jpg'  else  "
					+ "'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,case when journal_type  is null then 15 else journal_type end journal_type  from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left   join  (select MAX(pic_name) as pic_name,"
					+ "product_id from [product_pic] group by product_id)b on  b.product_id=a.ID where a.title_ID=0  and  a.ParentID=3  and   (a.ThbTitle like'%"
					+ attrMap.get("key")
					+ "%' or a.Description like'%"
					+ attrMap.get("key")
					+ "%')  ) c"
					+ " left join (select e.ProID from [dbo].[Order] d,dbo.order_info e  where d.memberid="
					+ attrMap.get("userid")
					+ "   and d.order_Number=e.order_number and (d.static=3 or d.static=2)) f "
					+ "on c.ID=f.ProID " + attrStr
					+ "  )   g order by ID) order by id ";
			listData = this.sqljdbcTemplate.queryForList(sql);
		} else {
			String sql = "select top "
					+ limit
					+ " a1.* from  (select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,a.is_pay,0 as if_buy from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b on b.product_id=a.ID where  title_ID=0  and  a.ParentID=3 and (a.ThbTitle like'%"
					+ attrMap.get("key")
					+ "%' or a.Description like'%"
					+ attrMap.get("key")
					+ "%') "
					+ attrStr
					+ ") a1 where a1.id not in (select top "
					+ offset
					+ " g.id from( select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then '114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,a.is_pay,0 as if_buy from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b on b.product_id=a.ID where  title_ID=0  and  a.ParentID=3 and  (a.ThbTitle like'%"
					+ attrMap.get("key") + "%' or a.Description like'%"
					+ attrMap.get("key") + "%') " + attrStr
					+ ")   g order by ID) order by id ";
			listData = this.sqljdbcTemplate.queryForList(sql);
		}

		return listData;
	}

	public Long countbookcitybook(Map<String, Object> attrMap) throws Exception {
		Long l = 0l;
		String attrStr = "";
		if (attrMap.get("type") != null) {
			attrStr = " and journal_type=" + attrMap.get("type");
		}
		try {
			if (!Check.isEmpty(attrMap.get("userid"))) {
				String sql = " select count(*) as count from (select c.*,case when f.ProID is null then 0 else 1 end if_buy from (select a.title_ID,a.id,a.is_pay,a.ThbTitle as title,"
						+ " a.author,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/d95694e1823440bfbac1aaa9b6a943f5.jpg'  else "
						+ "'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a left   join (select MAX(pic_name)"
						+ " as pic_name,product_id from [product_pic] group by product_id)b on  b.product_id=a.ID where a.title_ID=0  and  a.ParentID=3  and (a.ThbTitle like'%"
						+ attrMap.get("key")
						+ "%' or a.Description like'%"
						+ attrMap.get("key")
						+ "%') "
						+ attrStr
						+ " ) c left join (select e.ProID from [dbo].[Order] "
						+ "d,dbo.order_info e  where d.memberid="
						+ attrMap.get("userid")
						+ " and d.order_Number=e.order_number and (d.static=3 or d.static=2)) f on c.ID=f.ProID ) h";
				Map<String, Object> map = this.sqljdbcTemplate.queryForMap(sql);
				l = Long.parseLong(map.get("count").toString());
			} else {
				String sql = " select count(*) as count from (select a.id,a.ThbTitle as title,a.author,case when b.pic_name is null then "
						+ "'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else"
						+ " 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl,a.Remark as description,a.press,a.is_pay,0 as if_buy from (SELECT  ne.[ID],ne.[ParentID],ne.[ThbTitle],ne.[TitleStyle],ne.[Origin],ne.[KeyWord],ne.[Tag],ne.[WordFile],ne.[PDFFile],ne.[Img],ne.[ImgAlt],ne.[URL],ne.[Taxis],ne.[IssueDate],ne.[ClickMeasure],ne.[IsShow],ne.[IsStatic],ne.[StaticUrl],ne.[IsSecondShow],ne.[Recom],ne.[IsIndex],ne.[PutTop],ne.[NewsBrief],ne.[Remark],ne.[Memo],ne.[NodeID],ne.[Multi_Attrib],ne.[HtmlTitle],ne.[Description],ne.[Issue_Number],ne.[Attribute],ne.[Integrity],ne.[journal_type],ne.[journal_city],ne.[Press],ne.[ebook_price],ne.[Prices],ne.[read_Count],ne.[zan_Count],ne.[title_ID],ne.[is_pay],au.[ThbTitle] as  author FROM  [News] ne,[News] au where ne.ParentID=3 and au.id=ne.author) a "
						+ "left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)b on b.product_id=a.ID where  "
						+ "title_ID=0  and  a.ParentID=3 and (a.ThbTitle like'%"
						+ attrMap.get("key")
						+ "%' or a.Description like'%"
						+ attrMap.get("key") + "%') " + attrStr + " ) h";
				Map<String, Object> map = this.sqljdbcTemplate.queryForMap(sql);
				l = Long.parseLong(map.get("count").toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	public List<Map<String, Object>> getbookdetailbyid(
			Map<String, Object> attrMap) throws Exception {
		String uname = attrMap.get("username").toString();
		String udid = attrMap.get("udid").toString();
		
		if(this.isfreebook(attrMap.get("bid").toString()) == false)
		{
			String sql = "if((select count(*) from member where id='"
					+ uname
					+ "' and ( MAC_Address1 is null or MAC_Address1='' or MAC_Address1='"
					+ udid + "'))>0) begin update Member set MAC_Address1='" + udid
					+ "' where id='" + uname + "' end ";
			sql += "else if((select count(*) from member where id='"
					+ uname
					+ "' and ( MAC_Address2 is null or MAC_Address2='' or MAC_Address2='"
					+ udid + "'))>0) begin update Member set MAC_Address2='" + udid
					+ "' where id='" + uname + "' end ";
			sql += "else if((select count(*) from member where id='"
					+ uname
					+ "' and ( MAC_Address3 is null or MAC_Address3='' or MAC_Address3='"
					+ udid + "'))>0) begin update Member set MAC_Address3='" + udid
					+ "' where id='" + uname + "' end ";
			this.sqljdbcTemplate.execute(sql);
			
			sql = "select * from Member where id='" + uname
					+ "' and ( MAC_Address1='" + udid + "' or  MAC_Address2='"
					+ udid + "' or MAC_Address3='" + udid + "')";
			List<Map<String, Object>> list = this.sqljdbcTemplate.queryForList(sql);
			if (list == null || list.size() == 0) 
			{
				return null;
			}
		}
		else	
		{
			
			
		}
		


//		String macSql = "select MAC_Address1,MAC_Address2,MAC_Address3 from member where id='"
//				+ uname + "'";
//		Map<String, Object> data = this.sqljdbcTemplate.queryForMap(macSql);
//		
//		String inserSql;
//		if(data.get("MAC_Address1") == null || data.get("MAC_Address1") == "")
//		{
//			inserSql = "update Member set MAC_Address1='" + udid
//					+ "' where id='" + uname + "'";
//			this.sqljdbcTemplate.execute(inserSql);
//		}
//		else if(data.get("MAC_Address1").toString().equals(udid))
//		{
//			
//		}
//		else if(data.get("MAC_Address2") == null || data.get("MAC_Address2") == "" || data.get("MAC_Address2").toString().isEmpty())
//		{
//			inserSql = "update Member set MAC_Address2='" + udid
//					+ "' where id='" + uname + "'";
//			this.sqljdbcTemplate.execute(inserSql);
//		}
//		else if(data.get("MAC_Address2").toString().equals(udid))
//		{
//			
//		}
//		else if(data.get("MAC_Address3").toString() == null || data.get("MAC_Address3").toString() == "" || data.get("MAC_Address3").toString().isEmpty())
//		{
//			inserSql = "update Member set MAC_Address3='" + udid
//					+ "' where id='" + uname + "'";
//			this.sqljdbcTemplate.execute(inserSql);
//		}
//		else if(data.get("MAC_Address3").toString().equals(udid))
//		{
//			
//		}
//		else if(this.isfreebook(attrMap.get("bid").toString()) == false)
//		{
//			return null;
//		}
		


		
		List<Map<String, Object>> listzhangNew = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listzhangPic = new ArrayList<Map<String, Object>>();
		String sqlZhang = "SELECT id from news where title_id=?";
		String sqlPic = "SELECT pic_id, orderby ,'http://114.112.170.45:8080/UpLoadFiles/Images/'+[pic_name] as page FROM  [product_pic] where product_id=? order by pic_id"; 
		listzhangNew = this.sqljdbcTemplate.queryForList(sqlZhang, new Object[] { attrMap.get("bid")});
		
		if(listzhangNew != null && listzhangNew.size() > 0)
		{
			for (int j = 0; j < listzhangNew.size(); j++) 
			{
				listzhangPic = this.sqljdbcTemplate.queryForList(sqlPic,
						new Object[] { listzhangNew.get(j).get("id") });
				listzhangNew.get(j).put("zhang", listzhangPic);
			}	
			
		}
		
		return listzhangNew;
	}

	@Override
	public List<Map<String, Object>> getbooktypes() throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql = "SELECT [ID] as id,[Node_Name] as name   FROM  Node_Tree where parentid = 14 order by taxis";
		data = this.sqljdbcTemplate.queryForList(sql);
		return data;
	}

	// modify 2014-11-03
	@Override
	public Map<String, Object> getuserinfobyc(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT  [ID] as id,[Uname] as uname,Tname as turename,[Tell] as tell,[Email] as email ,Prices as money , 0 as paicount ,(select COUNT(*) from Order_Electronic where UID=? and stats=2) as buycount FROM   [Member] where ID=?";
		List<Map<String, Object>> listdata = this.sqljdbcTemplate.queryForList(
				sql,
				new Object[] { attrMap.get("userid"), attrMap.get("userid") });
		if (listdata != null && listdata.size() > 0) {
			return listdata.get(0);
		} else {
			return map;
		}

	}

	@Override
	public List<Map<String, Object>> getupaiedsbyc(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		Integer limit = Convert.nullToInt(attrMap.get("limit"));
		Integer offset = Convert.nullToInt(attrMap.get("offset"));
		String sql = " select top "
				+ limit
				+ " a1.* from( SELECT l.[id] as id,l.Name as name,l.Start_Price as price,a.[Retention_Price] as retention_price,a.[Retention_Time] "
				+ "as retention_time,'http://114.112.170.45:8080/UpLoadFiles/images/'+p.pic_name  as pic_name,l.Description as description  FROM  [Orders] a,[Lot] l,(select MAX(pic_name) as pic_name,product_id,img_type   from"
				+ " [product_pic] group by product_id,img_type)p where a.MemberID="
				+ attrMap.get("userid")
				+ " and a.LotID=l.id and p.img_type=1 and p.product_id=l.id  ) a1 where a1.id not in"
				+ " (select top "
				+ offset
				+ " a2.id from(SELECT l.[id] as id,l.Name as name,l.Start_Price as price,a.[Retention_Price] as retention_price,a.[Retention_Time] as "
				+ "retention_time,'http://114.112.170.45:8080/UpLoadFiles/images/'+p.pic_name  as pic_name,l.Description as description  FROM  [Orders] a,[Lot] l,(select MAX(pic_name) as pic_name,product_id,img_type   from [product_pic] "
				+ "group by product_id,img_type)p where a.MemberID="
				+ attrMap.get("userid")
				+ " and a.LotID=l.id and p.img_type=1 and p.product_id=l.id ) a2 order by a2.id) order by id";
		// String sql =
		// "SELECT l.[id] as id,l.Name as name,l.Start_Price as price,a.[Retention_Price] as retention_price,a.[Retention_Time] as retention_price,p.pic_name,l.Description as description  FROM  [Orders] a,[Lot] l,product_pic p where MemberID=? and a.LotID=l.id and p.img_type=1 and p.product_id=l.id";
		List<Map<String, Object>> list = this.sqljdbcTemplate.queryForList(sql);
		return list;
	}

	@Override
	public Long paidcount(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		String sql = "SELECT COUNT(*) as count  FROM  [Orders] a,[Lot] l,(select MAX(pic_name) as pic_name,product_id,img_type   from [product_pic] group by product_id,img_type)p where a.MemberID=? and a.LotID=l.id and p.img_type=1 and p.product_id=l.id  ";
		Long count = 0l;
		List<Map<String, Object>> list = this.sqljdbcTemplate.queryForList(sql,
				new Object[] { attrMap.get("userid") });
		if (list != null && list.size() > 0) {
			count = Long.parseLong(list.get(0).get("count").toString());
		}
		return count;
	}

	@Override
	public Boolean checkPwd(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		Boolean b = false;
		try {
			String sql = "SELECT  id,[uname]FROM  [Member] where id=? and Upass=?";
			String strPwd = Getnetmd5.md5(attrMap.get("oldpwd").toString());
			List<Map<String, Object>> list = this.sqljdbcTemplate.queryForList(
					sql, new Object[] { attrMap.get("userid"), strPwd });
			if (list != null && list.size() > 0) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public Boolean updatauserinfo(Map<String, Object> attrMap) throws Exception {
		Boolean b = false;
		String sortStr = "";
		Boolean isFirst = true;
		// if (attrMap.get("uname")!=null) {
		// sortStr+="[Uname] = ";
		// sortStr+=attrMap.get("uname");
		// isFirst=false;
		// }
		if (attrMap.get("tell") != null) {
			if (isFirst) {
				isFirst = false;
			} else {
				sortStr += ",";
			}
			sortStr += "Tell='";
			sortStr += attrMap.get("tell") + "'";
		}
		if (attrMap.get("email") != null) {
			if (isFirst) {
				isFirst = false;
			} else {
				sortStr += ",";
			}
			sortStr += "Email='";
			sortStr += attrMap.get("email") + "'";
		}
		if (attrMap.get("pwd") != null) {
			if (isFirst) {
				isFirst = false;
			} else {
				sortStr += ",";
			}
			sortStr += "Upass='";
			String strPwd = Getnetmd5.md5(attrMap.get("pwd").toString());
			sortStr += strPwd + "'";
		}
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE Member SET " + sortStr + " WHERE ID= "
				+ attrMap.get("userid"));
		try {
			this.sqljdbcTemplate.update(sql.toString());
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public List<Map<String, Object>> getRecommendBooks() throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql = "SELECT [ID] as book_id,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end book_img ,[ThbTitle] as book_name  FROM (select a.ID,a.ThbTitle from News a join  recommended_book d  on a.ID=d.BID) as c left join  (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) b on b.product_id= c.ID";
		data = this.sqljdbcTemplate.queryForList(sql);
		return data;
	}

	@Override
	public List<Map<String, Object>> getBestSellBooks() throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql = "select c.ID as book_id,c.ThbTitle as book_name,c.Prices as book_price , c.author as book_author,c.journal_type as type_id,c.SalesVolume as sales_volume,  case when d.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+d.pic_name end book_img  from (select a.ID,a.ThbTitle,a.Prices,a.journal_type,a.author,b.SalesVolume from News a join Selling b on a.ID=b.BID) c left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) d on c.ID=d.product_id order by sales_volume";
		data = this.sqljdbcTemplate.queryForList(sql);
		return data;
	}

	@Override
	public List<Map<String, Object>> getComment(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub		
		String bookID = attrMap.get("book_id").toString();
		String limit = attrMap.get("limit").toString();
		String offset = attrMap.get("offset").toString();
		
		if(limit == null)
		{
			limit = "10";
		}
		if(offset == null)
		{
			offset = "0";
		}
		
		String comSQL = "select top "
				+ limit
				+ " a.Comment_Content as comment_body , a.id as comment_id , a.userID as comment_uid,"
				+ "a.point as comment_point, b.nickname as comment_nick, a.[Comment_Date] as comment_time,"
				+ "'http://114.112.170.45:8080/UpLoadFiles/FaceImage/'+b.head_img as comment_face "
				+ "from Comment a join Member b on a.userID=b.ID where a.bookID='"
				+ bookID + "'"
				+ " and a.id not in (select top "
				+ offset 
				+ " id from Comment where bookID='"
				+ bookID + "'"
				+ " order by [Comment_Date] desc) order by a.[Comment_Date] desc";
		return this.sqljdbcTemplate.queryForList(comSQL);
	}

	@Override
	public int getZan(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select zan_Count from News where ID='";
		sql += attrMap.get("book_id").toString() + "'";
		int count = this.sqljdbcTemplate.queryForInt(sql);
		return count;
	}

	@Override
	public List<Map<String, Object>> addComment(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
//		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//		String sql = "insert into Comment values('";
//		sql += attrMap.get("book_id") + "','";
//		sql += attrMap.get("comment_user") + "','";
//		sql += attrMap.get("comment_body") + "','";
//		if (attrMap.get("comment_anonymous").equals("0")) {
//			sql += 0 + "',getdate())";
//		} else {
//			sql += 1 + "',getdate())";
//		}
		
		String bookID = attrMap.get("book_id").toString();
		String userID = attrMap.get("comment_user").toString();
		String body = attrMap.get("comment_body").toString();
		body = java.net.URLDecoder.decode(body,"UTF-8");
		String point = attrMap.get("comment_point").toString();
		
		String inSQL = "insert into Comment "
				+ "(bookID,userID,Comment_Content,point,Comment_Date) "
				+ "values("
				+ "'" + bookID + "',"
				+ "'" + userID + "',"
				+ "'" + body + "',"
				+ "'" + point + "',"
				+ "getdate())";

		this.sqljdbcTemplate.execute(inSQL);
		return null;
	}

	@Override
	public List<Map<String, Object>> setZan(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql = "if((select count(uid) from zan_Info where uid='";
		sql += attrMap.get("user_id").toString() + "' and info_id='";
		sql += attrMap.get("book_id").toString()
				+ "')=0) begin insert into zan_Info values('";
		sql += attrMap.get("user_id").toString() + "','";
		sql += attrMap.get("book_id").toString()
				+ "',getdate()) update News set zan_Count=isnull(zan_Count,0)+1 where ID='";
		sql += attrMap.get("book_id").toString()
				+ "' end else begin delete from zan_Info where uid='";
		sql += attrMap.get("user_id").toString() + "' and info_id='";
		sql += attrMap.get("book_id").toString()
				+ "' update News set zan_Count=isnull(zan_Count,0)-1 where ID='";
		sql += attrMap.get("book_id").toString() + "' end";
		this.sqljdbcTemplate.execute(sql);
		return null;
	}

	@Override
	public List<Map<String, Object>> getRelevant(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql = "SELECT top 4 [ID] as book_id,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end book_img ,[ThbTitle] as book_name,Prices as book_price  FROM (select a.ID,a.ThbTitle,a.Prices from News a where journal_type=(select journal_type from News b where ID='"
				+ attrMap.get("book_id")
				+ "')) as c left join  (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) b on b.product_id= c.ID Order By NewID() ";
		data = this.sqljdbcTemplate.queryForList(sql);
		return data;
	}

	@Override
	public Map<String, Object> addBookOrder(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
//		String sql = "insert into Order_Electronic values('";
//		sql += attrMap.get("order_id").toString() + "',0,'";
//		sql += attrMap.get("book_id").toString() + "','";
//		sql += attrMap.get("user").toString() + "','";
//		sql += attrMap.get("user_id").toString() + "','";
//		sql += attrMap.get("book_price").toString() + "',getdate(),'')";
//		this.sqljdbcTemplate.execute(sql);
//		sql = "select c.Order_Number as order_id,c.stats as order_state,c.BID as book_id,c.WinnerID as user_id,c.Retention_Price as book_price,c.Retention_Time as order_time ,c.Payment_Time as pay_time , c.ThbTitle as book_name,c.author as book_author,case when d.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+d.pic_name end book_img from (select a.* ,b.ThbTitle,b.author from Order_Electronic a join News b on a.bid=b.id where Order_Number='";
//		sql += attrMap.get("order_id").toString()
//				+ "') c left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)  d on c.BID=d.product_id";

		
		//订单ID
		String orderID = "YSYC_"+ System.currentTimeMillis();
		
		//书籍ID
		String bookID = attrMap.get("bookID").toString();
		
		//总价
		String allPrice = "";
		String bookSQL = "select ebook_price from News where id = '"+ bookID + "'";
		Map<String, Object> bookData = this.sqljdbcTemplate.queryForMap(bookSQL); ;
		if(bookData != null)
		{
			allPrice = bookData.get("ebook_price").toString();
		}
		
		//购买者
		String userID = attrMap.get("userID").toString();
		//获得者
		String Receive_id = attrMap.get("getID").toString();
		//收货人
		String shouname = attrMap.get("name").toString();
		//手机号
		String tel = attrMap.get("phoneNo").toString();

		//详细地址
		String address = attrMap.get("address").toString();

		//邮编
		String code = attrMap.get("code").toString();

		//付款方式
		String payType = "7";
		//是否已支付
		String ispay = "0";
		//订单状态
		String orderstate = "0";
		//补充说明
		String remark = "电子书";
		
		String crateSQL = "insert into [Order] "
				+ "(order_Number,NewsID,Pro_Prices,memberid,Receive_id,shou_name,shou_tell,shou_Address,"
				+ "shou_posscode,pay_type,pay_static,[static],remark)"
				+ " values('"
				+ orderID + "','" + bookID + "','" + allPrice + "','" + userID + "','" +  Receive_id + "','"
				+ shouname + "','" + tel + "','" + address + "','" + code + "','" + payType + "','" + ispay + "','"
				+ orderstate + "','" + remark
				+ "')";

		this.sqljdbcTemplate.execute(crateSQL);
		
		Map<String, Object> dataRsu = new HashMap<String, Object>();
		dataRsu.put("orderID", orderID);
		return dataRsu;
	}

	@Override
	public List<Map<String, Object>> pay(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String sql = "";
		data = this.sqljdbcTemplate.queryForList(sql);
		return data;
	}

	@Override
	public List<Map<String, Object>> getOrders(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
//		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//		String sql = "select c.Order_Number as order_id,c.stats as order_state,c.BID as book_id,c.WinnerID as user_id,c.Retention_Price as book_price,c.Retention_Time as order_time ,c.Payment_Time as pay_time , c.ThbTitle as book_name,c.author as book_author,case when d.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+d.pic_name end book_img from (select a.* ,b.ThbTitle,b.author from Order_Electronic a join News b on a.bid=b.id where UID='";
//		sql += attrMap.get("user_id").toString()
//				+ "') c left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)  d on c.BID=d.product_id order by c.retention_time desc";
//		data = this.sqljdbcTemplate.queryForList(sql);
//		return data;
		
		String userID = attrMap.get("user_id").toString();
		List<Map<String, Object>> orderData = new ArrayList<Map<String, Object>>();
		String orderSQL = "select order_Number as orderID,pay_static as orderState,Pro_Prices as orderPrice,"
				+ "createdate as orderTime,shou_Address as orderAddress,shou_posscode as orderCode,"
				+ "shou_tell as orderTel,shou_name as orderPeople,newsid,Receive_id"
				+ " from [order] where memberid = '"
				+ userID
				+ "'"
				+ " order by createdate desc";
		orderData = this.sqljdbcTemplate.queryForList(orderSQL);
		
		String bookSQL = "select top 1 id as bookid,thbtitle as title from news where id = ?";
		String userSQL = "select top 1 id as userID,uname as userName,nickname as userNick,"
				+ "'http://114.112.170.45:8080/UpLoadFiles/FaceImage/'+ head_img as userFace from Member where id=?";
		String picSQL = "select top 1 * from product_pic where product_id = ?";
		
		if(orderData != null && orderData.size() > 0)
		{
			for(int i = 0;i < orderData.size(); i++)
			{
				Map<String, Object> bookData = new HashMap<String, Object>();
				Map<String, Object> userData = new HashMap<String, Object>();
				Map<String, Object> picData = new HashMap<String, Object>();
				bookData = this.sqljdbcTemplate.queryForMap(bookSQL, orderData.get(i).get("newsid"));
				picData = this.sqljdbcTemplate.queryForMap(picSQL, orderData.get(i).get("newsid"));
				bookData.put("faceurl", "http://114.112.170.45:8080/UpLoadFiles/Images/"+picData.get("pic_name"));
				orderData.get(i).put("orderBook", bookData);
				
				userData = this.sqljdbcTemplate.queryForMap(userSQL, orderData.get(i).get("Receive_id"));
				orderData.get(i).put("orderUser", userData);
			}
			
		}
		
		return orderData;
	}

	@Override
	public Map<String, Object> getBalance(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "select Prices as balance from Member where ID=";
		sql += attrMap.get("user_id").toString();
		data = this.sqljdbcTemplate.queryForMap(sql);
		return data;
	}

	@Override
	public Map<String, Object> addRechargeOrder(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "insert into Order_Recharge values('";
		sql += attrMap.get("recharge_id").toString() + "',0,'";
		sql += attrMap.get("user").toString() + "','";
		sql += attrMap.get("user_id").toString() + "','";
		sql += attrMap.get("money").toString() + "','',getdate())";
		this.sqljdbcTemplate.execute(sql);
		sql = " select Order_Number as recharge_id,Retention_Price as money ,DraweeID as user_id,stats as recharge_state,Payment_Time as pay_time from Order_Recharge where DraweeID='";
		sql += attrMap.get("user").toString() + "' and Order_Number='";
		sql += attrMap.get("recharge_id").toString() + "'";
		data = this.sqljdbcTemplate.queryForMap(sql);
		return data;
	}

	public boolean recharge(String order) throws Exception {
		String sql = "update Order_Recharge set stats=2 ,payment_time=getdate() where Order_Number='"
				+ order
				+ "' update Member set Prices=Prices+(select Retention_Price from Order_Recharge where Order_Number='"
				+ order
				+ "') where id=(select payeeid from Order_Recharge where Order_Number='"
				+ order + "')";
		this.sqljdbcTemplate.execute(sql);
		return true;
	}

	@Override
	public List<Map<String, Object>> getAllBooks(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String limit = "";
		String offset = " top 0 ";
		String booktype = "";
		String key = "";
		String shoufeitype = "";
		String isbuy = "";
		if (attrMap.get("limit") != null) {
			limit = " top " + attrMap.get("limit");
		}
		if (attrMap.get("offset") != null) {
			offset = " top " + attrMap.get("offset");
		}
		if (attrMap.get("booktype") != null
				&& !attrMap.get("booktype").toString().equals("")) {
			booktype = " and journal_type=" + attrMap.get("booktype");
		}
		if (attrMap.get("key") != null) {
			key = " and ( ThbTitle like '%" + attrMap.get("key")
					+ "%' or author like '%" + attrMap.get("key")
					+ "%' or journal_type like '%" + attrMap.get("key")
					+ "%' )";
		}
		if (attrMap.get("shoufeitype") != null) {
			if (attrMap.get("shoufeitype").equals("0"))
			{
				shoufeitype = " and prices>0.00 and looktype='0'";
			}
			else if(attrMap.get("shoufeitype").equals("1"))
			{
				shoufeitype = " and prices=0.00 and looktype='0'";
			}
			else
			{
				shoufeitype = " and looktype='1' ";
			}
				
		}
		if (attrMap.get("user_id") != null) {
			isbuy = ", case when 2 in (select stats from Order_Electronic where UID='";
			isbuy += attrMap.get("user_id")
					+ "' and BID=ID ) then 1 else 0 end as is_buy";
		} else {
			isbuy = ", 0 as is_buy ";
		}
		String where = booktype + key + shoufeitype;
		String sql = "select " + limit;
		sql += "bookfree,  ID as bookid, ThbTitle as title,author,Description as description,Press as press,Prices as price,Integrity as pegecount,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl ";
		sql += isbuy
				+ " from News left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) b on ID=product_id where nodeid=3 and";
		sql += " ID not in (select ";
		sql += offset;
		sql += " c.ID from News c where 1=1 " + where + ") ";
		sql += where;
		

		data = this.sqljdbcTemplate.queryForList(sql);
		
		//读作者名称
		String sqlAthor = "select tag from news where id=?";
		List<Map<String, Object>> dataAthor = new ArrayList<Map<String, Object>>();
		if(data != null && data.size()>0)
		{
			for(int i=0;i<data.size();i++)
			{
				dataAthor = this.sqljdbcTemplate.queryForList(sqlAthor, new Object[] { data.get(i).get("author") });
				if(dataAthor != null && dataAthor.size() > 0)
				{
					data.get(i).put("author", dataAthor.get(0).get("tag"));
					data.get(i).put("finishstate","100");
				}
			}
		}

		return data;
	}

	@Override
	public Map<String, Object> getBookDetail(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		String isbuy = "";
		if (attrMap.get("user_id") != null) {
			isbuy = ", case when 2 in (select stats from Order_Electronic where UID='";
			isbuy += attrMap.get("user_id") + "' and BID='"
					+ attrMap.get("book_id")
					+ "' ) then 1 else 0 end as is_buy";
		} else {
			isbuy = ", 0 as is_buy ";
		}
		String sql = "select bookfree, c.Node_Name as typename, journal_type as typeid, a.ID as bookid, ThbTitle as title,author,remark as description,Press as press,Prices as price,Integrity as pegecount,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl ";
		sql += isbuy
				+ " from News a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) b on a.ID=product_id left join  Node_Tree c on journal_type=c.ID where a.ID='";
		sql += attrMap.get("book_id") + "' ";
		data = this.sqljdbcTemplate.queryForMap(sql);
		
		//读作者名称
		String sqlAthor = "select tag from news where id=?";
		Map<String, Object> dataAthor = new HashMap<String, Object>();;
		if(data != null )
		{
			dataAthor = this.sqljdbcTemplate.queryForMap(sqlAthor,data.get("author"));
			if(dataAthor != null)
			{
				data.put("author", dataAthor.get("tag"));
				data.put("finishstate","100");
			}
		}
		
		return data;
	}

	@Override
	public List<Map<String, Object>> getBuyBooks(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		
//		String isbuy = "";
//		if (attrMap.get("user_id") != null) {
//			isbuy = ", case when 2 in (select stats from Order_Electronic where UID='";
//			isbuy += attrMap.get("user_id")
//					+ "' and BID='a.ID' ) then 1 else 0 end as is_buy";
//		}
//		String sql = "select  journal_type as typeid, a.ID as bookid, ThbTitle as title,author,Description as description,Press as press,Prices as price,Integrity as finishstate,case when b.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+b.pic_name end faceurl ";
//		sql += isbuy
//				+ " from News a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) b on a.ID=product_id  join  Order_Electronic c on a.ID=c.BID where c.UID='";
//		sql += attrMap.get("user_id") + "'";
//		System.out.println(sql);
		
		String userID = attrMap.get("user_id").toString();
		String sqlBuys = "select * from buys where userID = '" + userID + "'";
		Map<String, Object> data = new HashMap<String, Object>();
		data = this.sqljdbcTemplate.queryForMap(sqlBuys);
		
		List<Map<String, Object>> dataBook = new ArrayList<Map<String, Object>>(); 
		
		String sqlBook = "select ID as bookid, ThbTitle as title, "
				+ "Prices as price "
				+ "from news where id = ?";
		String sqlPic = "select 'http://114.112.170.45:8080/UpLoadFiles/Images/'+ pic_name as faceurl "
				+ "from product_pic where product_id=?";		
		
		dataBook = this.sqljdbcTemplate.queryForList(sqlBook, data.get("bookID"));
		for(int i=0;i<dataBook.size();i++)
		{
			Map<String, Object> dataPic = new HashMap<String, Object>();
			dataPic = this.sqljdbcTemplate.queryForMap(sqlPic, dataBook.get(i).get("bookid"));
			
			dataBook.get(i).put("faceurl", dataPic.get("faceurl"));
		}
		
		return dataBook;
	}



	@Override
	public boolean checkPhone(Map<String, Object> attrMap) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select count(*) from Member where Tell ='"
				+ attrMap.get("phone") + "'";
		if (this.sqljdbcTemplate.queryForInt(sql) > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Map<String, Object> loginThird(String uid) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> mapdata = new HashMap<String, Object>();
//		uid = Getnetmd5.md5(uid);
//		if (uid.replaceAll(" ", "").equals("")) {
//			return null;
//		}
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmm");// 可以方便地修改日期格式
		String order = dateFormat.format(now);
		long i = Math.round(Math.random() * 100);
		i = i < 10 ? 00 : (i > 99 ? 99 : i);

		String sqlhave = "select count(*) from Member where OtherPartyMark='" + uid + "'";
		int count = this.sqljdbcTemplate.queryForInt(sqlhave);
		if(count == 0)
		{
			String sqlInser = "insert into Member (uname,OtherPartyMark,nickname) values('user"
									+ order + i + "',"
									+ "'" + uid + "','匿名用户')";
			
			this.sqljdbcTemplate.execute(sqlInser);						
		}
		
		String sqlselect = " select id as userID,nickName as userNick, [uname] as userName,'http://114.112.170.45:8080/UpLoadFiles/FaceImage/'+ head_img as userFace from Member where OtherPartyMark='" + uid
				+ "'";
		mapdata = this.sqljdbcTemplate
				.queryForMap(sqlselect);
		
		if(mapdata.get("userNick") == null)
		{
			mapdata.put("userNick", "用户"+mapdata.get("userID"));
		}
		return mapdata;
	}
	

	@Override
	public boolean bindingPhone(String id, String phone) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from Member where id='" + id
				+ "' and ( Tell is null or Tell='')";
		// System.out.println(id + "||" + phone);
		List<Map<String, Object>> listData = this.sqljdbcTemplate
				.queryForList(sql);
		if (listData != null && listData.size() == 0) {
			return false;
		}
		sql = "update Member set Tell='" + phone + "' where id='" + id + "'";
		this.sqljdbcTemplate.update(sql);
		return true;
	}

	@Override
	public int changePWD(String id, String oldpwd, String newpwd)
			throws Exception {
		// TODO Auto-generated method stub
		oldpwd = Getnetmd5.md5(oldpwd);
		newpwd = Getnetmd5.md5(newpwd);
		if (oldpwd.replaceAll(" ", "").equals("")
				|| newpwd.replaceAll(" ", "").equals("")) {
			return -1;
		}
		String sql = "select uname from Member where id='" + id
				+ "' and upass='" + oldpwd + "'";
		List<Map<String, Object>> listData = this.sqljdbcTemplate
				.queryForList(sql);
		if (listData != null && listData.size() > 0) {
			sql = "update Member set upass='" + newpwd + "' where id='" + id
					+ "' and upass='" + oldpwd + "'";
			this.sqljdbcTemplate.update(sql);
			return 1;
		}
		return 0;
	}

	@Override
	public int findPWD(String name, String phone, String newpwd)
			throws Exception {
		newpwd = Getnetmd5.md5(newpwd);
		if (newpwd.replaceAll(" ", "").equals("")) {
			return -1;
		}
		String sql = "select * from Member where uname='" + name
				+ "' and Tell='" + phone + "'";
		if (this.sqljdbcTemplate.queryForList(sql).size() == 0) {
			return 0;
		}
		sql = "update Member set upass='" + newpwd + "' where uname='" + name
				+ "' and Tell='" + phone + "'";
		this.sqljdbcTemplate.update(sql);
		return 1;
	}

	@Override
	public Map<String, Object> getOrderDetail(String order_id) throws Exception {
		// TODO Auto-generated method stub
		
//		String sql = "select top 1 c.Order_Number as order_id,c.stats as order_state,c.BID as book_id,c.WinnerID as user_id,c.Retention_Price as book_price,c.Retention_Time as order_time ,c.Payment_Time as pay_time , c.ThbTitle as book_name,c.author as book_author,case when d.pic_name is null then 'http://114.112.170.45:8080/UpLoadFiles/Images/802316c98f9344e884ffdbe453be98ce.jpg' else 'http://114.112.170.45:8080/UpLoadFiles/Images/'+d.pic_name end book_img from (select a.* ,b.ThbTitle,b.author from Order_Electronic a join News b on a.bid=b.id where a.order_number='";
//		sql += order_id
//				+ "') c left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id)  d on c.BID=d.product_id";
//		data = this.sqljdbcTemplate.queryForMap(sql);
//		return data;
		Map<String, Object> orderData = new HashMap<String, Object>();
		String orderSQL = "select top 1 * from [order] where order_number='" + order_id + "'"; 
		orderData = this.sqljdbcTemplate.queryForMap(orderSQL);
		
		Map<String, Object> bookData = new HashMap<String, Object>();
		String bookSQL = "select top 1 id as bookid, thbtitle as title from news where id = '"
				+ orderData.get("NewsID")
				+"'";
		bookData = this.sqljdbcTemplate.queryForMap(bookSQL);
		
		Map<String, Object> picData = new HashMap<String, Object>();
		String picSQL = "select top 1 pic_name from product_pic where product_id = '" + bookData.get("bookid") + "'";
		picData = this.sqljdbcTemplate.queryForMap(picSQL);
		bookData.put("faceurl", "http://114.112.170.45:8080/UpLoadFiles/Images/" + picData.get("pic_name"));
		
		Map<String, Object> userData = new HashMap<String, Object>();
		String userSQL = "select top 1 id as userID,uname as userName,nickname as userNick,"
				+ "'http://114.112.170.45:8080/UpLoadFiles/FaceImage/'+ head_img as userFace "
				+ "from Member where id = '" + orderData.get("Receive_id") + "'";
		userData = this.sqljdbcTemplate.queryForMap(userSQL);
		if(userData.get("userNick")==null)
		{
			userData.put("userNick", "用户"+userData.get("userID"));
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderID", orderData.get("order_number"));
		data.put("orderState", orderData.get("pay_static").toString());
		data.put("orderPrice", orderData.get("Pro_Prices").toString());
		data.put("orderTime", orderData.get("createdate"));
		data.put("orderAddress", orderData.get("shou_Address").toString());
		data.put("orderPeople", orderData.get("shou_name"));
		data.put("orderTel", orderData.get("shou_tell"));
		data.put("orderCode", orderData.get("shou_posscode"));
		data.put("orderBook", bookData);
		data.put("orderUser", userData);
		
		return data;
	}

	@Override
	public Map<String, Object> geturserdetailbyid(String user_id)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "SELECT  [ID] as userID ,'http://114.112.170.45:8080/UpLoadFiles/FaceImage/'+ head_img as userFace , QQ , weixin ,[Uname] as userName, nickName as userNick,[Tell] as tell,[Email] as email ,Member_Level huiyuanlv ,Prices as money , 0 as paicount ,(select COUNT(*) from Order_Electronic where UID=? and stats=2) as buycount FROM   [Member] where ID=?";
		List<Map<String, Object>> listdata = this.sqljdbcTemplate.queryForList(
				sql, new Object[] { user_id, user_id });
		if (listdata != null && listdata.size() > 0) 
		{
			if(listdata.get(0).get("userNick")==null)
			{
				listdata.get(0).put("userNick", "用户"+listdata.get(0).get("userID"));
			}
			return listdata.get(0);
		}
		else 
		{
			return null;
		}
	}

	@Override
	public Map<String, Object> uploaduserinformation(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		String sql;
		// List<Map<String, Object>> listData = this.sqljdbcTemplate
		// .queryForList(sql);
		// if (listData != null && listData.size() > 0) {
		sql = "update Member set nickName=? ,tell=? , email=? , qq=? ,weixin=? where id=?";
		String name = attrMap.get("turename").toString();
		name =  java.net.URLDecoder.decode(name,"UTF-8");
		int code = this.sqljdbcTemplate.update(
				sql,
				new Object[] {name, attrMap.get("tell"),
						attrMap.get("email"), attrMap.get("qq"),
						attrMap.get("weixin"), attrMap.get("user_id") });
		System.out.println("jdbc update return " + code);

		sql = "SELECT  [ID] as userID , 'http://114.112.170.45:8080/UpLoadFiles/Images/'+ head_img as userFace , QQ , weixin ,[Uname] as userName,nickName as userNick, [Tell] as tell,[Email] as email ,Member_Level huiyuanlv ,Prices as money , 0 as paicount ,(select COUNT(*) from Order_Electronic where UID=? and stats=2) as buycount FROM   [Member] where ID=?";
		List<Map<String, Object>> listdata = this.sqljdbcTemplate
				.queryForList(sql, new Object[] { attrMap.get("user_id"),
						attrMap.get("user_id") });
		if (listdata != null && listdata.size() > 0) 
		{
			if(listdata.get(0).get("userNick") == null)
			{
				listdata.get(0).put("userNick", "用户"+listdata.get(0).get("userID"));
			}
			return listdata.get(0);
		} else 
		{
			return null;
		}
		// }
	}

	@Override
	public List<Map<String, Object>> getexpertlist() throws Exception {
		// TODO Auto-generated method stub
		String sqlAuthorList = "SELECT  id as expertid, tag as name, 'http://114.112.170.45:8080/UpLoadFiles/Images/'+img as faceurl from news where nodeid = 2";
		
		return this.sqljdbcTemplate.queryForList(sqlAuthorList);
	}

	@Override
	public Map<String, Object> getexpertdetailbyid(String expertid)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "select id as expertid, tag as name,remark, 'http://114.112.170.45:8080/UpLoadFiles/Images/'+img as faceurl from news where id=?";
		
		String bookSql = "select a.ID as bookid, a.ThbTitle as title, a.Prices as price,'http://114.112.170.45:8080/UpLoadFiles/Images/'+ b.pic_name as faceurl "
				+ "from News a left join (select MAX(pic_name) as pic_name,product_id   from [product_pic] group by product_id) b on product_id=ID  where a.author = '" +  expertid + "' and a.nodeid = 3";

		Map<String, Object> detaiData = sqljdbcTemplate.queryForMap(sql, expertid);
		detaiData.put("books", this.sqljdbcTemplate.queryForList(bookSql));
		
		return detaiData;
	}

	@Override
	public List<Map<String, Object>> getnewslist(String offset, String limit)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "select top "
				+ limit
				+ " newid , title , image , time from new where newid not in(select top "
				+ offset + " newid from new)";
		return sqljdbcTemplate.queryForList(sql);
	}
	
	@Override
	public List<Map<String, Object>> getnewsdatalist(String offset, String limit)
			throws Exception {
		// TODO Auto-generated method stub
		String sql;
		if(offset != null)
		{
			 sql = "select top "
					+ limit
					+ " id as newID , thbtitle as newTitle, 'http://114.112.170.45:8080/UpLoadFiles/Images/'+ img as newImg, IssueDate as newTime from news where nodeid=1 and id not in(select top "
					+ offset + " id from news where nodeid=1)";
			
		}
		else
		{
			 sql = "select top "
						+ "5"
						+ " id as newID , thbtitle as newTitle, 'http://114.112.170.45:8080/UpLoadFiles/Images/'+ img as newImg, IssueDate as newTime from news where id not in(select top "
						+ "1" + " id from news) and nodeid=1";	
			
		}

		return sqljdbcTemplate.queryForList(sql);
	}
	
	@Override
	public Map<String, Object> getnewsdetail(String newid)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "select id as newID, thbtitle as newTitle,"
					+ "'http://114.112.170.45:8080/UpLoadFiles/Images/'+ img as newImg,"
					+ "remark as newsRemark, IssueDate as newTime from news where id=?";
		return sqljdbcTemplate.queryForMap(sql, newid);

	}

	@Override
	public Map<String, Object> getnewsdetailbyid(String newid) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from new where newid=?";
		return sqljdbcTemplate.queryForMap(sql, new Object[] { newid });
	}

	@Override
	public Map<String, Object> getcontactus() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from contact ";
		return sqljdbcTemplate.queryForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getmyadress(String user_id)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "select adressid ,b.name as sheng,a.shengcode as shengID ,c.name as shi,a.shicode as shiID ,d.name as qu,a.qucode as quID ,xiangxi,a.code ,a.name ,a.tel from address a left join province b on a.shengCode=b.id left join city c on a.shiCode=c.id left join District d on a.quCode=d.id where a.userid="
				+ user_id;
		sql += " order by defult desc";
		return sqljdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getshenglist() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select id as code ,name  from province";
		return sqljdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getshilist(String code) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select id as code ,name  from city where ProvinceId=?";
		return sqljdbcTemplate.queryForList(sql, new Object[] { code });
	}

	@Override
	public List<Map<String, Object>> getqulist(String code) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select id as code ,name  from District where cityid=?";
		return sqljdbcTemplate.queryForList(sql, new Object[] { code });
	}

	@Override
	public Map<String, Object> addadress(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		String name = attrMap.get("name").toString();
		name = java.net.URLDecoder.decode(name,"UTF-8");
		
		String xiangxi = attrMap.get("xiangxi").toString();
		xiangxi = java.net.URLDecoder.decode(xiangxi,"UTF-8");
		
		String sql = "insert into address(userid,shengCode,shiCode,quCode,xiangxi,name,tel,code) values('"
				+ attrMap.get("user_id")
				+ "','"
				+ attrMap.get("shengCode")
				+ "','"
				+ attrMap.get("shiCode")
				+ "','"
				+ attrMap.get("quCode")
				+ "','"
				+ xiangxi
				+ "','"
				+ name
				+ "','"
				+ attrMap.get("tel")
				+ "','" + attrMap.get("code") + "')";
		sqljdbcTemplate.execute(sql);
		return null;
	}

	@Override
	public Map<String, Object> updateadress(Map<String, Object> attrMap)
			throws Exception {
		// TODO Auto-generated method stub
		String nick = attrMap.get("name").toString();
		nick = java.net.URLDecoder.decode(nick,"UTF-8");
		String xiangxi = attrMap.get("xiangxi").toString();
		xiangxi = java.net.URLDecoder.decode(xiangxi,"UTF-8");
		String sql = "update address set shengCode='"
				+ attrMap.get("shengCode") + "' ,shiCode='"
				+ attrMap.get("shiCode") + "' ,quCode='"
				+ attrMap.get("quCode") + "' ,xiangxi='"
				+ xiangxi + "' ,name='" + nick 
				+ "' ,tel='" + attrMap.get("tel") + "' ,code='"
				+ attrMap.get("code") + "' where adressid='"
				+ attrMap.get("adressid") + "'";
		sqljdbcTemplate.update(sql);
		return null;
	}

	@Override
	public Map<String, Object> setdefultadress(String userid, String adressid)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "update address set defult=0 where userid=" + userid + "";
		sqljdbcTemplate.update(sql);
		sql = "update address set defult=1 where userid=" + userid
				+ " and adressid=" + adressid;
		sqljdbcTemplate.update(sql);
		return null;
	}

	@Override
	public Map<String, Object> deleteadress(String adressid) throws Exception {
		// TODO Auto-generated method stub
		String sql = "delete from address where adressid='" + adressid + "'";
		sqljdbcTemplate.execute(sql);
		return null;
	}

	@Override
	public Map<String, Object> uploaduserfaceimage(String userid, String name)
			throws Exception {
		// TODO Auto-generated method stub
//		String sql = "insert into userphoto values('" + userid + "','" + name
//				+ "','" + Constants.IMG_URL + "')";
//		sqljdbcTemplate.execute(sql);
//		sql = "update member set faceurl='" + Constants.IMG_URL + name
//				+ "' where id=" + userid;
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("faceurl", "http://114.112.170.45:8080/UpLoadFiles/Images/"
//				+ name);
//		return map;
		String sql = "update Member set head_img='"
				+name
				+ "' where id = '"
				+ userid
				+"'";
		sqljdbcTemplate.execute(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("faceurl", "http://114.112.170.45:8080/UpLoadFiles/FaceImage/"
				+ name);
		return map;
	}
	
	public List<Map<String, Object>> getpaimailist(String offset, String limit) throws Exception {
		// TODO Auto-generated method stub
		if(offset == null && limit == null)
		{
			offset = "0";
			limit = "5";
		}
		String sql = "select top "
					+ limit 
					+ " id as paimaiID,name as paimaiName,lot_number as paimaiCode,"
					+ "assess_prices as paimaiPGprice, Principal as paimaiFang from lot "
					+ "where id not in (select top "
					+ offset
					+ " id from lot order by Create_Date desc) order by Create_Date desc";
		
		List<Map<String, Object>> data = this.sqljdbcTemplate.queryForList(sql);
		String sqlPic = "select * from product_pic where product_id=?";
		List<Map<String, Object>> dataPic = new ArrayList<Map<String, Object>>();
		if(data != null && data.size()>0)
		{
			for(int i=0;i<data.size();i++)
			{
				dataPic = this.sqljdbcTemplate.queryForList(sqlPic,data.get(i).get("paimaiID"));
				if(dataPic != null && dataPic.size()>0)
				{
					String picStr= (String) dataPic.get(0).get("pic_name");
					data.get(i).put("paimaiImg", "http://114.112.170.45:8080/UpLoadFiles/Images/"+picStr);
				}	
			}
		}
		
		return data;
	}

	public Map<String, Object> getpaimaiDetail(String paimaiID) throws Exception {
		// TODO Auto-generated method stub

		String sql = "select "
					+ " id as paimaiID,name as paimaiName,lot_number as paimaiCode,"
					+ "'http://114.112.170.45:8080/UpLoadFiles/Images/' + lot_img as paimaiImg,"
					+ "assess_prices as paimaiPGprice, Principal as paimaiFang, "
					+ "Create_Date as paimaiTime, Telephone as paimaiTel ,Contant_Name as paimaiPeople, "
					+ "Start_Price as paimaiQiPrice,Price_Ladder1 as paimaiJiePrice,Auction_Mode as paimaiStyle,"
					+ "Lot_Time as paimaiBeginTime ,CloseTime as paimaiEndTime ,Lot_Type as paimaiType ,remark as paimaiRemark"
					+ " from lot where id = ?";
		
		Map<String, Object> data = this.sqljdbcTemplate.queryForMap(sql, paimaiID);
		if(data!=null)
		{
			String sqlPic = "select * from product_pic where product_id=?";
			List<Map<String, Object>> dataPic = this.sqljdbcTemplate.queryForList(sqlPic,data.get("paimaiID"));
			if(dataPic != null && dataPic.size()>0)
			{
				String[] pics = new String[dataPic.size()];
				String picStr= (String) dataPic.get(0).get("pic_name");
				data.put("paimaiImg", "http://114.112.170.45:8080/UpLoadFiles/Images/"+picStr);
				for(int i = 0; i < dataPic.size();i++)
				{
					picStr = (String) dataPic.get(i).get("pic_name");
					pics[i]= "http://114.112.170.45:8080/UpLoadFiles/Images/" + picStr;
				}
				data.put("paimaiPics", pics);
			}
		}
		
		return data;
	}
	
	public boolean isbuythisbook(String userID, String bookID) throws Exception
	{
		String sqlBuy = "select count(*) from buys where userID='"
				+ userID
				+ "' and bookID = '" + bookID + "'";
		int count = this.sqljdbcTemplate.queryForInt(sqlBuy);
		
		return count > 0 ? true : false;
	}
	
	public boolean isfreebook(String bookID) throws Exception
	{
		String sql = "select prices from news where id='" + bookID + "'";
		float price = Float.parseFloat(this.sqljdbcTemplate.queryForMap(sql).get("prices").toString());
		
		if(price > 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public List<Map<String, Object>> getallMember(String offset, String limit,String key) throws Exception 
	{
		if(offset == null && limit == null)
		{
			offset = "0";
			limit = "5";
		}
		
		String sql;
		
		if(key == null)
		{
			sql = "select top "
					+ limit 
					+ " id as userID, uname as userName,nickName as userNick,"
					+ "'http://114.112.170.45:8080/UpLoadFiles/FaceImage/' + head_img as userFace  from Member "
					+ "where id not in (select top "
					+ offset
					+ " id from Member)";
		}
		else
		{
			key = "%" + key + "%";
			
			sql = "select top "
					+ limit 
					+ " id as userID, uname as userName,nickName as userNick,"
					+ "'http://114.112.170.45:8080/UpLoadFiles/FaceImage/' + head_img as userFace  from Member "
					+ "where id not in (select top "
					+ offset
					+ " id from Member where uname like '"
					+ key + "'" + " or nickName like '"
					+ key + "'"
					+ ") and ( uname like '"
					+ key + "'" + " or nickName like '"
					+ key + "')";
		}
		
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data = this.sqljdbcTemplate.queryForList(sql);
		if(data != null && data.size()>0)
		{
			for(int i=0;i<data.size();i++)
			{
				if(data.get(i).get("userNick") == null)
				{
					data.get(i).put("userNick", "用户"+data.get(i).get("userID"));
					
				}
			}
		}
		
		return data;
	}
}

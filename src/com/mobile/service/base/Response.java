package com.mobile.service.base;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mobile.service.util.Aes;
import com.mobile.service.util.Constants;
import com.mobile.service.util.Convert;

public class Response implements ResIntf{
	
	private JdbcTemplate sqljdbcTemplate;

	public JdbcTemplate getSqljdbcTemplate() {
		return sqljdbcTemplate;
	}

	public void setSqljdbcTemplate(JdbcTemplate sqljdbcTemplate) {
		this.sqljdbcTemplate = sqljdbcTemplate;
	}
	
	public Map<String, Object> dataWithAuthorInfo(Map<String, Object> mapdata)  throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("authorID", mapdata.get("authorID"));
		result.put("authorName", mapdata.get("authorName"));
		result.put("authorRema", mapdata.get("authorRema"));
		result.put("authorSex", mapdata.get("authorSex"));
		result.put("authorImag", Constants.ImageImagesPath + mapdata.get("authorImag"));
		result.put("authorContent", mapdata.get("authorContent"));
		return result;
	}
	
	public Map<String, Object> dataWithUserInfo(Map<String, Object> mapdata , String c)  throws Exception
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userID", mapdata.get("userID"));
		result.put("userName", mapdata.get("userName"));
		if(mapdata.get("userNick") == null)
		{
			result.put("userNick", "用户"+mapdata.get("userID"));
		}
		else
		{
			result.put("userNick", mapdata.get("userNick"));
		}
		result.put("userPhone", mapdata.get("userPhone"));
		result.put("userEmail", mapdata.get("userEmail"));
		result.put("userSex", mapdata.get("userSex"));
		result.put("userImage", Constants.ImageFacePath + mapdata.get("userImage"));
		result.put("userPrice", mapdata.get("userPrice"));
		result.put("userSign", mapdata.get("userSign"));
		result.put("userLook", mapdata.get("userLook"));
		double money = Double.parseDouble(mapdata.get("userMoney").toString()); 
				;
		DecimalFormat df = new DecimalFormat("######0.00");   
		result.put("userMoney", df.format(money));
		result.put("userType", mapdata.get("userType"));
		result.put("userVIP", mapdata.get("userVIP"));
		
		try{
			Map<String, Object> city = new HashMap<String, Object>();
			String locsql = "select * from [T_Province],[T_City],[T_District] where [T_Province].ProID = '"
					 + mapdata.get("userSheng") + "' and [T_City].CityID = '"
					 + mapdata.get("userShi") + "' and [T_District].Id = '"
					 + mapdata.get("userQu") + "'";
			Map<String, Object> loc = this.sqljdbcTemplate.queryForMap(locsql);
			city.put("codeSheng", mapdata.get("userSheng"));
			city.put("nameSheng", loc.get("ProName"));
			city.put("codeShi", mapdata.get("userShi"));
			city.put("nameShi", loc.get("CityName"));
			city.put("codeQu", mapdata.get("userQu"));
			city.put("nameQu", loc.get("DisName"));
			
			result.put("userCity", city);
		}catch (Exception e) {
			e.printStackTrace();
		}
		result.put("isFan", "0");
		//是否已关注
		if(c != null)
		{
			String[] cs = null;
			try{
				cs = Convert.cToUserId(Aes.Decrypt(c, Constants.ENCODE_KEY));
				if(cs.length > 0)
				{
					String myid = cs[0];
					String fansql = "select count(*) from [fans] where fansK = '"
							+ mapdata.get("userID") + "' and fansZ = '"
							+ myid + "'";
					int have = this.sqljdbcTemplate.queryForInt(fansql);
					if(have > 0)
					{
						result.put("isFan", "1");
					}
					else
					{
						result.put("isFan", "0");
					}
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public Map<String, Object> dataWithBookInfo(Map<String, Object> mapdata , String c , boolean isList)  throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		//图集ID
		result.put("bookID", mapdata.get("bookID"));
		
		//图集名称
		result.put("bookName", mapdata.get("bookName"));
		
		//图集简介
		result.put("bookRemark", mapdata.get("bookRemark"));
		
		//图集创建时间
		result.put("bookTime", mapdata.get("bookTime"));
		
		//封面图片
		result.put("bookImage", Constants.ImageImagesPath + mapdata.get("bookImage"));
		
		//分类ID
		result.put("bookGroup", mapdata.get("bookGroup"));
		
		//页数
		result.put("bookPage", mapdata.get("bookPage"));
		
		//完成状态
		result.put("bookState", mapdata.get("bookState"));
		
		//是否收费（0 免费 1收费）
		result.put("bookFree", mapdata.get("bookFree"));
		
		//是否会员图集（0 否 1 是）
		result.put("bookVIP", mapdata.get("bookVIP"));
		
		//图集价格(金币个数)
		result.put("bookPrice", mapdata.get("bookPrice"));
		
		//下载量
		result.put("bookDowns", mapdata.get("bookDowns"));
		
		//专家
		Map<String, Object> author = this.sqljdbcTemplate.queryForMap("select * from [author] where authorID = '" + mapdata.get("bookAuthor") + "'");
		result.put("author", this.dataWithAuthorInfo(author));
		
		//评论数量 和 评分
		String countsql = "select count(*) as count,sum(commentPoint) as he from [pinglun] where bookID = '" + mapdata.get("bookID") + "'";
		Map<String, Object> count = this.sqljdbcTemplate.queryForMap(countsql);
		if(Integer.parseInt(count.get("count").toString()) != 0)
		{
			int bookComments = Integer.parseInt(count.get("count").toString());
			float he = Float.parseFloat(count.get("he").toString());
			result.put("bookComments", bookComments);

			DecimalFormat df = new DecimalFormat("######0.0");
			result.put("bookPoint", df.format((float)he  / (float)bookComments));
		}
		else
		{
			result.put("bookComments", "0");
			result.put("bookPoint", "3");
		}

		
		if(isList == false)
		{
			//分类名称
			Map<String, Object> group = this.sqljdbcTemplate.queryForMap("select * from [typeb] where groupID = '" + mapdata.get("bookGroup") + "'");
			result.put("bookGroupName", group.get("groupName"));
			
			//详细介绍
			result.put("bookContext", mapdata.get("bookContext"));
			
			//是否已购买
			result.put("isBuy", "0");
			String[] cs = null;
			try{
				cs = Convert.cToUserId(Aes.Decrypt(c, Constants.ENCODE_KEY));
				String userID = cs[0];
				String buysql = "select count(*) from [purchase] where bookID = '" + mapdata.get("bookID") + "' and userID = '" + userID + "'";
				int buys = this.sqljdbcTemplate.queryForInt(buysql);
				if(buys > 0){
					result.put("isBuy", "1");
				}
			}catch (Exception e) {
			}
			
			//分享链接
			result.put("shareURL", Constants.ShareHtmlPath + mapdata.get("bookID"));
		}

		
		return result;
	}
	
	public Map<String, Object> dataWithComment(Map<String, Object> mapdata)  throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("commentID", mapdata.get("commentID"));
		result.put("commentPoint", mapdata.get("commentPoint"));
		result.put("commentText", mapdata.get("commentText"));
		result.put("commentTime", mapdata.get("commentTime"));
		result.put("userID", mapdata.get("userID"));
		result.put("userNick", mapdata.get("userNick"));
		result.put("userImage", Constants.ImageFacePath + mapdata.get("userImage"));
		return result;
	}
	
	public Map<String, Object> dataWithTalk(Map<String, Object> mapdata)  throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("talkID", mapdata.get("talkID"));
		result.put("talkTime", mapdata.get("talkTime"));
		result.put("talkText", mapdata.get("talkText"));
		result.put("talkFrom", mapdata.get("talkFrom"));
		
		//说说图片
		List<Map <String , Object>> pics = this.sqljdbcTemplate.queryForList("select * from [talkpic] where talkID = '" + mapdata.get("talkID") + "'");
		if(pics.size() > 0)
		{
			List<String> images =  new ArrayList<String>(pics.size());
			for(int i = 0; i < pics.size(); i++)
			{
				images.add(Constants.ImageTalkPath + pics.get(i).get("talkImage"));
			}
			result.put("talkImages", images);
		}
		
		//发布人信息
		Map<String, Object> sender = new HashMap<String, Object>();
		sender.put("userID", mapdata.get("userID"));
		sender.put("userNick", mapdata.get("userNick"));
		sender.put("userImage", Constants.ImageFacePath + mapdata.get("userImage"));
		result.put("sender", sender);
		
		//点赞列表
		String zansql = "select * from [talkZan],[user] where [talkZan].userID = [user].userID and [talkZan].talkID = '"
				+ mapdata.get("talkID") + "'";
		List<Map <String , Object>> zans = this.sqljdbcTemplate.queryForList(zansql);
		result.put("zanCount", zans.size());
		
		result.put("zanList", this.dataWithTalkZans("0", "10", mapdata.get("talkID").toString()));
		
		//评论列表
		String comsel = "select * from [talkCom],[user] where [talkCom].userID = [user].userID and [talkCom].talkID = '"
				+ mapdata.get("talkID") + "' and (replayID = 0 or replayID is null)";
		List<Map <String , Object>> coms = this.sqljdbcTemplate.queryForList(comsel);
		result.put("comCount", coms.size());
		
		result.put("comList", this.dataWithTalkComment("0", "3", mapdata.get("talkID").toString()));
		
		
		return result;
	}
	
	public List<Map <String , Object>> dataWithTalkComment(String offset , String limit , String talkID) throws Exception{
		
		if(offset == null) offset = "0";
		if(limit == null) limit = "10";
		
		try{
			String comsel = "select top "
					+ limit
					+ " * from [talkCom],[user] where [talkCom].userID = [user].userID and [talkCom].talkID = '"
					+ talkID + "' and (replayID = 0 or replayID is null) and [talkCom].talkID not in (select top "
					+ offset + " [talkCom].talkID from [talkCom],[user] where [talkCom].userID = [user].userID and [talkCom].talkID = '"
					+ talkID + "' and (replayID = 0 or replayID is null) )";
			List<Map <String , Object>> coms = this.sqljdbcTemplate.queryForList(comsel);

			if(coms.size() > 0)
			{
				List<Map <String , Object>> comments =  new ArrayList<Map<String, Object>>(coms.size());
				for(int i = 0; i < coms.size(); i++)
				{
					Map<String, Object> comcontent = new HashMap<String, Object>();
					comcontent.put("comID", coms.get(i).get("commentID"));
					comcontent.put("comUser", coms.get(i).get("userID"));
					comcontent.put("comNick", coms.get(i).get("userNick"));
					comcontent.put("comImage", Constants.ImageFacePath + coms.get(i).get("userImage"));
					comcontent.put("comText", coms.get(i).get("text"));
					comcontent.put("comTime", coms.get(i).get("time"));
					
					String replaysql = "select * from [talkCom],[user] where [talkCom].userID = [user].userID and [talkCom].talkID = '"
							+ talkID + "' and [talkCom].replayID = '"
							+ coms.get(i).get("commentID") + "'";
					List<Map <String , Object>> res = this.sqljdbcTemplate.queryForList(replaysql);
					List<Map <String , Object>> replays =  new ArrayList<Map<String, Object>>(res.size());
					for(int j = 0 ; j < res.size() ; j ++)
					{
						Map<String, Object> replay = new HashMap<String, Object>();
						replay.put("replayID", res.get(j).get("commentID"));
						replay.put("replayUser", res.get(j).get("userID"));
						replay.put("replayNick", res.get(j).get("userNick"));
						replay.put("replayImage", Constants.ImageFacePath + res.get(j).get("userImage"));
						replay.put("replayText", res.get(j).get("text"));
						replay.put("replayTime", res.get(j).get("time"));
						
						Map<String, Object> accept = this.sqljdbcTemplate.queryForMap("select * from [user] where userID = '" + res.get(j).get("accept") + "'");
						replay.put("acceptUser", accept.get("userID"));
						replay.put("acceptNick", accept.get("userNick"));
						replay.put("acceptImage", Constants.ImageFacePath + accept.get("userImage"));
						replays.add(replay);
					}
					comcontent.put("replays", replays);
					
					comments.add(comcontent);
				}
				return comments;
			}
		}catch (Exception e) {
		}
		
		return null;
	}
	
	public List<Map <String , Object>> dataWithTalkZans(String offset , String limit , String talkID) throws Exception{
		
		if(offset == null) offset = "0";
		if(limit == null) limit = "10";
		
		try{
			String zansql = "select top "
					+ limit 
					+ " * from [talkZan],[user] where [talkZan].userID = [user].userID and [talkZan].talkID = '"
					+ talkID + "' and [talkZan].talkID not in (select top "
					+ offset + 
					" [talkZan].talkID from [talkZan],[user] where [talkZan].userID = [user].userID and [talkZan].talkID = '"
					+ talkID + "')";
			List<Map <String , Object>> zans = this.sqljdbcTemplate.queryForList(zansql);

			List<Map <String , Object>> zanList =  new ArrayList<Map<String, Object>>(zans.size());
			for(int i = 0; i < (zans.size() > 10 ? 10 : zans.size()); i++)
			{
				Map<String, Object> zanUser = new HashMap<String, Object>();
				zanUser.put("userID", zans.get(i).get("userID"));
				zanUser.put("userNick", zans.get(i).get("userNick"));
				zanUser.put("userImage", Constants.ImageFacePath + zans.get(i).get("userImage"));
				
				zanList.add(zanUser);
			}
			return zanList;
		}catch (Exception e) {
		}
		
		return null;
	}
	
	public Map<String, Object> dataWithGood(Map<String, Object> mapdata , boolean isList)  throws Exception{
		
		mapdata.put("goodImage", Constants.ImageGoodPath + mapdata.get("goodImage"));
		
		if(!isList)
		{
			String picsql = "select * from [goodImage] where goodID = '" + mapdata.get("goodID") + "'";
			List<Map <String , Object>> pics = this.sqljdbcTemplate.queryForList(picsql);
			List<String> images =  new ArrayList<String>(pics.size());
			for(int i = 0 ; i < pics.size(); i++)
			{
				images.add(Constants.ImageGoodPath + pics.get(i).get("goodImageName").toString());
			}
			mapdata.put("images", images);
		}
		
		return mapdata;
	}
	
	public boolean bookAlredyBuy(String bookID , String userID) throws Exception {
		int count = this.sqljdbcTemplate.queryForInt("select count(*) from [purchase] where userID = '" + userID + "' and bookID = '" + bookID + "'");
		if(count > 0)
		{
			return true;
		}
		return false;
	}
	
	public boolean idcarAlredyReview(String userID) throws Exception {		
		try{
			int ishave = this.sqljdbcTemplate.queryForInt("select count(*) from [idcar] where userID = '" + userID + "'");
			if(ishave == 0)
			{
				return false;
			}
			
			Map<String, Object> data = this.sqljdbcTemplate.queryForMap("select * from [idcar] where userID = '" + userID + "'");
			if(data != null)
			{
				if(data.get("idcarReview").toString().equals("1"))
				{
					return true;
				}
			}
		}
		catch (Exception e) {
		}
		return false;
	}
}
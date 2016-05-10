package com.mobile.service.util;

public class StyleUtil {

	public static String DETAIL_OUTER_DIV_START = "<div style = \"width:320px; height:auto;overflow:hidden\">";
	public static String DETAIL_IMAGE_DIV_START = "<div style = \"float:left\">";
	public static String DETAIL_DIV_LEFT_START = "<div style = \"float:left; margin-left:10px\">";
	public static String DETAIL_INTRO_DIV_START = "<div style = \"float:right\">";
	public static String DETAIL_CLEAR_DIV_START = "<div style = \"clear:both\">";
	public static String DETAIL_DIV_START = "<div>";
	public static String DETAIL_DIV_INNER_START = "<div style = \"margin-top:10px\">";
	public static String DETAIL_DIV_END = "</div>";
	public static String DETAIL_P_START = "<p>";
	public static String DETAIL_P_END = "</p>";
	public static String formatDetail(String image, String head, String content){
		String result = DETAIL_OUTER_DIV_START
							+ DETAIL_IMAGE_DIV_START
								+ "<img src = \"" + image + "\">"
							+ DETAIL_DIV_END
							+ DETAIL_CLEAR_DIV_START
							+ DETAIL_DIV_END
							+ DETAIL_DIV_START
							+ head
							+ DETAIL_DIV_END
							+ DETAIL_DIV_START
							+ content
							+ DETAIL_DIV_END
					  + DETAIL_DIV_END;
		return result;
	}
	
	public static String formatHead(String organizerName, String timeStart, String timeEnd, String address){
		String result = DETAIL_P_START
					  	+ organizerName
					  + DETAIL_P_END
					  + DETAIL_P_START
					  	+ timeStart;
	  	if(timeEnd != null && !timeEnd.equals("")){
	  		result += " - " + timeEnd;
	  	}
					  result += DETAIL_P_END
					  + DETAIL_P_START
					  	+ address
					  + DETAIL_P_END;
		return result;
	}
	
	public static String formatThreadDetail(String image, String userId, String userNick, String content){
		String result = DETAIL_OUTER_DIV_START
							+ DETAIL_IMAGE_DIV_START
								+ "<img src = \"" + image + "\" width=\"80px\" height=\"80px\">" +
										"<input type = \"hidden\" value = \"" + userId + "\" name = \"uban_userId\" id = \"uban_userId\"/>" +
										"<input type = \"hidden\" value = \"" + userNick + "\" name = \"uban_userNick\" id = \"uban_userNick\"/>"
							+ DETAIL_DIV_END
							+ DETAIL_DIV_LEFT_START
								+ DETAIL_DIV_START
									+ userNick
								+ DETAIL_DIV_END
								+ DETAIL_DIV_INNER_START
									+ "<input type = \"button\" id = \"goAction\" value = \"Ta的活动\" style = \"background-color: rgb(59, 152, 236); color:#ffffff; border: rgb(30, 137, 234) 1px solid\">"
									+ "<input type = \"button\" id = \"goThread\" value = \"Ta的约伴\" style = \"background-color: rgb(59, 152, 236); color:#ffffff; border: rgb(30, 137, 234) 1px solid\">"
								+ DETAIL_DIV_END
							+ DETAIL_DIV_END
							+ DETAIL_CLEAR_DIV_START
							+ DETAIL_DIV_START
							+ content
							+ DETAIL_DIV_END
					  + DETAIL_DIV_END;
		return result;
	}
}

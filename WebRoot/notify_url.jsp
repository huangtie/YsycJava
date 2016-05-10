
<%
	/* *
	 功能：支付宝服务器异步通知页面
	 版本：3.3
	 日期：2012-08-17
	 说明：
	 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

	 //***********页面功能说明***********
	 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
	 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
	 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
	 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
	 //********************************
	 * */
%>
<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="java.sql.*"%>
<%
	//获取支付宝POST过来反馈信息
	Map<String, String> params = new HashMap<String, String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter
			.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//商户订单号


	
	String out_trade_no = new String(request.getParameter(
			"out_trade_no").getBytes("ISO-8859-1"), "GBK");

	//支付宝交易号

	String trade_no = new String(request.getParameter("trade_no")
			.getBytes("ISO-8859-1"), "GBK");

	//交易状态
	String trade_status = new String(request.getParameter(
			"trade_status").getBytes("ISO-8859-1"), "GBK");
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

	if (AlipayNotify.verify(params)) {//验证成功
		//////////////////////////////////////////////////////////////////////////////////////////
		//请在这里加上商户的业务逻辑程序代码

		/*//responseTxt=true isSign=false 
		//返回回来的参数：
		body=yishangyacang&buyer_email=261788479@qq.com
		&buyer_id=2088202455553410&discount=0.00&gmt_close=2015-01-17 17:41:51
		&gmt_create=2015-01-17 17:41:51&gmt_payment=2015-01-17 17:41:51
		&is_total_fee_adjust=N&notify_id=4317203a319b994589f553ffcee2b3f04a
		&notify_time=2015-01-17 17:41:51&notify_type=trade_status_sync
		&out_trade_no=book201501171740494&payment_type=1&price=0.01&quantity=1
		&seller_email=yishangyacang@yeah.net&seller_id=2088711353384690
		&sign=V/XpwOgdDpKLSppkfQFMm7YN7612xOTUl9Z/32NVNKiDGIL+RZ39Q8AVh5+7ypEURdUyS3SBanlWvQZFcWrxJIRH9Qr1tkd/7trvphsIwDDDXUFqSEd7htz74mMpYOtxb/SzgzQn7MHzuS3hVDHnTIPqxe/gJZ5d1Vim8ajTtxc=
		&sign_type=RSA&subject=测试&total_fee=0.01&trade_no=2015011736811941
		&trade_status=TRADE_FINISHED&use_coupon=N
		 */

		out.println("success"); //请不要修改或删除
 
		String DBDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		//String DBURL = "jdbc:sqlserver://114.112.170.45;databaseName=test1;";
		String DBURL = "jdbc:sqlserver://114.112.170.45;databaseName=shangjian_DB;";
		String DBUSER = "sa";
		String DBPASS = "risenb2014$";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Connection conn2 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		
		Connection conn3 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs3 = null;
	
		String order = params.get("out_trade_no"); // 接收标摊参数
		String total_fee = params.get("total_fee"); // 接受表单参数
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);

			String sql1 = "update [order] set pay_static=1,pay_time=getdate() where Order_Number='";
			sql1 += order + "'";
			pstmt = conn.prepareStatement(sql1);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				//如果有内容，则此处执行，表示查询出来，含合法用户
			}

		       if (rs != null) 
		        {
		            try 
		            {
		                rs.close();
		            } catch (SQLException e) 
		            {
		                e.printStackTrace();
		            }
		        }
		        
		        if (pstmt != null) {
		            try {
		            	pstmt.close();
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        }
				
		        if(conn != null)
		        {  // 关闭连接对象   
		       		try{   
		          		conn.close() ;   
		       		}catch(SQLException e){   
		          		e.printStackTrace() ;   
		      		 }   
		        }  
			
		} catch (Exception e) {
		} 
/* 
 
		
		String bookID;
		String userID;
		try {
			Class.forName(DBDRIVER);
			conn2 = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
	
			String sql2 = "select * from [order] where Order_Number = '"+order+"'";
			pstmt2 = conn2.prepareStatement(sql2);
			rs2 = pstmt2.executeQuery();

			while (rs2.next()) 
			{
				bookID = rs2.getString("newid");
				userID = rs2.getString("Receive_id");
			}
			
	        if (rs2 != null) 
	        {
	            try 
	            {
	                rs2.close();
	            } catch (SQLException e) 
	            {
	                e.printStackTrace();
	            }
	        }
			
	        if (pstmt2 != null) 
	        {
	            try {
	            	pstmt2.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        
	        if(conn2 != null)
	        {  // 关闭连接对象   
	       		try{   
	          		conn.close() ;   
	       		}catch(SQLException e){   
	          		e.printStackTrace() ;   
	      		 }   
	        } 
			
		} catch (Exception e) {
		} 
		

		
		try {
			Class.forName(DBDRIVER);
			conn3 = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		
		
			String sql3 = "insert into [buys] (bookID,userID) values ('"
					+bookID
					+"','"
					+userID
					+"')";
			pstmt3 = conn3.prepareStatement(sql3);
			rs3 = pstmt3.executeQuery();
			if (rs3.next()) 
			{

			}
			
	        if (rs3 != null) 
	        {
	            try 
	            {
	                rs3.close();
	            } catch (SQLException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	        
	        if (pstmt3 != null) 
	        {
	            try {
	            	pstmt3.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
			
	        if(conn != null)
	        {  // 关闭连接对象   
	       		try{   
	          		conn3.close() ;   
	       		}catch(SQLException e){   
	          		e.printStackTrace() ;   
	      		 }   
	        } 
			
		} catch (Exception e) {
		} */
		

		
		
		
		//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

		if (trade_status.equals("TRADE_FINISHED")) {
			//判断该笔订单是否在商户网站中已经做过处理
			//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
			//如果有做过处理，不执行商户的业务程序

			//注意：
			//该种交易状态只在两种情况下出现
			//1、开通了普通即时到账，买家付款成功后。
			//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
		} else if (trade_status.equals("TRADE_SUCCESS")) {
			//判断该笔订单是否在商户网站中已经做过处理
			//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
			//如果有做过处理，不执行商户的业务程序

			//注意：
			//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
		}

		//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——


		//////////////////////////////////////////////////////////////////////////////////////////
	} else {//验证失败
		out.println("fail");
	}
%>

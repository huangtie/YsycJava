
<%
	/* *
	 ���ܣ�֧�����������첽֪ͨҳ��
	 �汾��3.3
	 ���ڣ�2012-08-17
	 ˵����
	 ���´���ֻ��Ϊ�˷����̻����Զ��ṩ���������룬�̻����Ը����Լ���վ����Ҫ�����ռ����ĵ���д,����һ��Ҫʹ�øô��롣
	 �ô������ѧϰ���о�֧�����ӿ�ʹ�ã�ֻ���ṩһ���ο���

	 //***********ҳ�湦��˵��***********
	 ������ҳ���ļ�ʱ�������ĸ�ҳ���ļ������κ�HTML���뼰�ո�
	 ��ҳ�治���ڱ������Բ��ԣ��뵽�������������ԡ���ȷ���ⲿ���Է��ʸ�ҳ�档
	 ��ҳ����Թ�����ʹ��д�ı�����logResult���ú�����com.alipay.util�ļ��е�AlipayNotify.java���ļ���
	 ���û���յ���ҳ�淵�ص� success ��Ϣ��֧��������24Сʱ�ڰ�һ����ʱ������ط�֪ͨ
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
	//��ȡ֧����POST����������Ϣ
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
		//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת��
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}

	//��ȡ֧������֪ͨ���ز������ɲο������ĵ���ҳ����תͬ��֪ͨ�����б�(���½����ο�)//
	//�̻�������


	
	String out_trade_no = new String(request.getParameter(
			"out_trade_no").getBytes("ISO-8859-1"), "GBK");

	//֧�������׺�

	String trade_no = new String(request.getParameter("trade_no")
			.getBytes("ISO-8859-1"), "GBK");

	//����״̬
	String trade_status = new String(request.getParameter(
			"trade_status").getBytes("ISO-8859-1"), "GBK");
	
	//��ȡ֧������֪ͨ���ز������ɲο������ĵ���ҳ����תͬ��֪ͨ�����б�(���Ͻ����ο�)//

	if (AlipayNotify.verify(params)) {//��֤�ɹ�
		//////////////////////////////////////////////////////////////////////////////////////////
		//������������̻���ҵ���߼��������

		/*//responseTxt=true isSign=false 
		//���ػ����Ĳ�����
		body=yishangyacang&buyer_email=261788479@qq.com
		&buyer_id=2088202455553410&discount=0.00&gmt_close=2015-01-17 17:41:51
		&gmt_create=2015-01-17 17:41:51&gmt_payment=2015-01-17 17:41:51
		&is_total_fee_adjust=N&notify_id=4317203a319b994589f553ffcee2b3f04a
		&notify_time=2015-01-17 17:41:51&notify_type=trade_status_sync
		&out_trade_no=book201501171740494&payment_type=1&price=0.01&quantity=1
		&seller_email=yishangyacang@yeah.net&seller_id=2088711353384690
		&sign=V/XpwOgdDpKLSppkfQFMm7YN7612xOTUl9Z/32NVNKiDGIL+RZ39Q8AVh5+7ypEURdUyS3SBanlWvQZFcWrxJIRH9Qr1tkd/7trvphsIwDDDXUFqSEd7htz74mMpYOtxb/SzgzQn7MHzuS3hVDHnTIPqxe/gJZ5d1Vim8ajTtxc=
		&sign_type=RSA&subject=����&total_fee=0.01&trade_no=2015011736811941
		&trade_status=TRADE_FINISHED&use_coupon=N
		 */

		out.println("success"); //�벻Ҫ�޸Ļ�ɾ��
 
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
	
		String order = params.get("out_trade_no"); // ���ձ�̯����
		String total_fee = params.get("total_fee"); // ���ܱ�����
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);

			String sql1 = "update [order] set pay_static=1,pay_time=getdate() where Order_Number='";
			sql1 += order + "'";
			pstmt = conn.prepareStatement(sql1);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				//��������ݣ���˴�ִ�У���ʾ��ѯ���������Ϸ��û�
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
		        {  // �ر����Ӷ���   
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
	        {  // �ر����Ӷ���   
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
	        {  // �ر����Ӷ���   
	       		try{   
	          		conn3.close() ;   
	       		}catch(SQLException e){   
	          		e.printStackTrace() ;   
	      		 }   
	        } 
			
		} catch (Exception e) {
		} */
		

		
		
		
		//�������������ҵ���߼�����д�������´�������ο�������

		if (trade_status.equals("TRADE_FINISHED")) {
			//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
			//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
			//���������������ִ���̻���ҵ�����

			//ע�⣺
			//���ֽ���״ֻ̬����������³���
			//1����ͨ����ͨ��ʱ���ˣ���Ҹ���ɹ���
			//2����ͨ�˸߼���ʱ���ˣ��Ӹñʽ��׳ɹ�ʱ�����𣬹���ǩԼʱ�Ŀ��˿�ʱ�ޣ��磺���������ڿ��˿һ�����ڿ��˿�ȣ���
		} else if (trade_status.equals("TRADE_SUCCESS")) {
			//�жϸñʶ����Ƿ����̻���վ���Ѿ���������
			//���û�������������ݶ����ţ�out_trade_no�����̻���վ�Ķ���ϵͳ�в鵽�ñʶ�������ϸ����ִ���̻���ҵ�����
			//���������������ִ���̻���ҵ�����

			//ע�⣺
			//���ֽ���״ֻ̬��һ������³��֡�����ͨ�˸߼���ʱ���ˣ���Ҹ���ɹ���
		}

		//�������������ҵ���߼�����д�������ϴ�������ο�������


		//////////////////////////////////////////////////////////////////////////////////////////
	} else {//��֤ʧ��
		out.println("fail");
	}
%>

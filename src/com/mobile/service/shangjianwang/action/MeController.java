package com.mobile.service.shangjianwang.action;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mobile.bean.Captcha;
import com.mobile.service.base.CommonJson;
import com.mobile.service.base.CommonParam;
import com.mobile.service.base.ListJson;
import com.mobile.service.base.MapJson;
import com.mobile.service.base.PushUtil;
import com.mobile.service.shangjianwang.service.impl.MeServiceImpl;
import com.mobile.service.util.Aes;
import com.mobile.service.util.Check;
import com.mobile.service.util.Constants;
import com.mobile.service.util.Convert;
import com.mobile.service.util.ImageUtil;

@Controller
public class MeController {

	private MeServiceImpl meService;
	
	public MeServiceImpl getMeService() {
		return meService;
	}

	public void setMeService(MeServiceImpl meService) {
		this.meService = meService;
		System.out.println("start");
		new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(5000);
						checkCaptcha(null, 0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private List<Captcha> captchaList = new ArrayList<Captcha>();

	/**
	 * @param capthca
	 *            ��֤��
	 * @param what
	 *            0ȥ����ʱ��֤�� ��1������֤�� ��2ɾ����֤��
	 * @return 1 60S�ڲ����ظ���ȡ ��2��֤�ɹ�
	 */
	private synchronized int checkCaptcha(Captcha captcha, int what) {
		int re = 0;
		switch (what) {
		case 0:
			for (int i = 0; i < captchaList.size(); i++) {
				Captcha c = captchaList.get(i);
				if (c.gettime() + 1800 < c.getDate()) {// ��֤��30����ʱЧ
					captchaList.remove(c);
				}
				// System.out.print(c.getcaptcha() + " ");
			}
			// System.out.println(" ");
			break;
		case 1:
			for (int i = 0; i < captchaList.size(); i++) {
				Captcha c = captchaList.get(i);
				if (c.getphone().equals(captcha.getphone())) {
					if (c.gettime() + 60 < captcha.getDate()) {// 60S�ڲ��ظ���ȡ
						captchaList.remove(c);
					} else {
						return 1;
					}
				}
			}
			captchaList.add(captcha);
			break;
		case 2:
			for (int i = 0; i < captchaList.size(); i++) {
				Captcha c = captchaList.get(i);
				if (c.getphone().equals(captcha.getphone())
						&& c.getcaptcha() == captcha.getcaptcha()) {
					captchaList.remove(c);
					return 2;
				}
			}
			break;
		}

		return re;
	}

	/**
	 * 2.1.10. �����û���Ϣ
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/updatauserinfo")
	public @ResponseBody CommonJson updatauserinfo(HttpServletRequest request,
			HttpSession session) {
		CommonJson json = new CommonJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();

			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					// ��ȡ�ж��ٱ���
					attrMap.put("userid", temp[0]);
					// if (request.getParameter("uname") != null) {
					// attrMap.put("attrMap", request.getParameter("uname"));
					// }
					if (request.getParameter("tell") != null) {
						attrMap.put("tell", request.getParameter("tell"));
					}
					if (request.getParameter("email") != null) {
						attrMap.put("email", request.getParameter("email"));
					}
					if (request.getParameter("pwd") != null) {
						if (request.getParameter("oldpwd") != null) {
							attrMap.put("oldpwd",
									request.getParameter("oldpwd"));
							if (!meService.checkPwd(attrMap)) {
								json.setErrorMsg("�����벻��ȷ");
								json.setSuccess(false);
								return json;
							}
						} else {
							json.setErrorMsg("�����벻��Ϊ��");
							json.setSuccess(false);
							return json;
						}
						attrMap.put("pwd", request.getParameter("pwd"));

					}
					Boolean b = meService.updatauserinfo(attrMap);
					json.setSuccess(b);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * ��ȡ���ͼ��
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getfreebooks")
	public @ResponseBody ListJson getfreebooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			Integer offset = 0;
			if (!Check.isEmpty(request.getParameter("offset"))) {
				offset = Integer.parseInt(request.getParameter("offset"));
			}
			attrMap.put("offset", offset);
			Integer limit = Constants.pageSize;
			if (!Check.isEmpty(request.getParameter("limit"))) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			attrMap.put("limit", limit);
			// ��ȡ������
			Integer type = 0;
			if (request.getParameter("type") != null) {
				try {
					type = Integer.parseInt(request.getParameter("type"));
					attrMap.put("type", type);
				} catch (Exception e) {
					json.setErrorMsg("ͼ�����������ʹ���");
					json.setSuccess(false);
					return json;
				}

			}
			// ģ��ͷ����
			// ��ȡ�ж��ٱ���
			Long l = meService.countfreebook(attrMap);
			// ��ȡ���е�����
			List<Map<String, Object>> data = meService.getfreebooks(attrMap);

			json.setData(data);
			json.setCount(l);
			json.setSuccess(true);
			// ģ��β��ʼ
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * ��ȡ�ѹ�ͼ��
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getbuyedbooks")
	public @ResponseBody ListJson getbuyedbooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			Integer offset = 0;
			if (!Check.isEmpty(request.getParameter("offset"))) {
				offset = Integer.parseInt(request.getParameter("offset"));
			}
			attrMap.put("offset", offset);
			Integer limit = Constants.pageSize;
			if (!Check.isEmpty(request.getParameter("limit"))) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			attrMap.put("limit", limit);
			// ��ȡ������
			Integer type = 0;
			if (request.getParameter("type") != null) {
				try {
					type = Integer.parseInt(request.getParameter("type"));
					attrMap.put("type", type);
				} catch (Exception e) {
					json.setErrorMsg("ͼ�����������ʹ���");
					json.setSuccess(false);
					return json;
				}

			}
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					// ��ȡ�ж��ٱ���
					attrMap.put("userid", temp[0]);
					Long l = meService.countbuyedbook(attrMap);

					// ��ȡ���е�����
					List<Map<String, Object>> data = meService
							.getbuyedbooks(attrMap);
					json.setData(data);
					json.setCount(l);
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * ��ȡ�����ܿ����飨������ѵĺ��ѹ��ģ�
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getallcanreadbooks")
	public @ResponseBody ListJson getallcanreadbooks(
			HttpServletRequest request, HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			Integer offset = 0;
			if (!Check.isEmpty(request.getParameter("offset"))) {
				offset = Integer.parseInt(request.getParameter("offset"));
			}
			attrMap.put("offset", offset);
			Integer limit = Constants.pageSize;
			if (!Check.isEmpty(request.getParameter("limit"))) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			attrMap.put("limit", limit);
			// ��ȡ������
			Integer type = 0;
			if (request.getParameter("type") != null) {
				try {
					type = Integer.parseInt(request.getParameter("type"));
					attrMap.put("type", type);
				} catch (Exception e) {
					json.setErrorMsg("ͼ�����������ʹ���");
					json.setSuccess(false);
					return json;
				}

			}
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					// ��ȡ�ж��ٱ���
					Long l = meService.countcanreadbook(attrMap);
					attrMap.put("userid", temp[0]);
					// ��ȡ���е�����
					List<Map<String, Object>> data = meService
							.getallcanreadbooks(attrMap);
					json.setData(data);
					json.setCount(l);
					json.setSuccess(true);

					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				// ֻ��ȡ��ѵ�
				Long l = meService.countfreebook(attrMap);
				// ��ȡ���е�����
				List<Map<String, Object>> data = meService
						.getfreebooks(attrMap);
				json.setData(data);
				json.setCount(l);
				json.setSuccess(true);
			}
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * 2.1.5. ��ȡ�������б�������ݹؼ��������б�
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getbookcitybooks")
	public @ResponseBody ListJson getbookcitybooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			Integer offset = 0;
			if (!Check.isEmpty(request.getParameter("offset"))) {
				offset = Integer.parseInt(request.getParameter("offset"));
			}
			attrMap.put("offset", offset);
			Integer limit = Constants.pageSize;
			if (!Check.isEmpty(request.getParameter("limit"))) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			attrMap.put("limit", limit);
			// ��ȡ������
			Integer type = 0;
			if (request.getParameter("type") != null) {
				try {
					type = Integer.parseInt(request.getParameter("type"));
					attrMap.put("type", type);
				} catch (Exception e) {
					json.setErrorMsg("ͼ�����������ʹ���");
					json.setSuccess(false);
					return json;
				}

			}
			String key = request.getParameter("key");
			attrMap.put("key", Convert.toUtf8(Convert.nullToString(key)));
			// attrMap.put("key",key);
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				// ģ��ͷ����
				// ��ȡ�ж��ٱ���
				attrMap.put("userid", temp[0]);
			}
			Long l = meService.countbookcitybook(attrMap);

			// ��ȡ���е�����
			List<Map<String, Object>> data = meService
					.getbookcitybooks(attrMap);
			json.setData(data);
			json.setCount(l);
			json.setSuccess(true);
			// ģ��β��ʼ

		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * 2.1.6. ������id��ȡÿ�������ϸ�½�
	 * 
	 * @author xjch liaodq-2014-11-18-modify
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getbookdetailbyid")
	public @ResponseBody ListJson getbookdetailbyid(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String bid = request.getParameter("id");
			String udid = request.getParameter("udid");
			if (c == null || bid == null || udid == null) {
				json.setErrorMsg("ȱ�ٲ���");
				json.setSuccess(false);
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				System.out.println("decrypt");
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) 
				{
					if(meService.isfreebook(bid)==false && meService.isbuythisbook(temp[0], bid) == false)
					{
						json.setErrorMsg("2160");
						json.setSuccess(false);
						return json;
					}
					
					attrMap.put("bid", bid);
					attrMap.put("username", temp[0]);
					attrMap.put("udid", udid);
					// ��ȡ���е�����
					List<Map<String, Object>> data = meService
							.getbookdetailbyid(attrMap);
					if (data == null && meService.isfreebook(bid) == false) 
					{
						json.setErrorMsg("2161");
						json.setSuccess(false);
						return json;
					}
					
					json.setData(data);
					json.setCount(Long.parseLong(data.size() + ""));
					System.out.println("long");
					json.setSuccess(true);
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * 2.1.7. ��ȡ�鼮���� ����
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getbooktypes")
	public @ResponseBody ListJson getbooktypes(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			Integer offset = 0;
			if (!Check.isEmpty(request.getParameter("offset"))) {
				offset = Integer.parseInt(request.getParameter("offset"));
			}
			attrMap.put("offset", offset);
			Integer limit = Constants.pageSize;
			if (!Check.isEmpty(request.getParameter("limit"))) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			attrMap.put("limit", limit);
			// ��ȡ���е�����
			List<Map<String, Object>> data = meService.getbooktypes();
			json.setData(data);
			json.setCount(Long.valueOf(data.size()));
			json.setSuccess(true);

			// ģ��β��ʼ

		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * 2.1.8. ���ݵ�¼��־��ȡ������ϸ����
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getuserinfobyc")
	public @ResponseBody MapJson getuserinfobyc(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("userid", temp[0]);

					// ��ȡ���е�����
					Map<String, Object> data = meService
							.getuserinfobyc(attrMap);
					json.setData(data);
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * 2.1.9. ���ݵ�¼��־��ȡ������Ʒ
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getupaiedsbyc")
	public @ResponseBody ListJson getupaiedsbyc(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(false);
		// ģ��ͷ��ʼ
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			Integer offset = 0;
			if (!Check.isEmpty(request.getParameter("offset"))) {
				offset = Integer.parseInt(request.getParameter("offset"));
			}
			attrMap.put("offset", offset);
			Integer limit = Constants.pageSize;
			if (!Check.isEmpty(request.getParameter("limit"))) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			attrMap.put("limit", limit);
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("userid", temp[0]);

					// ��ȡ���е�����
					List<Map<String, Object>> list = meService
							.getupaiedsbyc(attrMap);
					// ��ȡһ���ж��ٸ�
					Long paidCount = meService.paidcount(attrMap);
					json.setData(list);
					json.setCount(paidCount);
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				String error_msg = "����ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}
		}
		return json;
	}

	/**
	 * ��ȡ�Ƽ��鼮 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getrecommendbooks")
	public @ResponseBody ListJson getRecommendBooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getRecommendBooks());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * ��ȡ�����鼮 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getbestsellbooks")
	public @ResponseBody ListJson getBestSellBooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getBestSellBooks());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	};

	/**
	 * ��ȡ����/�� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getcommentzan")
	public @ResponseBody ListJson getCommentZan(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		String book_id = request.getParameter("book_id");
		String limit = request.getParameter("limit");
		String offset = request.getParameter("offset");
		if (book_id == null || book_id.equals("")) {
			json.setErrorMsg("��������");
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			attrMap.put("book_id", book_id);
			if(limit != null)
			{
				attrMap.put("limit", limit);
			}
			else
			{
				attrMap.put("limit", "10");
			}
			if(offset != null)
			{
				attrMap.put("offset", offset);
			}
			else
			{
				attrMap.put("offset", "0");
			}
			
			json.setData(meService.getComment(attrMap));
			//json.setCount(meService.getZan(attrMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		// String expireTime = Convert.dateToExpire();
		// String src = "1" + "+" + expireTime;
		// try {
		// String enString = Aes.Encrypt(src, Constants.ENCODE_KEY);
		// json.setErrorMsg(enString);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return json;
	};

	/**
	 * ���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/addcomment")
	public @ResponseBody ListJson addComment(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String book_id = request.getParameter("bookID");
			String comment_body = request.getParameter("comment_body");
			String comment_point = request
					.getParameter("comment_point");
			if (book_id == null || book_id.equals("")) {
				json.setErrorMsg("�鼮ID");
				json.setSuccess(false);
				return json;
			} else if (comment_body == null || comment_body.equals("")) {
				json.setErrorMsg("����������");
				json.setSuccess(false);
				return json;
			}else if (c == null || c.equals("")) {
				json.setErrorMsg("ȱc");
				json.setSuccess(false);
				return json;
			}
			
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("comment_user", temp[0]);
					attrMap.put("comment_body", comment_body);
					attrMap.put("comment_point", comment_point);
					attrMap.put("book_id", book_id);
					json.setData(meService.addComment(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * �� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/setzan")
	public @ResponseBody ListJson setZan(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String book_id = request.getParameter("book_id");
			if (book_id == null || book_id.equals("")) {
				json.setErrorMsg("��������");
				json.setSuccess(false);
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					attrMap.put("book_id", book_id);
					json.setData(meService.setZan(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ��ȡ����鼮 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getrelevant")
	public @ResponseBody ListJson getRelevant(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		String book_id = request.getParameter("book_id");
		if (book_id == null || book_id.equals("")) {
			json.setErrorMsg("��������");
			json.setSuccess(false);
			return json;
		}
		attrMap.put("book_id", book_id);
		try {
			json.setData(meService.getRelevant(attrMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		json.setSuccess(true);
		// json.setErrorMsg("���Բ���");

		return json;
	};

	/**
	 * �鼮���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/addbookorder")
	public @ResponseBody MapJson addBookOrder(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			
			
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String book_id = request.getParameter("bookID");
			String user_id = request.getParameter("userID");
			String getID = request.getParameter("getID");
			String name = request.getParameter("name");
			if(name == null)
			{
				name = "δ��";
			}
			String phoneNo = request.getParameter("phoneNo");
			if(phoneNo == null)
			{
				phoneNo = "δ��";
			}
			String code = request.getParameter("code");
			if(code == null)
			{
				code = "δ��";
			}
			String address = request.getParameter("address");
			if(address == null)
			{
				address = "δ��";
			}
			if (book_id == null || book_id.equals("")) {
				json.setErrorMsg("��������");
				json.setSuccess(false);
				return json;
			} else if (user_id == null || user_id.equals("")) {
				json.setErrorMsg("��������");
				json.setSuccess(false);
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					boolean isbuy = meService.isbuythisbook(getID, book_id);
					if(isbuy == true)
					{
						json.setErrorMsg("0001");
						json.setSuccess(false);
						return json;
					}
					
					attrMap.put("userID", temp[0]);
					attrMap.put("bookID", book_id);
					attrMap.put("userID", user_id);
					attrMap.put("getID", getID);
					attrMap.put("name", name);
					attrMap.put("phoneNo", phoneNo);
					attrMap.put("code", code);
					attrMap.put("address", address);
					json.setData(meService.addBookOrder(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ֧�� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/pay")
	public @ResponseBody ListJson pay(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			// json.setData(meService.pay(attrMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ��ȡ���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getorders")
	public @ResponseBody ListJson getOrders(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					json.setData(meService.getOrders(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * �˻���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getbalance")
	public @ResponseBody MapJson getBalance(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					json.setData(meService.getBalance(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ��ֵ���� 2014-10-19
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/addrechargeorder")
	public @ResponseBody MapJson addRechargeOrder(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String user_id = request.getParameter("user_id");
			String money = request.getParameter("money");
			if (money == null || money.equals("")) {
				json.setErrorMsg("��������");
				json.setSuccess(false);
				return json;
			} else if (user_id == null || user_id.equals("")) {
				json.setErrorMsg("��������");
				json.setSuccess(false);
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user", temp[0]);
					attrMap.put("user_id", user_id);
					attrMap.put("money", money);
					attrMap.put("recharge_id", getOrderID("recharge"));
					json.setData(meService.addRechargeOrder(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	private String getOrderID(String str) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// ���Է�����޸����ڸ�ʽ
		String order = dateFormat.format(now);
		long i = Math.round(Math.random() * 10);
		return str + order + i;
	}

	/**
	 * ��ȡ�̳��鼮�б� 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getallbooks")
	public @ResponseBody ListJson getAllBooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			if (request.getParameter("shoufeitype") != null)
				attrMap.put("shoufeitype", request.getParameter("shoufeitype"));
			if (request.getParameter("booktype") != null)
				attrMap.put("booktype", request.getParameter("booktype"));
			if (request.getParameter("key") != null)
				attrMap.put("key", Convert.toUtf8(Convert.nullToString(request
						.getParameter("key"))));
			if (request.getParameter("offset") != null)
				attrMap.put("offset", request.getParameter("offset"));
			if (request.getParameter("limit") != null)
				attrMap.put("limit", request.getParameter("limit"));
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					attrMap.put("user_id", temp[0]);
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			}
			json.setData(meService.getAllBooks(attrMap));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * �����鼮id��ȡ�鼮���� 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getbookdetail")
	public @ResponseBody MapJson getBookDetail(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String book_id = request.getParameter("book_id");
			if (book_id == null || book_id.equals("")) {
				json.setErrorMsg("��������");
				json.setSuccess(false);
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			}
			attrMap.put("book_id", book_id);
			json.setData(meService.getBookDetail(attrMap));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * �ѹ��鼮 2014-11-03
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getbuybooks")
	public @ResponseBody ListJson getBuyBooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					json.setData(meService.getBuyBooks(attrMap));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};



	// private int getPass() {
	// int c = (int) (Math.random() * 10 + Math.random() * 100 + Math.random()
	// * 1000 + Math.random() * 10000 + Math.random() * 100000 + Math
	// .random() * 1000000);
	// return c < 100000 ? c * 10 : (c > 999999 ? c / 10 : c);
	// }

	/**
	 * ��֤�� 2014-11-13
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getcode")
	public @ResponseBody MapJson captcha(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String phone = request.getParameter("phone");
		if (phone == null) {
			json.setSuccess(false);
			json.setErrorMsg("�ֻ��Ų���Ϊ��");
			return json;
		}
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put("phone", phone);
		// try {
		// if (!meService.checkPhone(attrMap)) {
		// json.setSuccess(false);
		// json.setErrorMsg("�ֻ����ѱ�ע��");
		// return json;
		// }
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// json.setErrorMsg("�ڲ�����");
		// json.setSuccess(false);
		// }

		// new Thread() {
		// public void run() {
		Captcha captcha = new Captcha(phone);
		if (checkCaptcha(captcha, 1) == 1) {
			json.setSuccess(false);
			json.setErrorMsg("60S�ڲ����ظ���ȡ��֤��");
		} else {
			// String msg =
			// "http://106.ihuyi.cn/webservice/sms.php?method=Submit&account=cf_liaodq&password=123321&mobile="
			// + phone
			// + "&content=������֤���ǣ�"
			// + captcha.getcaptcha()
			// + "���벻Ҫ����֤��й¶�������ˡ�";

			// String result = HttpRequestProxy.captcha(msg);
			String result = "<SubmitResult xmlns=\"http:\\106.ihuyi.cn\\\"><code>2<\\code><msg>提交成功<\\msg>";
			if (result.substring(result.indexOf("<code>") + 6,
					result.indexOf("<code>") + 7).equals("2")) {
				json.setSuccess(true);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("code", captcha.getcaptcha());
				json.setData(data);
			} else {
				json.setErrorMsg("�ڲ�����");
				json.setSuccess(false);
			}

			// ��ø�Ԫ�ؽ��

		}
		// }
		// }.start();
		return json;
	}

	/**
	 * ��������¼ 2014-11-17
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login_third")
	public @ResponseBody MapJson loginThird(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(false);
		try {

			String uid = request.getParameter("uid");
			if (!Check.isEmpty(uid)) {
				Map<String, Object> data = meService.loginThird(uid);
				if (data != null) {
					json.setData(data);
					json.setSuccess(true);
				} else {
					String error_msg = "MD5";
					json.setErrorMsg(error_msg);
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("uid����Ϊ��");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		return json;
	}

	/**
	 * ���ֻ� 2014-11-18
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/bindingphone")
	public @ResponseBody MapJson bindingPhone(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String phone = request.getParameter("phone");
		final String code = request.getParameter("code");
		final String c = request.getParameter("c");
		if (c == null || phone == null || code == null || code.equals("")) {
			json.setSuccess(false);
			json.setErrorMsg("ȱ�ٲ���");
			return json;
		}
		try {
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// new Thread() {
					// public void run() {
					Captcha captcha = new Captcha(phone);
					captcha.setcaptcha(Integer.parseInt(code));
					if (checkCaptcha(captcha, 2) == 2) {
						if (!meService.bindingPhone(temp[0], phone)) {
							json.setSuccess(false);
							json.setErrorMsg("���û��Ѱ��ֻ�");
							return json;
						}
						json.setSuccess(true);
					} else {
						json.setSuccess(false);
						json.setErrorMsg("��֤����Ч");
						return json;
					}
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	@RequestMapping(value = "/changepwd")
	public @ResponseBody MapJson changePWD(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String oldpwd = request.getParameter("oldpwd");
		final String newpwd = request.getParameter("newpwd");
		final String c = request.getParameter("c");
		if (oldpwd == null || newpwd == null || c == null) {
			json.setErrorMsg("ȱ�ٲ���");
			json.setSuccess(false);
			return json;
		}
		try {
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					int change = meService.changePWD(temp[0], oldpwd, newpwd);
					if (0 == change) {
						json.setSuccess(false);
						json.setErrorMsg("�������");
						return json;
					} else if (change == -1) {
						json.setErrorMsg("MD5");
						json.setSuccess(false);
						return json;
					}
					json.setSuccess(true);
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		return json;
	}

	/**
	 * �һ����� 2014-11-19
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/findpassword")
	public @ResponseBody MapJson findPWD(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String phone = request.getParameter("phone");
		final String code = request.getParameter("code");
		final String username = request.getParameter("username");
		final String newpwd = request.getParameter("newpwd");
		if (newpwd == null || username == null || phone == null || code == null
				|| code.equals("")) {
			json.setSuccess(false);
			json.setErrorMsg("ȱ�ٲ���");
			return json;
		}
		try {
			// new Thread() {
			// public void run() {
			Captcha captcha = new Captcha(phone);
			captcha.setcaptcha(Integer.parseInt(code));
			if (checkCaptcha(captcha, 2) == 2) {
				int find = meService.findPWD(username, phone, newpwd);
				if (find == -1) {
					json.setErrorMsg("MD5");
					json.setSuccess(false);
					return json;
				} else if (find == 0) {
					json.setErrorMsg("�û������ֻ��Ų���");
					json.setSuccess(false);
					return json;
				}
				json.setSuccess(true);
			} else {
				json.setSuccess(false);
				json.setErrorMsg("��֤����Ч");
				return json;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	/**
	 * ���ݶ���ID��ȡ�������� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getorderdetail")
	public @ResponseBody MapJson getorderdetail(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String order_id = request.getParameter("order_id");
		if (order_id == null) {
			json.setSuccess(false);
			json.setErrorMsg("ȱ�ٲ���");
			return json;
		}
		try {
			Map<String, Object> map = meService.getOrderDetail(order_id);
			json.setData(map);
			if (map == null || map.isEmpty())
				json.setSuccess(false);
			else
				json.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	/**
	 * �����û�ID��ȡ�û����� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/geturserdetailbyid")
	public @ResponseBody MapJson geturserdetailbyid(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String user_id = request.getParameter("user_id");
		if (user_id == null) {
			json.setSuccess(false);
			json.setErrorMsg("ȱ�ٲ���");
			return json;
		}
		try {
			Map<String, Object> map = meService.geturserdetailbyid(user_id);
			json.setData(map);
			if (map == null || map.isEmpty())
				json.setSuccess(false);
			else
				json.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	/**
	 * �޸��û����� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/uploaduserinformation")
	public @ResponseBody MapJson uploaduserinformation(
			HttpServletRequest request, HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			request.setCharacterEncoding("gbk");
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String turename = request.getParameter("turename");
			System.out.println(turename);
			System.out.println(URLDecoder.decode(turename));
			System.out.println(new String(request.getParameter("turename")
					.getBytes("ISO-8859-1"), "UTF-8"));
			String tell = request.getParameter("tell");
			String email = request.getParameter("email");
			String qq = request.getParameter("QQ");
			String weixin = request.getParameter("weixin");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("�����µ�¼");
				json.setSuccess(false);
				return json;
			}
			attrMap.put("turename", turename);
			attrMap.put("tell", tell);
			attrMap.put("email", email);
			attrMap.put("qq", qq);
			attrMap.put("weixin", weixin);
			json.setData(meService.uploaduserinformation(attrMap));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}

	/**
	 * ��ȡר���Ŷ��б� 2015-01-59
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getexpertlist")
	public @ResponseBody ListJson getexpertlist(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getexpertlist());
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ����ר��ID��ȡר������ 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getexpertdetailbyid")
	public @ResponseBody MapJson getexpertdetailbyid(
			HttpServletRequest request, HttpSession session) {
		MapJson json = new MapJson();
		final String expertid = request.getParameter("expertid");
		if (expertid == null) {
			json.setSuccess(false);
			json.setErrorMsg("ȱ�ٲ���");
			return json;
		}
		try {
			Map<String, Object> map = meService.getexpertdetailbyid(expertid);
			json.setData(map);
			if (map == null || map.isEmpty())
				json.setSuccess(false);
			else
				json.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	private boolean isNumber(String str) {
		if (str == null)
			return false;
		if (str.substring(0, 1).equals("-"))
			str = str.substring(1);
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ȡ��Ѷ�б� 2015-01-59
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getnewslist")
	public @ResponseBody ListJson getnewslist(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		final String offset = request.getParameter("offset");
		final String limit = request.getParameter("limit");
		if (!isNumber(offset) || !isNumber(limit)) {
			json.setSuccess(false);
			json.setErrorMsg("��������");
			return json;
		}
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getnewslist(offset, limit));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ������ѶID��ȡ��Ѷ���� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getnewsdetailbyid")
	public @ResponseBody MapJson getnewsdetailbyid(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		final String newid = request.getParameter("newid");
		if (newid == null) {
			json.setSuccess(false);
			json.setErrorMsg("��������");
			return json;
		}
		try {
			Map<String, Object> map = meService.getnewsdetailbyid(newid);
			json.setData(map);
			if (map == null || map.isEmpty())
				json.setSuccess(false);
			else
				json.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	/**
	 * ��ȡ��ϵ�������� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getcontactus")
	public @ResponseBody MapJson getcontactus(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		// final String newid = request.getParameter("newid");
		// if (newid == null) {
		// json.setSuccess(false);
		// json.setErrorMsg("��������");
		// return json;
		// }
		try {
			Map<String, Object> map = meService.getcontactus();
			json.setData(map);
			if (map == null || map.isEmpty())
				json.setSuccess(false);
			else
				json.setSuccess(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		// }
		// }.start();
		return json;
	}

	/**
	 * ��ȡ�ҵ��ջ���ַ�б� 2015-01-29
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getmyadress")
	public @ResponseBody ListJson getmyadress(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					json.setData(meService.getmyadress(temp[0]));
					json.setSuccess(true);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ��ȡʡ�б� 2015-01-29
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getshenglist")
	public @ResponseBody ListJson getshenglist(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		// final String offset = request.getParameter("offset");
		// final String limit = request.getParameter("limit");
		// if (!isNumber(offset) || !isNumber(limit)) {
		// json.setSuccess(false);
		// json.setErrorMsg("��������");
		// return json;
		// }
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getshenglist());
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ����ʡID��ȡ���б� 2015-01-29
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getshilist")
	public @ResponseBody ListJson getshilist(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		final String code = request.getParameter("code");
		if (!isNumber(code)) {
			json.setSuccess(false);
			json.setErrorMsg("��������");
			return json;
		}
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getshilist(code));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * ������ID��ȡ���б� 2015-01-29
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getqulist")
	public @ResponseBody ListJson getqulist(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		final String code = request.getParameter("code");
		if (!isNumber(code)) {
			json.setSuccess(false);
			json.setErrorMsg("��������");
			return json;
		}
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getqulist(code));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	};

	/**
	 * �����ջ���ַ 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addadress")
	public @ResponseBody MapJson addadress(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String shengCode = request.getParameter("shengCode");
			String shiCode = request.getParameter("shiCode");
			String quCode = request.getParameter("quCode");
			String xiangxi = request.getParameter("xiangxi");
			String name = request.getParameter("name");
			String tel = request.getParameter("tel");
			String code = request.getParameter("code");
			if (shengCode == null || shiCode == null || quCode == null
					|| xiangxi == null || name == null || tel == null) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
				return json;
			}
			attrMap.put("shengCode", shengCode);
			attrMap.put("shiCode", shiCode);
			attrMap.put("quCode", quCode);
			attrMap.put("xiangxi", xiangxi);
			attrMap.put("name", name);
			attrMap.put("tel", tel);
			attrMap.put("code", code);
			json.setData(meService.addadress(attrMap));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}

	/**
	 * �޸��ջ���ַ 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/updateadress")
	public @ResponseBody MapJson updateadress(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String shengCode = request.getParameter("shengCode");
			String shiCode = request.getParameter("shiCode");
			String quCode = request.getParameter("quCode");
			String xiangxi = request.getParameter("xiangxi");
			String name = request.getParameter("name");
			String tel = request.getParameter("tel");
			String code = request.getParameter("code");
			String adressid = request.getParameter("adressid");
			if (shengCode == null || shiCode == null || quCode == null
					|| xiangxi == null || name == null || tel == null
					|| adressid == null) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
				return json;
			}
			attrMap.put("shengCode", shengCode);
			attrMap.put("shiCode", shiCode);
			attrMap.put("quCode", quCode);
			attrMap.put("xiangxi", xiangxi);
			attrMap.put("name", name);
			attrMap.put("tel", tel);
			attrMap.put("code", code);
			attrMap.put("adressid", adressid);
			json.setData(meService.updateadress(attrMap));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}

	/**
	 * ����Ĭ�ϵ�ַ 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/setdefultadress")
	public @ResponseBody MapJson setdefultadress(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String adressid = request.getParameter("adressid");
			if (adressid == null) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					json.setData(meService.setdefultadress(temp[0], adressid));
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
				return json;
			}
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}

	/**
	 * ɾ���ջ���ַ 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteadress")
	public @ResponseBody MapJson deleteadress(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			String adressid = request.getParameter("adressid");
			if (adressid == null) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					// ģ��ͷ����
					attrMap.put("user_id", temp[0]);
					json.setData(meService.deleteadress(adressid));
					// ģ��β��ʼ
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
				return json;
			}
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}

	/**
	 * �ϴ�/�޸��û�ͷ�� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/uploaduserfaceimage")
	public @ResponseBody MapJson uploaduserfaceimage(
			HttpServletRequest request, HttpSession session) {

		MapJson json = new MapJson();
		json.setSuccess(true);
		try {
			Map<String, Object> attrMap = new HashMap<String, Object>();
			String c = request.getParameter("c");
			if (!Check.isEmpty(c)) {
				// ����C����������½ʱ����û����������½ʱ�䳬���涨ʱ�䣬����ʾ���µ�½�����������û���������ȡ��ϸ��Ϣ
				String enString = Aes.Decrypt(c, Constants.ENCODE_KEY);
				String[] temp = Convert.cToUserId(enString);
				if (Convert.dateThan(temp[1])) {
					MultipartFile contentfile = ((MultipartHttpServletRequest) request)
							.getFile("image");
					if (!contentfile.isEmpty()) {
						String name[] = ImageUtil.getName(contentfile
								.getOriginalFilename());
						String originalUrl = name[0];
						String thumbnailUrl = name[1];
						// FileOutputStream fileOS = new FileOutputStream(
						// (new StringBuilder(Constants.BASE_PATH)).append(
						// originalUrl).toString());
						//String path = request.getRealPath("/image");
						String path = "d:\\web\\UpLoadFiles\\FaceImage";
						File file = new File(path);
						if (!file.exists() || !file.isDirectory()) {
							file.mkdirs();
						}
						FileOutputStream fileOS = new FileOutputStream(path
								+ "/" + originalUrl);
						fileOS.write(contentfile.getBytes());
						fileOS.close();
						// int imageHeight = ImageUtil.imageScale(
						// (new StringBuilder(Constants.BASE_PATH)).append(
						// originalUrl).toString(),
						// (new StringBuilder(Constants.BASE_PATH)).append(
						// thumbnailUrl).toString(), Integer.valueOf(240));
						// Image image = new Image();
						// image.setUrl((new
						// StringBuilder(Constants.HOST_PATH)).append(
						// thumbnailUrl).toString());
						// image.setWidth(240);
						// image.setHeight(imageHeight);
						// image.setOriginalUrl((new
						// StringBuilder(Constants.HOST_PATH))
						// .append(originalUrl).toString());
						// list.add(image);
						json.setData(meService.uploaduserfaceimage(temp[0],
								originalUrl));
					} else {
						json.setSuccess(false);
					}
				} else {
					json.setErrorMsg("session���ڻ��û��Ѿ���ɾ��");
					json.setSuccess(false);
					return json;
				}
			} else {
				json.setErrorMsg("���ȵ�¼");
				json.setSuccess(false);
				return json;
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setErrorMsg("����");
			return json;
		}

		return json;
	}
	
	/**
	 * ��ȡ��Ѷ�б� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getnewsList")
	public @ResponseBody ListJson getnewsList(HttpServletRequest request,
			HttpSession session) 
	{
		ListJson json = new ListJson();
		final String offset = request.getParameter("offset");
		final String limit = request.getParameter("limit");
		if(offset != null || limit !=null)
		{
			if (!isNumber(offset) || !isNumber(limit)) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
		}
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getnewsdatalist(offset, limit));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}
	
	/**
	 * ��ȡ��Ѷ���� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getnewsDetail")
	public @ResponseBody MapJson getnewsDetail(HttpServletRequest request,
			HttpSession session) 
			{
		MapJson json = new MapJson();
		final String newID = request.getParameter("newsID");
		if(newID == null)
		{
			json.setErrorMsg("ȱ�ٲ���");
			json.setSuccess(false);
			return json;
		}
		try
		{
			json.setData(meService.getnewsdetail(newID));
			json.setSuccess(true);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		
		return json;
	}
	
	/**
	 * ����Ʒ�б� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getpaimaiList")
	public @ResponseBody ListJson getpaimaiList(HttpServletRequest request,
			HttpSession session) 
	{
		ListJson json = new ListJson();
		final String offset = request.getParameter("offset");
		final String limit = request.getParameter("limit");
		if(offset != null || limit !=null)
		{
			if (!isNumber(offset) || !isNumber(limit)) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
		}
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getpaimailist(offset, limit));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}
	
	/**
	 * ��ȡ�������� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getpaimaiDetail")
	public @ResponseBody MapJson getpaimaiDetail(HttpServletRequest request,
			HttpSession session) 
			{
		MapJson json = new MapJson();
		final String paimaiID = request.getParameter("paimaiID");
		if(paimaiID == null)
		{
			json.setErrorMsg("ȱ�ٲ���");
			json.setSuccess(false);
			return json;
		}
		try
		{
			json.setData(meService.getpaimaiDetail(paimaiID));
			json.setSuccess(true);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		
		return json;
	}
	
	/**
	 * ����Ʒ�б� 2015-01-29
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getallMember")
	public @ResponseBody ListJson getallMember(HttpServletRequest request,
			HttpSession session) 
	{
		ListJson json = new ListJson();
		final String offset = request.getParameter("offset");
		final String limit = request.getParameter("limit");
		final String key = request.getParameter("key");
		
		if(offset != null || limit !=null)
		{
			if (!isNumber(offset) || !isNumber(limit)) {
				json.setSuccess(false);
				json.setErrorMsg("��������");
				return json;
			}
		}
		json.setSuccess(true);
		// json.setCount(1l);
		try {
			json.setData(meService.getallMember(offset, limit,key));
			json.setSuccess(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// json.setErrorMsg("���Բ���");
		return json;
	}
	
	
	
	
	/**
	 * �û���¼ 2.1
	 * 
	 * @author xjch
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login")
	public @ResponseBody MapJson accountLogin(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		try {	
			String userName = request.getParameter("userName");
			String userPassword = request.getParameter("userPassword");
			String userType = request.getParameter("userType");
			String userPart = request.getParameter("userPart");
			json.setErrorMsg("��������");
			json.setErrorCode("9998");
			if(!Check.isEmpty(userType))
			{
				if(userType.equals("1"))
				{
					if(Check.isEmpty(userName) || Check.isEmpty(userPassword))
					{
						return json;
					}
				}
				if(userType.equals("2") || userType.equals("3") || userType.equals("4"))
				{
					if(Check.isEmpty(userPart))
					{
						return json;
					}
				}
				
				Map<String, Object> data = meService.login(request);
				if (data != null && data.size() > 0) {
					json.setData(data);
					json.setSuccess(true);
					json.setErrorCode("10000");
					json.setErrorMsg("�ɹ�");
				} else {
					String error_msg = "�û��������벻��";
					json.setErrorMsg(error_msg);
					json.setErrorCode("10001");
					json.setSuccess(false);
				}
			}
			else
			{
				return json;
			}
			
		} catch (NullPointerException e) {
			json.setErrorMsg("�û��������ڻ��������");
			json.setSuccess(false);
		} catch (Exception e) {
			if (e.getClass().getName()
					.equals("javax.crypto.IllegalBlockSizeException")
					|| e.getClass().getName()
							.equals("java.lang.IllegalArgumentException")) { //
				String error_msg = "��½��־����";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			} else {
				e.printStackTrace();
				String error_msg = "��½ʧ��";
				json.setErrorMsg(error_msg);
				json.setSuccess(false);
			}

		}
		return json;
	}
	
	/**
	 * ע�� 2.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/register")
	public @ResponseBody MapJson register(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String userName = request.getParameter("userName");
		final String userPassword = request.getParameter("userPassword");
//		final String userNick = request.getParameter("userNick");
		if (userName == null || userPassword == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> list = meService.register(request);
			if (list == null) {
				json.setErrorMsg("�û����ظ�");
				json.setErrorCode("10003");
				return json;
			} 
			Map<String, Object> data = new HashMap<String, Object>();
			String expireTime = Convert.dateToExpire();
			String src = list.get("userID") + "+" + expireTime;
			String enString = Aes.Encrypt(src, Constants.ENCODE_KEY);
			data.put("c", enString);
			data.put("userInfo", list);
			json.setData(data);
			json.setSuccess(true);
			json.setErrorCode("10000");
			json.setErrorMsg("�ɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * �һ����� 2.3
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/finepassword")
	public @ResponseBody MapJson finepassword(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String userName = request.getParameter("userName");
		final String userPassword = request.getParameter("userPassword");
		final String userPhone = request.getParameter("userPhone");
		if (userName == null || userPassword == null || userPhone == null) {
			json.setSuccess(false);
			return json;
		}
		try {

			boolean issuccess = meService.finepassword(request);
			
			if(issuccess == true)
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}
			else
			{
				json.setSuccess(false);
				json.setErrorCode("10004");
				json.setErrorMsg("���˺�δ���ֻ��Ż����ֻ��Ŵ���,����ϵ�ͷ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	
	/**
	 * ��ȡ�û����� 2.4
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getUserInfo")
	public @ResponseBody MapJson getUserInfo(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String userID = request.getParameter("userID");;
		if (userID == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> data = meService.getUserInfo(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * 2.5 �޸�����
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/updateUserInfo")
	public @ResponseBody MapJson updateUserInfo(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String c = request.getParameter("c");
		final String userNick = request.getParameter("userNick");
		final String userSex = request.getParameter("userSex");
		final String userSheng = request.getParameter("userSheng");
		final String userShi = request.getParameter("userShi");
		final String userQu = request.getParameter("userQu");
		if (c == null || userSheng== null || userShi== null || userQu==null || userNick == null || userSex == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			boolean issuccess = meService.updateUserInfo(request);
			
			if(issuccess == false)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * �ϴ�ͷ�� 2.6
	 * 
	 * @author liaodq
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/uploadPhoto")
	public @ResponseBody MapJson uploadPhoto(
			HttpServletRequest request, HttpSession session) {

		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		MultipartFile contentfile = ((MultipartHttpServletRequest) request)
				.getFile("file");
		if(c == null || contentfile.isEmpty())
		{
			return json;
		}
		
		try {
			Map<String, Object> data = meService.uploadPhoto(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}

		return json;
	}
	
	/**
	 * ��ȡͼ���б� 3.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getBookList")
	public @ResponseBody ListJson getBookList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");

		try {
			List<Map<String, Object>> data = meService.getBookList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡͼ������ 3.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getBookInformation")
	public @ResponseBody MapJson getBookInformation(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String bookID = request.getParameter("bookID");;
		if (bookID == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> data = meService.getBookInformation(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡͼ��������Դ 3.3
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getBookDownData")
	public @ResponseBody ListJson getBookDownData(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");

		final String bookID = request.getParameter("bookID");
		final String c = request.getParameter("c");
		final String udid = request.getParameter("udid");
		if (bookID == null || c == null || udid == null) {
			json.setSuccess(false);
			return json;
		}
		
		try {
			Map<String, Object> data = meService.getBookDownData(request);
			
			if(data.get("success").equals(false))
			{
				json.setSuccess(false);
				json.setErrorCode(data.get("code").toString());
				json.setErrorMsg(data.get("msg").toString());
			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("��ȡ�ɹ�");
				List<Map<String, Object>> list = (List<Map<String, Object>>)data.get("datas");
				json.setData(list);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * �������� 4.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/sendComment")
	public @ResponseBody MapJson sendComment(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String bookID = request.getParameter("bookID");
		final String c = request.getParameter("c");
		final String commentText = request.getParameter("commentText");
		final String commentPoint = request.getParameter("commentPoint");
		if (bookID == null || c == null || commentText == null || commentPoint == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			boolean success = meService.sendComment(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10005");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ�����б� 4.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getBookComments")
	public @ResponseBody ListJson getBookComments(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");

		final String bookID = request.getParameter("bookID");
		if (bookID == null) {
			json.setSuccess(false);
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getBookComments(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡiap�б� 5.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getiapPriceList")
	public @ResponseBody ListJson getiapPriceList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		try {
			List<Map<String, Object>> data = meService.getiapPriceList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ����ͼ�� 5.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/buyWithBook")
	public @ResponseBody MapJson buyWithBook(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		try {
			Map<String, Object> data = meService.buyWithBook(request);
			
			if(data.get("success").equals(false))
			{
				json.setSuccess(false);
				json.setErrorCode(data.get("code").toString());
				json.setErrorMsg(data.get("msg").toString());
			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("����ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ�ѹ����ͼ�� 5.3
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getBuyedBooks")
	public @ResponseBody ListJson getBuyedBooks(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");

		final String c = request.getParameter("c");
		if(c == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getBuyedBooks(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ��ֵ�����Ѽ�¼ 5.4
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getPurchaseLogs")
	public @ResponseBody ListJson getPurchaseLogs(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");

		final String c = request.getParameter("c");
		if(c == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getPurchaseLogs(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * �ӹ�ע 6.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/becomeFans")
	public @ResponseBody MapJson becomeFans(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String userID = request.getParameter("userID");
		if(c == null || userID == null)
		{
			return json;
		}
		
		try {
			Map<String, Object> data = meService.becomeFans(request);
			
			if(data.get("success").equals(false))
			{
				json.setSuccess(false);
				json.setErrorCode(data.get("code").toString());
				json.setErrorMsg(data.get("msg").toString());
			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("��ע�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ�û��б� 6.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getMemberList")
	public @ResponseBody ListJson getMemberList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		try {
			List<Map<String, Object>> data = meService.getMemberList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡ�ҹ�ע�����б� 6.3
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getFansList")
	public @ResponseBody ListJson getFansList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		if(c == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getFansList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��˵˵ 6.4
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/sendTalk")
	public @ResponseBody MapJson sendTalk(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String talkAllLook = request.getParameter("talkAllLook");
		if(c == null || talkAllLook == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.sendTalk(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10013");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * ɾ��˵˵ 6.5
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/deleteTalk")
	public @ResponseBody MapJson deleteTalk(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String talkID = request.getParameter("talkID");
		if(c == null || talkID == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.deleteTalk(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10014");
				json.setErrorMsg("ɾ��ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ����˵˵ 6.6
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/commentTalk")
	public @ResponseBody MapJson commentTalk(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String talkID = request.getParameter("talkID");
		final String text = request.getParameter("text");
		if(c == null || talkID == null || text == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.commentTalk(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10005");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ����˵˵ 6.7
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/zanTalk")
	public @ResponseBody MapJson zanTalk(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String talkID = request.getParameter("talkID");
		if(c == null || talkID == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.zanTalk(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10015");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ˵˵���� 6.8
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getTalkDetail")
	public @ResponseBody MapJson getTalkDetail(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String talkID = request.getParameter("talkID");
		if (talkID == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> data = meService.getTalkDetail(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  �鿴ĳ��˵˵������ 6.9
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getZansWithTalk")
	public @ResponseBody ListJson getZansWithTalk(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String talkID = request.getParameter("talkID");
		if(talkID == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getZansWithTalk(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  �鿴ĳ��˵˵�������� 6.10
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getCommentsWithTalk")
	public @ResponseBody ListJson getCommentsWithTalk(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String talkID = request.getParameter("talkID");
		if(talkID == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getCommentsWithTalk(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡ˵˵�б� 6.11
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getTalkList")
	public @ResponseBody ListJson getTalkList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String talkAllLook = request.getParameter("talkAllLook");
		if(talkAllLook == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getTalkList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡͼ�������б� 7.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getGroupList")
	public @ResponseBody ListJson getGroupList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		try {
			List<Map<String, Object>> data = meService.getGroupList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡ�ؼ��б� 7.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getAuthorList")
	public @ResponseBody ListJson getAuthorList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		try {
			List<Map<String, Object>> data = meService.getAuthorList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ�ؼ����� 7.3
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getAuthorDetail")
	public @ResponseBody MapJson getAuthorDetail(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String authorID = request.getParameter("authorID");
		if (authorID == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> data = meService.getAuthorDetail(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡ�����б� 7.4
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getNewsList")
	public @ResponseBody ListJson getNewsList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		try {
			List<Map<String, Object>> data = meService.getNewsList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ�������� 7.5
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getNewsDetail")
	public @ResponseBody MapJson getNewsDetail(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String newsID = request.getParameter("newsID");
		if (newsID == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> data = meService.getNewsDetail(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡ֪ͨ����(Banner) 7.6
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getBannerList")
	public @ResponseBody ListJson getBannerList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		try {
			List<Map<String, Object>> data = meService.getBannerList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  ��ȡʡ�����б� 7.7
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getCityList")
	public @ResponseBody ListJson getCityList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		try {
			List<Map<String, Object>> data = meService.getCityList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 *  �����б� 8.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getGoodsList")
	public @ResponseBody ListJson getGoodsList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		try {
			List<Map<String, Object>> data = meService.getGoodsList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * ��ȡ��Ʒ���� 8.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getGoodDetail")
	public @ResponseBody MapJson getGoodDetail(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		final String goodID = request.getParameter("goodID");
		if (goodID == null) {
			json.setSuccess(false);
			return json;
		}
		try {
			Map<String, Object> data = meService.getGoodDetail(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * �������� 8.3
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/sendGoods")
	public @ResponseBody MapJson sendGoods(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String goodName = request.getParameter("goodName");
		final String groupID = request.getParameter("groupID");
		final String goodPrice = request.getParameter("goodPrice");
		final String goodRemark = request.getParameter("goodRemark");
		if(c == null || goodName == null || groupID == null || goodPrice == null || goodRemark == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.sendGoods(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10013");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * ���� 8.4
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/goodPay")
	public @ResponseBody MapJson goodPay(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String goodID = request.getParameter("goodID");
		final String goodPayPrice = request.getParameter("goodPayPrice");
		if(c == null || goodID == null || goodPayPrice == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.goodPay(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10016");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 *  ��ȡ��Ʒ�ĳ����б� 8.5
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	@RequestMapping(value = "/getGoodPayList")
	public @ResponseBody ListJson getGoodPayList(HttpServletRequest request,
			HttpSession session) {
		ListJson json = new ListJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
				
		final String goodID = request.getParameter("goodID");
		if(goodID == null)
		{
			return json;
		}
		
		try {
			List<Map<String, Object>> data = meService.getGoodPayList(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		// }
		// }.start();
		return json;
	}
	
	/**
	 * �ɽ����� 8.6
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/finishGoodPay")
	public @ResponseBody MapJson finishGoodPay(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String goodID = request.getParameter("goodID");
		final String goodPayID = request.getParameter("goodPayID");
		if(c == null || goodID == null || goodPayID == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.finishGoodPay(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("10017");
				json.setErrorMsg("�ɽ�ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * ��Ʒ�ϼ�/�¼� 8.7
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/changeGoodStatus")
	public @ResponseBody MapJson changeGoodStatus(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		final String goodID = request.getParameter("goodID");
		final String status = request.getParameter("status");
		if(c == null || goodID == null || status == null)
		{
			return json;
		}
		
		try {
			boolean success = meService.changeGoodStatus(request);
			
			if(success == false)
			{
				json.setSuccess(false);
				json.setErrorCode("9997");
				json.setErrorMsg("����ʧ��");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * �ϴ���֤ 9.1
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/sendIdCar")
	public @ResponseBody MapJson sendIdCar(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		if(c == null)
		{
			return json;
		}
		
		try {
			Map<String, Object> data = meService.sendIdCar(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * ��ȡ��֤״̬ 9.2
	 * 
	 * @param attrMap
	 * @return
	 * @{return null;}
	 */
	
	@RequestMapping(value = "/getIdcarStatus")
	public @ResponseBody MapJson getIdcarStatus(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		
		final String c = request.getParameter("c");
		if(c == null)
		{
			return json;
		}
		
		try {
			Map<String, Object> data = meService.getIdcarStatus(request);
			
			if(data == null)
			{
				json.setSuccess(false);
				json.setErrorCode("9999");
				json.setErrorMsg("�ڲ�����");

			}
			else
			{
				json.setSuccess(true);
				json.setErrorCode("10000");
				json.setErrorMsg("�ɹ�");
				json.setData(data);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
	
	@RequestMapping(value = "/testpush")
	public @ResponseBody MapJson testpush(HttpServletRequest request,
			HttpSession session) {
		MapJson json = new MapJson();
		json.setErrorMsg("��������");
		json.setErrorCode("9998");
		

		try {
			//test.testPush();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setErrorMsg("�ڲ�����");
			json.setSuccess(false);
		}
		return json;
	}
}

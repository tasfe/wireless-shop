package com.ws.web.opens.weixin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.MySecurity;

import com.opensymphony.xwork2.Action;
import com.ws.web.util.struts.CustomAction;

public class EntryAction extends CustomAction{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String method = request.getMethod();
		if(method.toLowerCase().equals("get")){
			verify(request, response);
		}else{
			reply(request, response);
		}
		
		return Action.NONE;
	}
	
	/**
	 * 回复信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		InputStream is = request.getInputStream();
		OutputStream os = response.getOutputStream();
		DefaultSession session = DefaultSession.newInstance();
		try{
			String token = request.getParameter("token");
			session.addOnHandleMessageListener(new WeiXinRestaurantHandleMessageAdapter(session, token));
		}finally{
			session.process(is, os);
			session.close();
			if(is != null){
				is.close();
				is = null;
			}
			if(os != null){
				os.flush();
				os.close();
				os = null;
			}
		}
	}
	
	/**
	 * 验证
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void verify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Writer out = response.getWriter();
		String rid = request.getParameter("rid");
		String result = "";
		try{
			if(rid != null && !rid.trim().isEmpty()){
				String signature = request.getParameter("signature");	// 微信加密签名
				String timestamp = request.getParameter("timestamp");	// 时间戳
				String nonce = request.getParameter("nonce");			// 随机数
				String echostr = request.getParameter("echostr");		// 随机字符串
//				WeixinRestaurantDao.verify(account, signature, timestamp, nonce);
				
				List<String> list = new ArrayList<String>(3) {
					private static final long serialVersionUID = 1L;
					public String toString() {
						return this.get(0) + this.get(1) + this.get(2);
					}
				};
				list.add("q123123");
				list.add(timestamp);
				list.add(nonce);
				
				Collections.sort(list);
				
				String tmpStr = new MySecurity().encode(list.toString(), MySecurity.SHA_1);
				
				if (signature.equals(tmpStr)) {
					result = echostr;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result = "";
		}finally{
			out.write(result);
			out.flush();
			out.close();
		}
	}

}

package com.ws.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageAdapter;
import org.marker.weixin.MySecurity;
import org.marker.weixin.msg.Data4Item;
import org.marker.weixin.msg.Msg4ImageText;
import org.marker.weixin.msg.Msg4Text;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static final String WEIXIN_ADDR = "http://115.28.35.211/wr/phone/index.htm";
	static final String COMMAND_HELP = "h";
	static final String COMMAND_MENU = "m";
	
	/**
	 * 接受新TOKEN验证请求
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		System.out.println("rid:  " + request.getParameter("rid"));
		String signature = request.getParameter("signature");	// 微信加密签名
		String timestamp = request.getParameter("timestamp");	// 时间戳
		String nonce = request.getParameter("nonce");			// 随机数
		String echostr = request.getParameter("echostr");		// 随机字符串

		List<String> list = new ArrayList<String>(3) {
			private static final long serialVersionUID = 2621444383666420433L;
			public String toString() {
				return this.get(0) + this.get(1) + this.get(2);
			}
		};
		list.add("q123123");
		list.add(timestamp);
		list.add(nonce);
		
		Collections.sort(list);
		
		String tmpStr = new MySecurity().encode(list.toString(), MySecurity.SHA_1);
		Writer out = response.getWriter();
		if (signature.equals(tmpStr)) {
			out.write(echostr);
		} else {
			out.write("");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 处理微信信息
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		InputStream is = request.getInputStream();
		OutputStream os = response.getOutputStream();
		
		final DefaultSession session = DefaultSession.newInstance();
		final String rid = request.getParameter("rid");
		
		session.addOnHandleMessageListener(new HandleMessageAdapter(){
			@Override
			public void onTextMsg(Msg4Text msg) {
				Msg4ImageText mit = new Msg4ImageText();
				Data4Item dItem = null;
				Msg4Text mText = null;
				msg.setContent(msg.getContent().trim().toLowerCase());
				
				if(COMMAND_HELP.equals(msg.getContent())){
					mText =	new Msg4Text();
					mText.setFromUserName(msg.getToUserName());
					mText.setToUserName(msg.getFromUserName());
					mText.setFuncFlag("0");
					mText.setContent("餐厅编号:"+rid+"\nToUserName(openID):"+msg.getToUserName()+"\nFromUserName(开发者微信号):"+msg.getFromUserName());
					session.callback(mText);
//				}else if(COMMAND_MENU.equals(msg.getContent())){
				}else{
					mit.setFromUserName(msg.getToUserName());
					mit.setToUserName(msg.getFromUserName()); 
					mit.setCreateTime(msg.getCreateTime());
					
					dItem = new Data4Item("title", "头部", "http://115.28.35.211/title.jpg", WEIXIN_ADDR); 
					mit.addItem(dItem);
					
					dItem = new Data4Item();
					dItem.setTitle("微餐厅");
					dItem.setUrl(WEIXIN_ADDR);
					mit.addItem(dItem);
					
//					dItem = new Data4Item();
//					dItem.setTitle("百度");
//					dItem.setUrl("http://www.baidu.com");
//					mit.addItem(dItem);
//					
//					dItem = new Data4Item();
//					dItem.setTitle("google");
//					dItem.setUrl("http://www.google.com.hk");
//					mit.addItem(dItem);
					
					mit.setFuncFlag("0");  
					session.callback(mit);
//				}else{
//					Msg4Text rmsg =	new Msg4Text();
//					rmsg.setFromUserName(msg.getToUserName());
//					rmsg.setToUserName(msg.getFromUserName());
//					rmsg.setFuncFlag("0");
//					rmsg.setContent(new StringBuilder().append("未知命令\n")
//							.append("输入【h】获取帮助信息\n")
//							.append("输入【m】获得主菜单")
//							.toString());
//					session.callback(rmsg);
				}
			}
		});
		
		session.process(is, os);
		session.close();
	}

}

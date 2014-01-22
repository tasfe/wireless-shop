package com.ws.web.opens.weixin;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageAdapter;
import org.marker.weixin.msg.Data4Item;
import org.marker.weixin.msg.Msg4ImageText;
import org.marker.weixin.msg.Msg4Text;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.ws.util.DBHelper;

public class TestAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	static final String WEIXIN_ADDR = "http://115.28.35.211/wr/phone/index.htm";
	static final String COMMAND_HELP = "h";
	static final String COMMAND_MENU = "m";
	static final String COMMAND_TEST = "t";
	
	
	public String execute() throws Exception {
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
		
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
					
					mText.setContent("rid:"+rid+"\nToUserName(openID):"+msg.getToUserName()+"\nFromUserName(开发者微信号):"+msg.getFromUserName());
					session.callback(mText);

				}else if(COMMAND_TEST.equals(msg.getContent())){
					DBHelper a = null;
					mText = new Msg4Text();
					mText.setFromUserName(msg.getToUserName());
					mText.setToUserName(msg.getFromUserName());
					mText.setFuncFlag("0");
					mText.setContent("");
					try {
						a = DBHelper.newInstance();
						ResultSet res = a.getStmt().executeQuery("select * from restaurant;");
						while(res != null && res.next()){
//							System.out.println("restaurant_name: " + a.getRes().getString("restaurant_name"));
							mText.setContent(mText.getContent()+"\nname: "+res.getString("name"));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if(a != null){
							a.close();
						}
					}
					session.callback(mText);
					
//				}else if(COMMAND_MENU.equals(msg.getContent())){
				}else{
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
					dItem = new Data4Item();
					dItem.setTitle("google");
					dItem.setUrl("http://www.google.com.hk");
					mit.addItem(dItem);
					
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
		
		return ActionSupport.NONE;
	}
	
	@SuppressWarnings("unused")
	private void test(){
		DBHelper a = null;
		try {
			a = DBHelper.newInstance();
			ResultSet res = a.getStmt().executeQuery("select * from restaurant");
			while(res != null && res.next()){
				System.out.println("restaurant_name: " + res.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(a != null){
				a.close();
			}
		}
	}
	
/*		
	private String rid = "";
	private String signature = "";
	private String timestamp = "";
	private String nonce = "";
	private String echostr = "";
	
	public void setRid(String rid) {
		this.rid = rid;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}
	
	public String execute() throws Exception {
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);

//		System.out.println("rid:  " + rid);
//		System.out.println("signature:  " + signature);
//		System.out.println("timestamp:  " + timestamp);
//		System.out.println("nonce:  " + nonce);
//		System.out.println("echostr:  " + echostr);

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
		
//		System.out.println("echostr:  "+echostr);
//		System.out.println("signature:  "+signature);
//		System.out.println("tmpStr:  "+tmpStr);
		
		if (signature.equals(tmpStr)) {
			out.write(echostr);
		} else {
			out.write("");
		}
		out.flush();
		out.close();
		
		return ActionSupport.NONE;
	}
*/
	
}

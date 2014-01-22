package com.ws.web.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NewSMSUtil{
	public static String TIP_NEW_MEMBER = "您本次操作的验证码是：{code}，如果不您本人操作请联系客服!【{name}】";
	private static final boolean IS_OPEN = true; // 是否开启短信功能, 后期改为用户配置
	private static final String USER_ID = "4311";
	private static final String ACCOUNT = "fcr2013";
	private static final String PASSWORD = "123456";
	private static final String URL = "http://inter.ueswt.com/smsGBK.aspx?";
	private static final String ACTION = "send";
	private static int TIMEOUT = 5 * 1000;
	
	private NewSMSUtil(){}
	public static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	public enum Status{
		CLOSE_FUNCTION("closeFunction", "系统已关闭短信功能."),
		FAILD("Faild", "短信发送失败."),
		SUCCESS("Success", "短信发送成功.");
		
		Status(String status, String msg){
			this.status = status;
			this.msg = msg;
		}
		private String status;
		private String msg;
		
		public String getStatus(){
			return this.status;
		}
		public String getMsg(){
			return this.msg;
		}
		public boolean isClose(){
			return this == Status.CLOSE_FUNCTION;
		}
		public boolean isSuccess(){
			return this == Status.SUCCESS;
		}
		
		public String toString(){
			return "status: "+this.status+", msg:"+this.msg;
		}
		
		public static Status parse(String val) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException{
//			System.out.println(val);
			
//			System.out.println("val:  "+val);
//			String[] s = val.split("\\|");
//			for(Status v : values()){
//				if(s[0].trim().equals(v.getStatus())){
//					return v;
//				}
//			}
			
			Document document = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(val.getBytes("UTF-8")));
			NodeList list = document.getElementsByTagName("returnsms").item(0).getChildNodes();
			for(int i = 0; i < list.getLength(); i++){   
				if(list.item(i).getNodeName().equals("returnstatus")){
					if(list.item(i).getTextContent().trim().equals(NewSMSUtil.Status.SUCCESS.getStatus())){
						return NewSMSUtil.Status.SUCCESS;
					}else if(list.item(i).getTextContent().trim().equals(NewSMSUtil.Status.FAILD.getStatus())){
						return NewSMSUtil.Status.FAILD;
					}
				}
	        }
			throw new IllegalArgumentException("The taste reference type(val = " + val + ") is invalid.");
		}
		
	}
	
	/**
	 * 发送短信, 返回响应状态
	 * @param mobile
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static Status send(String mobile, String content) throws UnsupportedEncodingException, IOException, Exception {
		if(NewSMSUtil.IS_OPEN){
			HttpURLConnection httpconn = null;
			String result = "";
			// 短信内容单条短信最多70字
			String memo = content.length() < 70 ? content.trim() : content.trim().substring(0, 70);
			System.out.println("短信内容: " + memo);
			StringBuilder sb = new StringBuilder();
			sb.append(NewSMSUtil.URL)
			.append("userid=").append(NewSMSUtil.USER_ID)
			.append("&account=").append(NewSMSUtil.ACCOUNT)
			.append("&password=").append(NewSMSUtil.PASSWORD)
			.append("&mobile=").append(mobile)
			.append("&content=").append(URLEncoder.encode(memo, "gb2312"))
			.append("&action=").append(NewSMSUtil.ACTION);
			try {
				URL url = new URL(sb.toString());
				httpconn = (HttpURLConnection) url.openConnection();
				httpconn.setConnectTimeout(NewSMSUtil.TIMEOUT);
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream(), "gb2312"));
				while(rd.ready()){
					result += rd.readLine();
				}
				rd.close();
			} catch (MalformedURLException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} catch(Exception e) {
				throw e;
			} finally{
				if(httpconn != null){
					httpconn.disconnect();
					httpconn = null;
				}
			}
			return Status.parse(result);
		}else{
			return Status.CLOSE_FUNCTION;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static int createCode(){
		int code = 0;
		boolean s = true;
		while(s){
			code = (int)(Math.random() * 1000000);
			if(code > 100000 && code < 999999){
				s = false;
			}
		}
		return code;
	}
	
	public static void main(String[] args){
		try {
			for(int i=1; i<= 8; i++){
				Status temp = NewSMSUtil.send("15999955793", NewSMSUtil.TIP_NEW_MEMBER.replace("{code}", createCode() + "").replace("{name}", "智易科技"));
				if(!temp.isClose() && !temp.isSuccess()){
					System.out.println("警告: 新会员注册信息短信发送失败, 远程接口响应异常.");
				}
				System.out.println("响应结果:  " + temp.getMsg());
			}
		} catch (Exception e) {
			System.out.println("异常: 新会员注册信息短信发送失败, 程序运行异常.");
			e.printStackTrace();
		}
	}
	
}

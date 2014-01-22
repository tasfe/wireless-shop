package com.ws.web.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class Test {
	
	public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create";
	public static final String ACCESS_TOKEN = "j0gPizLEIQfhO-f9jem-aTek6rCaNAfPOMrXjS4ipdzc0NlPNlz_jSKpM13_bPJzwhN84dgMrD5fwzxejiHoDJYwg6LXXegT6tdX3HoStmUCrcSscQ6qyJA2fWTIj4nwqjQZF1U0Dww8GhcZu9wgGg";
	
	public static void main(String[] args){
		LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> button = new ArrayList<Map<String,Object>>();
		
		int random = (int)(Math.random() * 1000000);
		
		button.add(WeiXinMenu.viewBuilder("点菜", WeiXinRestaurantHandleMessageAdapter.WEIXIN_ORDER+"?_d="+random+"&rid=26").toJsonMap());
		button.add(WeiXinMenu.viewBuilder("促销信息", WeiXinRestaurantHandleMessageAdapter.WEIXIN_SALES+"?_d="+random+"&rid=26").toJsonMap());
		
		List<WeiXinMenu> subButton = new ArrayList<WeiXinMenu>();
		subButton.add(WeiXinMenu.clickBuilder("导航", "m"));
		subButton.add(WeiXinMenu.viewBuilder("餐厅简介", WeiXinRestaurantHandleMessageAdapter.WEIXIN_ABOUT+"?_d="+random+"&rid=26"));
		subButton.add(WeiXinMenu.viewBuilder("会员资料", WeiXinRestaurantHandleMessageAdapter.WEIXIN_MEMBER+"?_d="+random+"&rid=26"));
		subButton.add(WeiXinMenu.viewBuilder("智易科技", "http://www.baidu.com"));
		
		button.add(WeiXinMenu.subButtonBuilder("更多", subButton).toJsonMap());
		
		body.put("button", button);
		
		HttpURLConnection httpconn = null;
//		PrintWriter out = null;
		OutputStreamWriter out = null;
		StringBuilder sb = new StringBuilder();
		sb.append(MENU_CREATE_URL)
		  .append("?")
		  .append("access_token=").append(ACCESS_TOKEN);
		
		String line, result = "";
		try {
			URL url = new URL(sb.toString());
			httpconn = (HttpURLConnection) url.openConnection();
			httpconn.setRequestMethod("POST");
			httpconn.setConnectTimeout(5 * 1000);
			httpconn.setDoOutput(true);
			httpconn.setDoInput(true);
//			out = new PrintWriter(httpconn.getOutputStream());
			out = new OutputStreamWriter(httpconn.getOutputStream(), "UTF-8");  
			String postData = JSONObject.fromObject(body).toString();
			System.out.println("postData:  "+postData);
			out.write(postData);
			out.flush();
			out.close();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(httpconn.getInputStream(), "gb2312"));
			while((line = br.readLine()) != null){
				result += line;
			}
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			if(httpconn != null){
				httpconn.disconnect();
				httpconn = null;
			}
//			if(out != null){
//				out.flush();
//				out.close();
//				out = null;
//			}
			System.out.println("send result:  " + result);
		}
	}
	
}

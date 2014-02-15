package com.ws.web.init;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.alibaba.fastjson.JSONObject;
import com.ws.util.InitParamsUtil;
import com.ws.util.json.JObject;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static JSONObject config;
	public static final JSONObject getConfig() {
		return config;
	}
	
	public void init() throws ServletException {
		try {
			config = JObject.getConfig(getServletContext().getRealPath("/") + "/WEB-INF/classes/" + getServletConfig().getInitParameter("config"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		try{
			InitParamsUtil.initDBParams(config);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		try{
//			InitParamsUtil.initOSSParams(config);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServletException(e);
		}
		super.init();
	}
	
}

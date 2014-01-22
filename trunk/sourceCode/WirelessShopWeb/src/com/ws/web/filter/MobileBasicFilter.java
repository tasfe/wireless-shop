package com.ws.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ws.pojo.exception.SystemBE;
import com.ws.util.json.JObject;

public class MobileBasicFilter extends WRBasicFilter{
	
	public static final String REGION = "mobile";
	public static final String LOGIN_USER_SESSION_KEY = "mobile_login_user_session_key";
	
	private static String[] pwl = new String[0];
	private static String[] awl = new String[0];
	public static String[] getPageWhiteList() {
		return pwl;
	}
	public static String[] getActionWhiteList() {
		return awl;
	}
	
	@Override
	public void destroy() { }
	@Override
	public void init(FilterConfig conf) throws ServletException {
		super.init(conf);
		initActionWhiteList();
		initPageWhiteList();
		pwl = super.pageWhiteList;
		awl = super.actionWhiteList;
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String uri = request.getRequestURI();
		String target = uri.substring(uri.lastIndexOf('/') + 1);
		String type = target.substring(target.lastIndexOf(".") + 1);
		
		if(isJScript(type) || isPicture(type)){
			chain.doFilter(request, response);
		}else if(isPage(type)){
			response.setContentType("text/html; charset=utf-8");
			MobilePageFilter.doFilter(request, response, chain, target);
		}else if(isAction(type)){
			response.setContentType("text/plain; charset=utf-8");
			MobileActionFilter.doFilter(request, response, chain, target);
		}else{
			try{
				response.setContentType("text/plain; charset=utf-8");
				PrintWriter writer = response.getWriter();
				JObject jobj = new JObject(false, "Error", SystemBE.UNKNOWN_RESOURCES.getMessage(), SystemBE.UNKNOWN_RESOURCES.getCode());
				writer.print(jobj.toString());
				writer.flush();
				writer.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
}

package com.ws.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MobilePageFilter{
	private MobilePageFilter(){}
	
	public static void doFilter(ServletRequest req, ServletResponse res, FilterChain chain, String target) 
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		
		Object loginUser = session.getAttribute(MobileBasicFilter.LOGIN_USER_SESSION_KEY);
		
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-Control", "no-cache");
		response.setHeader("expires", "0");
		
		if(target.isEmpty() || FilterUtil.filterWhiteForTarget(MobileBasicFilter.getPageWhiteList(), target)){
			if(loginUser != null){
				response.sendRedirect(request.getContextPath().concat("/" + MobileBasicFilter.REGION + "/login.htm"));
			}else{
				chain.doFilter(request, response);
			}
		}else{
			if(loginUser == null){
				response.sendRedirect(request.getContextPath().concat("/" + MobileBasicFilter.REGION + "/login.htm"));
			}else{
				chain.doFilter(request, response);
			}
		}
	}
	
}

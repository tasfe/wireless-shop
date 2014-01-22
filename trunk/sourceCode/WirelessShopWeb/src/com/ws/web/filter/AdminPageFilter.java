package com.ws.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminPageFilter{
	private AdminPageFilter(){}
	
	public static void doFilter(ServletRequest req, ServletResponse res, FilterChain chain, String target) 
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		
		Object loginUser = session.getAttribute(AdminBasicFilter.LOGIN_USER_SESSION_KEY);
		
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-Control", "no-cache");
		response.setHeader("expires", "0");
		
		if(target.isEmpty() || FilterUtil.filterWhiteForTarget(AdminBasicFilter.getPageWhiteList(), target)){
			if(loginUser != null){
				response.sendRedirect(request.getContextPath().concat("/" + AdminBasicFilter.REGION + "/main.htm"));
			}else{
				chain.doFilter(request, response);
			}
		}else{
			if(loginUser == null){
				response.sendRedirect(request.getContextPath().concat("/" + AdminBasicFilter.REGION + "/index.htm"));
			}else{
				chain.doFilter(request, response);
			}
		}
	}
	
}

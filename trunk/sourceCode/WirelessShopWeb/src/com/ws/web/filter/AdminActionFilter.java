package com.ws.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ws.pojo.exception.SystemBE;
import com.ws.util.json.JObject;

public class AdminActionFilter{
	private AdminActionFilter() { }
	
	public static void doFilter(ServletRequest req, ServletResponse res, FilterChain chain, String target)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		
//		System.out.println("getRequestURI: "+request.getRequestURI());
//		System.out.println("getContextPath: "+request.getContextPath());
//		System.out.println("getRequestURL: "+ request.getRequestURL().toString());
//		System.out.println("referer: "+ request.getHeader("referer"));
		if(FilterUtil.filterWhiteForTarget(AdminBasicFilter.getActionWhiteList(), target)){
			chain.doFilter(request, response);
		}else{
			Object loginUser = session.getAttribute(AdminBasicFilter.LOGIN_USER_SESSION_KEY);
			JObject jobj;
//			System.out.println("loginUser:  "+loginUser);
			if(loginUser == null){
				jobj = new JObject();
				jobj.initTip(false, "Error", SystemBE.LOGIN_TIMEOUT.getMessage(), SystemBE.LOGIN_TIMEOUT.getCode());
				response.getWriter().print(jobj.toString());
				jobj = null;
				return;
			}else{
				chain.doFilter(request, response);
			}
		}
	}

}

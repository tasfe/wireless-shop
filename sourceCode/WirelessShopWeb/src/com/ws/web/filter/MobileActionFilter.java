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

public class MobileActionFilter{
	private MobileActionFilter() { }
	
	public static void doFilter(ServletRequest req, ServletResponse res, FilterChain chain, String target)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		
		if(FilterUtil.filterWhiteForTarget(MobileBasicFilter.getActionWhiteList(), target)){
			chain.doFilter(request, response);
		}else{
			Object loginUser = session.getAttribute(MobileBasicFilter.LOGIN_USER_SESSION_KEY);
			JObject jobj;
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

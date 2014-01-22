package com.ws.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicFilter implements Filter {

	public void destroy() { }
	
	public void init(FilterConfig fConfig) throws ServletException { }
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		System.out.println("basic filter........................");
//		System.out.println("getRequestURI: "+request.getRequestURI());
//		System.out.println("getContextPath: "+request.getContextPath());
//		System.out.println("getRequestURL: "+ request.getRequestURL().toString());
//		System.out.println("referer: "+ request.getHeader("referer"));
//		
//		String referer = request.getHeader("referer");
//        
//		if(referer == null){
//			
//		}else{
//			
//		}
		
		chain.doFilter(request, response);
	}

	

}

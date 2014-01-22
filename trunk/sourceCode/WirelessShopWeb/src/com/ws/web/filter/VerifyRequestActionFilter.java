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


public class VerifyRequestActionFilter extends WRBasicFilter {
	
	@Override
	public void init(FilterConfig conf) throws ServletException {
		super.init(conf);
		initResourceWhiteList();
	}
	
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String uri = request.getRequestURI();
		String path = request.getContextPath();
		String region = uri.substring(path.length() + 1);
		region = region.indexOf("/") != -1 ? region.substring(0, region.indexOf("/")) : region;
		
//		System.out.println("uri: "+uri);
//		System.out.println("path: "+path);
//		System.out.println("region: "+region);
//		System.out.println("region: "+region);
		
		if(FilterUtil.filterWhiteForTarget(super.resourceWhiteList, region)){
			chain.doFilter(request, response);
		}else{
			try{
				response.setContentType("text/plain; charset=utf-8");
				PrintWriter writer = response.getWriter();
				JObject jobj = new JObject(false, "Error", SystemBE.NONE_PRIVILEGE.getMessage(), SystemBE.NONE_PRIVILEGE.getCode());
				writer.print(jobj.toString());
				writer.flush();
				writer.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}

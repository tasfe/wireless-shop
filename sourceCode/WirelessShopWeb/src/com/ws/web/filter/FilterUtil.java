package com.ws.web.filter;

import javax.servlet.http.HttpServletRequest;

public class FilterUtil {
	
	/**
	 * 解释 web.xml 自定义白名单集
	 * @param wl
	 * @return
	 */
	public static synchronized String[] explainWhiteList(String wl){
		String[] whiteList = new String[0];
		if(wl != null){
			whiteList = wl.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "").trim().split(",");
		}
		for(int i = 0; i < whiteList.length; i++){
			whiteList[i] = whiteList[i].trim();
		}
		return whiteList;
	}
	
	public static boolean filterWhiteForURI(String[] wl, HttpServletRequest request){
		return filterWhiteForTarget(wl, request.getRequestURI().substring(request.getContextPath().length() + 1));
	}
	
	/**
	 * 验证白名单
	 * @param wl
	 * @param target
	 * @return
	 */
	public static boolean filterWhiteForTarget(String[] wl, String target){
		boolean isWhite = false;
		for(String temp : wl){
			if(target.equals(temp)){
				isWhite = true;
				break;
			}
		}
		return isWhite;
	}
	
}

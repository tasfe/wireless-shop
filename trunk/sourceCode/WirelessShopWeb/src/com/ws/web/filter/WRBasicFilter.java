package com.ws.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class WRBasicFilter implements Filter {
	protected FilterConfig config;
	protected String[] resourceWhiteList = new String[0];
	protected String[] pageWhiteList = new String[0];
	protected String[] actionWhiteList = new String[0];
	
	public static final String[] TYPE_PAGE = new String[]{"htm", "html", "jsp"};
	public static final String[] TYPE_ACTION = new String[]{"action", "do"};
	public static final String[] TYPE_JSCRIPT = new String[]{"js"};
	public static final String[] TYPE_PICTURE = new String[]{"jpg", "gif", "png", "bmp"};
	
	@Override
	public void init(FilterConfig conf) throws ServletException {
		config = conf;
	}
	
	protected FilterConfig getConfig() {
		return config;
	}
	protected void initResourceWhiteList() {
		resourceWhiteList = FilterUtil.explainWhiteList(config.getInitParameter("ResourceWhiteList"));
	}
	protected void initPageWhiteList() {
		pageWhiteList = FilterUtil.explainWhiteList(config.getInitParameter("PageWhiteList"));
	}
	protected void initActionWhiteList() {
		actionWhiteList = FilterUtil.explainWhiteList(config.getInitParameter("ActionWhiteList"));
	}
	
	/**
	 * 验证请求资源类型
	 * @param type
	 * @param resource
	 * @return
	 */
	static boolean checkType(String[] type, String resource){
		boolean check = false;
		if(resource == null){
			throw new NullPointerException("对比类型不能为空.");
		}
		for(String temp : type){
			if(resource.toLowerCase().trim().equals(temp)){
				check = true;
				break;
			}
		}
		return check;
	}
	/**
	 * 请求页面
	 * @param page
	 * @return
	 */
	public static boolean isPage(String page){
		return checkType(TYPE_PAGE, page);
	}
	/**
	 * 请求数据
	 * @param action
	 * @return
	 */
	public static boolean isAction(String action){
		return checkType(TYPE_ACTION, action);
	}
	/**
	 * 请求脚本
	 * @param action
	 * @return
	 */
	public static boolean isJScript(String action){
		return checkType(TYPE_JSCRIPT, action);
	}
	/**
	 * 请求页面
	 * @param action
	 * @return
	 */
	public static boolean isPicture(String action){
		return checkType(TYPE_PICTURE, action);
	}
	
}

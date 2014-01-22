package com.ws.pojo.exception;

/**
 * 
 * @author WuZY
 *	code range : 9999 - 9999
 */
public final class SystemBE {
	private SystemBE(){};
	
//	public static final BusinessException LOGIN_TIMEOUT = BusinessException.define(9998, "failure, user login timeout.");
	public static final BusinessException LOGIN_TIMEOUT = BusinessException.define(9998, "操作失败, 登录超时.");
	public static final BusinessException NONE_PRIVILEGE = BusinessException.define(9997, "操作失败, 请求的资源没有权限.");
	public static final BusinessException UNKNOWN_RESOURCES = BusinessException.define(9996, "操作失败, 请求的资源没有找到.");
	
}

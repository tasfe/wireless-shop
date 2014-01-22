package com.ws.pojo.exception;

/**
 * 
 * @author WuZY
 *	code range : 8950 - 8999
 */
public final class DeptBE {
	private DeptBE(){};
	
	public static final BusinessException NO_FIND = BusinessException.define(8999, "操作失败, 该部门资料不存在或已删除.");
	
	public static final BusinessException ADD_FAILURE = BusinessException.define(8989, "操作失败, 添加新部门资料失败, 请联系管理员.");
	public static final BusinessException ADD_FAILURE_HAS = BusinessException.define(8988, "操作失败, 该部门已存在.");
	
	
}

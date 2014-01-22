package com.ws.pojo.exception;

/**
 * 
 * @author Administrator
 *	code range : 8899 - 8849
 */
public final class FoodBE {
	private FoodBE(){}
	
	public static final BusinessException NO_FIND = BusinessException.define(8899, "操作失败, 该菜品资料不存在或已删除.");
	
	public static final BusinessException ADD_FAILURE = BusinessException.define(8898, "操作失败, 添加菜品资料失败, 请检查数据格式.");
	public static final BusinessException ADD_FAILURE_HAS = BusinessException.define(8897, "操作失败, 该编号菜品资料已存在.");
	
	public static final BusinessException UPDATE_FAILURE = BusinessException.define(8888, "操作失败, 修改菜品资料失败, 请检查数据格式.");
	
}

package com.ws.pojo.exception;

/**
 * 
 * @author WuZY
 *	code range : 8900 - 8949
 */
public final class KitchenBE {
	private KitchenBE(){};
	
	public static final BusinessException NO_FIND = BusinessException.define(8949, "操作失败, 该分厨资料不存在或已删除.");
	
	public static final BusinessException ADD_FAILURE = BusinessException.define(8948, "操作失败, 添加分厨资料失败, 请联系管理员.");
	public static final BusinessException ADD_FAILURE_HAS = BusinessException.define(8947, "操作失败, 该分厨已存在.");
	
}

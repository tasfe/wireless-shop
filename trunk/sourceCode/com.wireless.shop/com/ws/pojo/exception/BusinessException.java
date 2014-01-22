package com.ws.pojo.exception;

/**
 * 
 * @author WuZY
 * @desc
 * Code Range to each type as below.
 * 	dept: 		8999 - 8950
 * 	kitchen:	8900 - 8949
 * 	food:		8899 - 8849
 */
public final class BusinessException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public BusinessException(String msg){
		this(9999, msg);
	}
	public BusinessException(int code, String msg){
		super(msg);
		this.code = code;
	}
	
	private int code;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public static BusinessException define(int code, String msg){
		return new BusinessException(code, msg);
	}
	
}

package com.ws.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.ws.util.DateUtil;

public final class LogUtil {
	private LogUtil(){}
	private static final String LINE_SEPARATOR = "\r\n";
	
	static void write(String log, boolean flag){
		try {
			File lf = new File("ELOG_" + DateUtil.formatToDate(new Date()) + ".log");
			if(lf.exists()){
				lf.createNewFile();
			}
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(lf, flag));
	    	writer.write(log);
	    	writer.flush();
	    	writer.close();
		} catch (IOException e) {
			System.out.println("日志写入错误, 当前时间: " + DateUtil.now());
			e.printStackTrace();
		}
	}
	
	synchronized static void write(String log){
		write(log, false);
	}
	public synchronized static void append(Exception e){
		write(e.getMessage(), true);
	}
	public synchronized static void append(String log){
		write(DateUtil.now() + LINE_SEPARATOR + log + LINE_SEPARATOR, true);
	}
	public synchronized static void clear(){
		write("", true);
	}
	
	public static void main(String[] args){
		for(int i = 0; i < 3; i++){
			append("写入测试, 当前时间:  " + DateUtil.now());
		}
	}

}

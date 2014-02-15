package com.ws.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.ws.util.oss.OSSImageUtil;
import com.ws.util.oss.OSSParams;
import com.ws.util.oss.OSSUtil;

public class InitParamsUtil {
	
	/**
	 * 初始化DB连接池参数
	 * @param config
	 */
	public static void initDBParams(JSONObject config){
		if(config == null || config.size() == 0){
			throw new NullPointerException("初始化DB连接池失败, 参数列表为空.");
		}
		DBHelper.init(config.getString(DBHelper.DB_HOST_KEY),
				config.getString(DBHelper.DB_PORT_KEY), 
				config.getString(DBHelper.DB_NAME_KEY),
				config.getString(DBHelper.DB_USER_KEY), 
				config.getString(DBHelper.DB_PWD_KEY)
		);
	}
	
	/**
	 * 初始化OSS服务参数
	 * @param config
	 */
	public static void initOSSParams(JSONObject config){
		if(config == null || config.size() == 0){
			throw new NullPointerException("初始化OSS服务失败, 参数列表为空.");
		}
		OSSUtil.init(OSSParams.init(config.getString(OSSParams.OSS_ACCESS_ID_KEY), 
				config.getString(OSSParams.OSS_ACCESS_KEY_KEY), 
				config.getString(OSSParams.OSS_INNER_POINT_KEY), 
				config.getString(OSSParams.OSS_OUTER_POINT_KEY)));
		OSSImageUtil.init(config.getString(OSSImageUtil.BUCKET_IMAGE_KEY));
		OSSImageUtil.setDefaultFile(config.getString(OSSImageUtil.BUCKET_IMAGE_DEFAULT_KEY));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("信息: 图片上传服务开启成功. ");
	}
	
}

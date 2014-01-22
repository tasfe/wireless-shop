package com.ws.socket;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ws.dao.food.IFoodDao;
import com.ws.dao.food.impl.FoodDao;
import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.food.Food;
import com.ws.socket.javaSocket.PrintServerMainHandler;
import com.ws.util.DBHelper;
import com.ws.util.InitParamsUtil;
import com.ws.util.json.JObject;

import net.sf.json.JSONObject;

public class RunServer {
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		JSONObject config = null;
		try {
			// 读取配置文件
			if(args == null || args.length == 0){
				// 读取默认配置文件
				config = JObject.getConfig(RunServer.class.getClassLoader().getResourceAsStream("com/wr/config/config.json"));
			}else if(args[0].indexOf("/") >= 0){
				// 读取相对路径配置文件
				config = JObject.getConfig(RunServer.class.getClassLoader().getResourceAsStream(args[0]));
			}else{
				// 读取自定义配置文件
				config = JObject.getConfig(args[0]);
			}
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("信息: 服务器基础配置读取成功. ");
		} catch (Exception e) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("信息: 服务器基础配置读取失败. ");
			e.printStackTrace();
			throw e;
		}
		try{
			// 初始化数据库连接池信息
			InitParamsUtil.initDBParams(config);
			// TODO
			//updatePrice();
		}catch(Exception e){
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("信息: 数据库连接池信息初始化失败. ");
			e.printStackTrace();
			throw e;
		}
		final Object JAVA_SOCKET_PORT = config.get("JAVA_SOCKET_PORT"), JAVA_SOCKET_POOL_SIZE = config.get("JAVA_SOCKET_POOL_SIZE");
		new Thread(new Runnable() {
			public void run() {
				try {
					// 开启打印服务
					new PrintServerMainHandler(Integer.valueOf(JAVA_SOCKET_PORT.toString()), Integer.valueOf(JAVA_SOCKET_POOL_SIZE.toString())).start();
				} catch (IOException e) {
					System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					System.out.println("信息: 打印服务开启失败. ");
					e.printStackTrace();
				}
			}
		}).start();
		
		try{
			// 初始化图片上传服务
			InitParamsUtil.initOSSParams(config);
		}catch(Exception e){
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("信息: 图片上传服务开启失败. ");
			e.printStackTrace();
		}
	}
	
	
	static void updatePrice() throws SQLException, BusinessException{
		IFoodDao foodDao = new FoodDao();
		List<Food> list = foodDao.getByExtra(" AND F.r_id = 26 ");
		DBHelper conn = DBHelper.newInstance();
		int count = 0;
		for(Food temp : list){
			temp.setPrice((float) (Math.random() * 1000));
			foodDao.update(conn, temp);
			System.out.println("food alias: " + temp.getAlias());
			count++;
		}
		System.out.println("count: "+count);
	}
	
}

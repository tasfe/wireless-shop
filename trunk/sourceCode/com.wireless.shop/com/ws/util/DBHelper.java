package com.ws.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper {
	
	private DBHelper(){}
	
	private Connection conn = null;
	private Statement stmt = null;
	public Connection getConn() {
		return conn;
	}
	public Statement getStmt() {
		return stmt;
	}
	
	public static final String DB_HOST_KEY = "DB_HOST";
	public static final String DB_PORT_KEY = "DB_PORT";
	public static final String DB_NAME_KEY = "DB_NAME";
	public static final String DB_USER_KEY = "DB_USER";
	public static final String DB_PWD_KEY = "DB_PWD";
	
	
	public static String dbUrl;
	public static String dbHost;
	public static String dbPort;
	public static String dbName;
	public static String dbUser;
	public static String dbPwd;
	public static final String dbDriver = "com.mysql.jdbc.Driver";
	
	/**
	 * 创建连接地址
	 * @param dbHost
	 * @param dbPort
	 * @param dbName
	 * @return
	 */
	private static String createUrl(String dbHost, String dbPort, String dbName){
		return "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useUnicode=true&characterEncoding=utf8";
	}
	
	/**
	 * 初始化默认连接池
	 * @param dbHost
	 * @param dbPort
	 * @param dbName
	 * @param user
	 * @param pwd
	 */
	public static void init(String dbHost, String dbPort, String dbName, String dbUser, String dbPwd){
		DBHelper.dbHost = dbHost;
		DBHelper.dbPort = dbPort;
		DBHelper.dbName = dbName;
		DBHelper.dbUser = dbUser;
		DBHelper.dbPwd = dbPwd;
		DBHelper.dbUrl = DBHelper.createUrl(dbHost, dbPort, dbName);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("信息: 初始化数据库信息成功. ");
	}
	
	/**
	 * 获取默认连接池
	 * @return
	 * @throws SQLException 
	 */
	public static DBHelper newInstance() throws SQLException{
		return new DBHelper().builder(DBHelper.dbUrl, DBHelper.dbUser, DBHelper.dbPwd);
	}
	
	/**
	 * 自定义连接池
	 * @param dbHost
	 * @param dbPort
	 * @param dbName
	 * @param user
	 * @param pwd
	 * @return
	 * @throws SQLException 
	 */
	public static DBHelper define(String dbHost, String dbPort, String dbName, String dbUser, String dbPwd) throws SQLException{
		return new DBHelper().builder(DBHelper.createUrl(dbHost, dbPort, dbName), dbUser, dbPwd);
	}
	
	/**
	 * 生产自定义连接池
	 * @param dbUrl
	 * @param dbUser
	 * @param dbPwd
	 * @return
	 * @throws SQLException
	 */
	public DBHelper builder(String dbUrl, String dbUser, String dbPwd) throws SQLException{
		try{
			Class.forName(DBHelper.dbDriver);
		}catch(ClassNotFoundException e){
			throw new SQLException("错误: 数据库连接池驱动加载失败.");
		}
		
		this.conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		if(this.conn == null || this.conn.isClosed()){
			throw new SQLException("错误: 数据库连接失败.");
		}
		this.stmt = this.conn.createStatement();
		this.stmt.execute("SET NAMES utf8");
		this.stmt.execute("USE " + DBHelper.dbName);
		
		return this;
	}
	
	public int getLastInsertId() throws SQLException{
		ResultSet res = null;
		try{
			res = this.stmt.executeQuery("SELECT LAST_INSERT_ID()");
			if(res != null && res.next())
				return res.getInt(1);
			else
				throw new SQLException("获取新插入数据编号失败.");
		}finally{
			safeClose(res);
		}
	}
	
	public void close(){
		try {
			if(this.stmt != null){
				this.stmt.close();
				this.stmt = null;
			}
			if(this.conn != null){
				this.conn.close();
				this.conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void commit(){
		try {
			if(this.conn != null) this.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void rollback(){
		try {
			if(this.conn != null) this.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void safeClose(DBHelper closable){
		try{
			if(closable != null){
				closable.close();
				closable = null;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void safeClose(Statement closable){
		try{
			if(closable != null){
				closable.close();
				closable = null;
			}			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void safeClose(ResultSet closable){
		try {
			if(closable != null){
				closable.close();
				closable = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

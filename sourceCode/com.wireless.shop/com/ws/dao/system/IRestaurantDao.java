package com.ws.dao.system;

import java.sql.SQLException;
import java.util.List;

import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;

public interface IRestaurantDao {
	public static final String DB_T_NAME = DBHelper.dbName + ".ws_restaurant"; 
	
	public List<Restaurant> getRestaurant(DBHelper conn, String extra) throws SQLException;
	public List<Restaurant> getRestaurant(String extra) throws SQLException;
	
	public List<Restaurant> getAll(DBHelper conn) throws SQLException;
	public List<Restaurant> getAll() throws SQLException;
	
	public Restaurant getById(DBHelper conn, int id) throws SQLException;
	public Restaurant getById(int id) throws SQLException;
	
}

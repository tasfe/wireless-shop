package com.ws.dao.system.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.dao.system.IRestaurantDao;
import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;

public class RestaurantDao implements IRestaurantDao{

	@Override
	public List<Restaurant> getRestaurant(DBHelper conn, String extra)
			throws SQLException {
		List<Restaurant> list = new ArrayList<Restaurant>();
		Restaurant item = null;
		String querySQL = "SELECT R.id, R.name, R.birth_date, R.expire_date, R.info, R.tele, R.address"
				+ " FROM " + IRestaurantDao.DB_T_NAME + " R WHERE 1=1 ";
		if(extra != null)
			querySQL += extra;
		ResultSet res = conn.getStmt().executeQuery(querySQL);
		while(res != null && res.next()){
			item = new Restaurant(res.getInt("id"), 
					res.getString("name"), 
					res.getTimestamp("birth_date").getTime(), 
					res.getTimestamp("expire_date").getTime(),
					res.getString("info"),
					res.getString("tele"),
					res.getString("address")
			);
			list.add(item);
		}
		
		res.close();
		res = null;
		item = null;
		return list;
	}

	@Override
	public List<Restaurant> getRestaurant(String extra) throws SQLException {
		DBHelper conn = DBHelper.newInstance();
		try{
			return this.getRestaurant(conn, extra);
		} finally {
			conn.close();
		}
	}

	@Override
	public List<Restaurant> getAll(DBHelper conn) throws SQLException {
		return this.getRestaurant(conn, null);
	}

	@Override
	public List<Restaurant> getAll() throws SQLException {
		return this.getRestaurant(null);
	}

	@Override
	public Restaurant getById(DBHelper conn, int id) throws SQLException {
		String extra = " AND R.id = " + id;
		List<Restaurant> list = this.getRestaurant(conn, extra);
		if(!list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public Restaurant getById(int id) throws SQLException {
		DBHelper conn = DBHelper.newInstance();
		try{
			return this.getById(conn, id);
		} finally {
			conn.close();
		}
	}

}

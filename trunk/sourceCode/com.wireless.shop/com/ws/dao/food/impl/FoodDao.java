package com.ws.dao.food.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.dao.dept.IKitchenDao;
import com.ws.dao.food.IFoodDao;
import com.ws.dao.system.IRestaurantDao;
import com.ws.pojo.dept.Kitchen;
import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.exception.FoodBE;
import com.ws.pojo.food.Food;
import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;

public class FoodDao extends OBeanBasic implements IFoodDao{
	
	public int getByExtraCount(DBHelper conn, String extra) throws SQLException{
		String querySQL = "SELECT COUNT(F.id)"
				+ " FROM " + DB_T_NAME + " F, " + IRestaurantDao.DB_T_NAME + " R, " + IKitchenDao.DB_T_NAME + " K"
				+ " WHERE F.r_id = R.id AND F.kitchen_id = K.id";
		if(extra != null) querySQL += extra;
		res = conn.getStmt().executeQuery(querySQL);
		if(res != null && res.next())
			return res.getInt(1);
		else
			return 0;
	}
	
	public List<Food> getByExtra(DBHelper conn, String extra)
			throws SQLException {
		List<Food> list = new ArrayList<Food>();
		Food item = null;
		String querySQL = "SELECT F.id, F.alias, F.name food_name, F.price, F.status, F.desc, F.img,"
				+ " F.kitchen_id, K.name kitchen_name,"
				+ " F.r_id, R.name r_name"
				+ " FROM " + DB_T_NAME + " F, " + IRestaurantDao.DB_T_NAME + " R, " + IKitchenDao.DB_T_NAME + " K"
				+ " WHERE F.r_id = R.id AND F.kitchen_id = K.id";
		if(extra != null) querySQL += extra;
		res = conn.getStmt().executeQuery(querySQL);
		while(res != null && res.next()){
			item = new Food(res.getInt("id"),
				res.getInt("r_id"),
				res.getInt("alias"),
				res.getString("food_name"),
				res.getFloat("price"),
				res.getInt("kitchen_id"),
				res.getInt("status"),
				res.getString("img"),
				res.getString("desc")
			);
			item.setKitchen(new Kitchen(res.getInt("kitchen_id"), res.getString("kitchen_name")));
			item.setRestaurant(new Restaurant(res.getInt("r_id"), res.getString("r_name")));
			list.add(item);
		}
		
		DBHelper.safeClose(res);
		item = null;
		return list;
	}
	
	public List<Food> getByExtra(String extra) throws SQLException {
		try{
			$conn = DBHelper.newInstance();
			return this.getByExtra($conn, extra);
		}finally{
			safeClose();
		}
	}

	
	public Food insert(DBHelper conn, Food insert) throws SQLException,
			BusinessException {
		// TODO 检查 alias
		
		String insertSQL = "INSERT INTO " + DB_T_NAME + " (r_id, alias, name, price, status, kitchen_id, `desc`, img) "
				+ " VALUES("
				+ "" + insert.getRid()
				+ "," + insert.getAlias()
				+ ",'" + insert.getName() + "'"
				+ "," + insert.getPrice()
				+ "," + insert.getStatus()
				+ "," + insert.getKitchenId()
				+ "," + (insert.getDesc() != null && !insert.getDesc().isEmpty() ? "'" + insert.getDesc() + "'" : "null")
				+ ",null"
				+ ")";
		if(conn.getStmt().executeUpdate(insertSQL) > 0){
			insert.setId(conn.getLastInsertId());
			return insert;
		}else{
			throw FoodBE.ADD_FAILURE;
		}
	}
	
	public Food insert(Food insert) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			return this.insert($conn, insert);
		} finally {
			safeClose();
		}
	}
	
	public Food update(DBHelper conn, Food update) throws SQLException,
			BusinessException {
		// TODO 检查 alias
		
		String updateSQL = "UPDATE " + DB_T_NAME + " SET "
				+ "alias=" + update.getAlias()
				+ ",name='" + update.getName() + "'"
				+ ",price=" + update.getPrice()
				+ ",status=" + update.getStatus()
				+ ",kitchen_id=" + update.getKitchenId()
				+ ",`desc`=" + (update.getDesc() != null && !update.getDesc().isEmpty() ? "'" + update.getDesc() + "'" : "null")
				+ " WHERE id = " + update.getId() + " AND r_id = " + update.getRid();
		if(conn.getStmt().executeUpdate(updateSQL) > 0){
			return update;
		}else{
			throw FoodBE.ADD_FAILURE;
		}
	}
	
	public Food update(Food update) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			return this.update($conn, update);
		} finally {
			safeClose();
		}
	}
	
	public Food getById(DBHelper conn, int foodId) throws SQLException,
			BusinessException {
		List<Food> list = this.getByExtra(conn, " AND F.id = " + foodId);
		if(list != null && !list.isEmpty())
			return list.get(0);
		else
			throw FoodBE.NO_FIND;
	}
	
	public Food getById(int foodId) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			return this.getById($conn, foodId);
		} finally {
			safeClose();
		}
	}

	public void updateImage(DBHelper conn, Food update) throws SQLException,
			BusinessException {
		String updateSQL = "UPDATE " + DB_T_NAME + " SET "
				+ " img = " + (update.getImg() == null ? null : "'" + update.getImg() + "'")
				+ " WHERE id = " + update.getId();
		if(conn.getStmt().executeUpdate(updateSQL) == 0){
			throw FoodBE.NO_FIND;
		}
	}
	
	public void updateImage(Food update) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			updateImage($conn, update);
		} finally {
			safeClose();
		}
	}
}

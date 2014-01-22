package com.ws.dao.food;

import java.sql.SQLException;
import java.util.List;

import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.food.Food;
import com.ws.util.DBHelper;

public interface IFoodDao{
	public static final String DB_T_NAME = DBHelper.dbName + ".wr_food";
	
	public int getByExtraCount(DBHelper conn, String extra) throws SQLException;
	public List<Food> getByExtra(DBHelper conn, String extra) throws SQLException;
	public List<Food> getByExtra(String extra) throws SQLException;
	
	public Food getById(DBHelper conn, int foodId) throws SQLException, BusinessException;
	public Food getById(int foodId) throws SQLException, BusinessException;
	
	public Food insert(DBHelper conn, Food insert) throws SQLException, BusinessException;
	public Food insert(Food insert) throws SQLException, BusinessException;
	
	public Food update(DBHelper conn, Food update) throws SQLException, BusinessException;
	public Food update(Food update) throws SQLException, BusinessException;
	
	public void updateImage(DBHelper conn, Food update) throws SQLException, BusinessException;
	public void updateImage(Food update) throws SQLException, BusinessException;
	
}

package com.ws.dao.dept;

import java.sql.SQLException;
import java.util.List;

import com.ws.pojo.dept.Kitchen;
import com.ws.pojo.exception.BusinessException;
import com.ws.util.DBHelper;

public interface IKitchenDao {
	public static final String DB_T_NAME = DBHelper.dbName + ".wr_kitchen"; 
	
	public List<Kitchen> getByExtra(DBHelper conn, String extra) throws SQLException;
	public List<Kitchen> getByExtra(String extra) throws SQLException;
	
	public List<Kitchen> getAll(DBHelper conn) throws SQLException;
	public List<Kitchen> getAll() throws SQLException;
	
	public List<Kitchen> getByDeptId(DBHelper conn, int deptId) throws SQLException;
	public List<Kitchen> getByDeptId(int deptId) throws SQLException;
	
	public Kitchen getById(DBHelper conn, int id) throws SQLException, BusinessException;
	public Kitchen getById(int id) throws SQLException, BusinessException;
	
	public void insert(DBHelper conn, Kitchen insert) throws SQLException, BusinessException;
	public void insert(Kitchen insert) throws SQLException, BusinessException;
	
	public void update(DBHelper conn, Kitchen update) throws SQLException, BusinessException;
	public void update(Kitchen update) throws SQLException, BusinessException;
	
	public void delete(DBHelper conn, Kitchen delete) throws SQLException, BusinessException;
	public void delete(Kitchen delete) throws SQLException, BusinessException;
	
}

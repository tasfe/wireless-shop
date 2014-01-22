package com.ws.dao.dept;

import java.sql.SQLException;
import java.util.List;

import com.ws.pojo.dept.Department;
import com.ws.pojo.exception.BusinessException;
import com.ws.util.DBHelper;

public interface IDeptDao {
	public static final String DB_T_NAME = DBHelper.dbName + ".wr_department";
	
	public List<Department> getByExtra(DBHelper conn, String extra) throws SQLException;
	public List<Department> getByExtra(String extra) throws SQLException;
	
	public List<Department> getAll(DBHelper conn) throws SQLException;
	public List<Department> getAll() throws SQLException;
	
	public Department getById(DBHelper conn, int id) throws SQLException, BusinessException;
	public Department getById(int id) throws SQLException, BusinessException;
	
	public void insert(DBHelper conn, Department insert) throws SQLException, BusinessException;
	public void insert(Department insert) throws SQLException, BusinessException;
	
	public void update(DBHelper conn, Department update) throws SQLException, BusinessException;
	public void update(Department update) throws SQLException, BusinessException;
	
	public void delete(DBHelper conn, Department delete) throws SQLException, BusinessException;
	public void delete(Department delete) throws SQLException, BusinessException;
	
}

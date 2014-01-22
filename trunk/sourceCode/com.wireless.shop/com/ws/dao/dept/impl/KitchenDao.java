package com.ws.dao.dept.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.dao.dept.IDeptDao;
import com.ws.dao.dept.IKitchenDao;
import com.ws.dao.system.IRestaurantDao;
import com.ws.pojo.dept.Department;
import com.ws.pojo.dept.Kitchen;
import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.exception.KitchenBE;
import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;

public class KitchenDao extends OBeanBasic implements IKitchenDao{
	
	/**
	 * 
	 */
	public List<Kitchen> getByExtra(DBHelper conn, String extra)
			throws SQLException {
		List<Kitchen> list = new ArrayList<Kitchen>();
		Kitchen item = null;
		String querySQL = "SELECT K.id, K.name kitchen_name,  "
				+ " K.r_id r_id, R.name r_name, "
				+ " K.dept_id dept_id, D.name dept_name "
				+ " FROM " + IKitchenDao.DB_T_NAME + " K, " + IRestaurantDao.DB_T_NAME + " R, " + IDeptDao.DB_T_NAME + " D"
				+ " WHERE K.r_id=R.id AND K.dept_id=D.id ";
		if(extra != null) querySQL += extra;
		res = conn.getStmt().executeQuery(querySQL);
		while(res != null && res.next()){
			item = new Kitchen(res.getInt("id"),
				res.getString("kitchen_name"),
				res.getInt("r_id"),
				res.getInt("dept_id")
			);
			item.setDept(new Department(res.getInt("dept_id"), 
				res.getString("dept_name"),
				res.getInt("r_id")
			));
			item.setRestaurant(new Restaurant(res.getInt("r_id"), res.getString("r_name")));
			list.add(item);
		}
		DBHelper.safeClose(res);
		item = null;
		return list;
	}

	/**
	 * 
	 */
	public List<Kitchen> getByExtra(String extra) throws SQLException {
		try{
			$conn = DBHelper.newInstance();
			return this.getByExtra($conn, extra);
		} finally {
			safeClose();
		}
	}
	
	/**
	 * 
	 */
	public List<Kitchen> getAll(DBHelper conn) throws SQLException {
		return this.getByExtra(conn, null);
	}
	public List<Kitchen> getAll() throws SQLException {
		return this.getByExtra(null);
	}
	
	private void check(DBHelper conn, Kitchen check) throws SQLException, BusinessException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 */
	public void insert(DBHelper conn, Kitchen insert) throws SQLException, BusinessException {
		//
		this.check(conn, insert);
		//
		String insertSQL = "INSERT INTO " + DB_T_NAME
				+ " (name, dept_id, r_id)"
				+ " VALUES('" + insert.getName() + "'," + insert.getDeptId() + "," + insert.getRid() + ")";
		if(conn.getStmt().executeUpdate(insertSQL) < 1){
			throw KitchenBE.ADD_FAILURE;
		}
	}

	/**
	 * 
	 */
	public void insert(Kitchen insert) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			this.insert($conn, insert);
		} finally {
			safeClose();
		}
	}

	/**
	 * 
	 */
	public void update(DBHelper conn, Kitchen update) throws SQLException,
			BusinessException {
		//
		this.check(conn, update);
		//
		String updateSQL = "UPDATE " + DB_T_NAME + " SET"
				+ " name = '" + update.getName() + "'"
				+ " ,dept_id = " + update.getDeptId()
				+ " WHERE id = " + update.getId();
		if(conn.getStmt().executeUpdate(updateSQL) < 1){
			throw KitchenBE.NO_FIND;
		}
	}

	/**
	 * 
	 */
	public void update(Kitchen update) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			this.update($conn, update);
		} finally {
			safeClose();
		}
	}
	
	/**
	 * 
	 */
	public void delete(DBHelper conn, Kitchen delete) throws SQLException,
			BusinessException {
		// TODO Auto-generated method stub
	}
	public void delete(Kitchen delete) throws SQLException, BusinessException {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 
	 */
	public List<Kitchen> getByDeptId(DBHelper conn, int deptId)
			throws SQLException {
		return getByExtra(conn, " AND K.dept_id = " + deptId);
	}
	public List<Kitchen> getByDeptId(int deptId) throws SQLException {
		try{
			$conn = DBHelper.newInstance();
			return getByDeptId($conn, deptId);
		} finally {
			safeClose();
		}
	}

	/**
	 * 
	 */
	public Kitchen getById(DBHelper conn, int id) throws SQLException, BusinessException {
		List<Kitchen> list = getByExtra(conn, " AND K.id = " + id); 
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}else{
			throw KitchenBE.NO_FIND;
		}
	}
	public Kitchen getById(int id) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			return getById($conn, id);
		} finally {
			safeClose();
		}
	}

}

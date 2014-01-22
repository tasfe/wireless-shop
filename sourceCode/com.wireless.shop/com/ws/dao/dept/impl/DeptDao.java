package com.ws.dao.dept.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.dao.dept.IDeptDao;
import com.ws.dao.system.IRestaurantDao;
import com.ws.dao.system.impl.RestaurantDao;
import com.ws.pojo.dept.Department;
import com.ws.pojo.exception.BusinessException;
import com.ws.pojo.exception.DeptBE;
import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;

public class DeptDao extends OBeanBasic implements IDeptDao{
	
	private IRestaurantDao restaurantDao = new RestaurantDao();
	
	public List<Department> getByExtra(DBHelper conn, String extra)
			throws SQLException {
		List<Department> list = new ArrayList<Department>();
		Department item = null;
		String querySQL = "SELECT D.id, D.name dept_name, "
				+ " D.r_id r_id, R.name r_name "
				+ " FROM " + IDeptDao.DB_T_NAME + " D, " + IRestaurantDao.DB_T_NAME + " R "
				+ " WHERE D.r_id=R.id ";
		if(extra != null) querySQL += extra;
		res = conn.getStmt().executeQuery(querySQL);
		while(res != null && res.next()){
			item = new Department(res.getInt("id"),
				res.getString("dept_name"),
				new Restaurant(res.getInt("r_id"), 
					res.getString("r_name")
				)
			);
			list.add(item);
		}
		
		DBHelper.safeClose(res);
		item = null;
		return list;
	}
	
	public List<Department> getByExtra(String extra) throws SQLException {
		try{
			$conn = DBHelper.newInstance();
			return this.getByExtra($conn, extra);
		} finally {
			safeClose();
		}
	}
	
	public List<Department> getAll(DBHelper conn) throws SQLException {
		return this.getByExtra(conn, null);
	}
	
	public List<Department> getAll() throws SQLException {
		return this.getByExtra(null);
	}
	
	/**
	 * 
	 */
	public Department getById(DBHelper conn, int id) throws SQLException, BusinessException {
		String extra = " AND D.id = " + id;
		List<Department> list = this.getByExtra(conn, extra);
		if(!list.isEmpty()){
			list.get(0).setRestaurant(restaurantDao.getById(conn, list.get(0).getRid()));
			return list.get(0);
		}else{
			throw DeptBE.NO_FIND;
		}
	}

	/**
	 * 
	 */
	public Department getById(int id) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			return this.getById($conn, id);
		} finally {
			safeClose();
		}
	}
	
	/**
	 * 验证操作数据
	 * @param conn
	 * @param check
	 * @throws SQLException
	 * @throws BusinessException
	 */
	private void check(DBHelper conn, Department check) throws SQLException,
			BusinessException{
		String querySQL =  "SELECT count(*) FROM " + DB_T_NAME 
				+ " WHERE r_id = " + check.getRid()
				+ " AND name = '" + check.getName() + "'"
				+ (check.getId() > 0 ? " AND id <> " + check.getId() : "");
		res = conn.getStmt().executeQuery(querySQL);
		if(res != null && res.next() && res.getInt(1) > 0){
			throw DeptBE.ADD_FAILURE_HAS;
		}
		DBHelper.safeClose(res);
	}
	
	/**
	 * 
	 */
	public void insert(DBHelper conn, Department insert) throws SQLException,
			BusinessException {
		this.check(conn, insert);
		//
		String querySQL = "INSERT INTO " + DB_T_NAME + " (name, r_id) "
				+ " VALUES('" + insert.getName() + "', " + insert.getRid() + ")";
		if(conn.getStmt().executeUpdate(querySQL) == 0){
			throw DeptBE.ADD_FAILURE;
		}
	}
	
	/**
	 * 
	 */
	public void insert(Department insert) throws SQLException, BusinessException {
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
	public void update(DBHelper conn, Department update) throws SQLException,
			BusinessException {
		this.check(conn, update);
		//
		String querySQL = "UPDATE " + DB_T_NAME + " SET "
				+ " name = '" + update.getName() + "'"
				+ " WHERE id = " + update.getId();
		if(conn.getStmt().executeUpdate(querySQL) == 0){
			throw DeptBE.NO_FIND;
		}
	}

	/**
	 * 
	 */
	public void update(Department update) throws SQLException, BusinessException {
		try{
			$conn = DBHelper.newInstance();
			this.update($conn, update);
		} finally {
			safeClose();
		}
	}

	@Override
	public void delete(DBHelper conn, Department delete) throws SQLException,
			BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Department delete) throws SQLException,
			BusinessException {
		// TODO Auto-generated method stub
		
	}

}

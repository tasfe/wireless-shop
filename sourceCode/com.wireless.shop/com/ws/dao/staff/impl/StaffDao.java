package com.ws.dao.staff.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.dao.staff.IStaffDao;
import com.ws.dao.system.IRestaurantDao;
import com.ws.pojo.staff.Staff;
import com.ws.pojo.system.Restaurant;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;

public class StaffDao extends OBeanBasic implements IStaffDao{

	public int getByExtraCount(DBHelper conn, String extra) throws SQLException {
		String querySQL = "SELECT COUNT(S.id)" 
				+ " FROM " + IStaffDao.DB_T_NAME + " S, " + IRestaurantDao.DB_T_NAME + " R"
				+ " WHERE S.r_id = R.id ";
		if(extra != null) querySQL += extra;
		res = conn.getStmt().executeQuery(querySQL);
		if(res != null && res.next())
			return res.getInt(1);
		else
			return 0;
	}
	
	public List<Staff> getByExtra(DBHelper conn, String extra)
			throws SQLException {
		List<Staff> list = new ArrayList<Staff>();
		Staff item = null;
		String querySQL = "SELECT S.id, S.name, S.pwd, "
				+ " R.id r_id, R.name r_name"
				+ " FROM " + IStaffDao.DB_T_NAME + " S, " + IRestaurantDao.DB_T_NAME + " R"
				+ " WHERE S.r_id = R.id ";
		if(extra != null) querySQL += extra;
		res = conn.getStmt().executeQuery(querySQL);
		while(res != null && res.next()){
			item = new Staff(
				res.getInt("id"),
				res.getInt("r_id"),
				res.getString("name"),
				res.getString("pwd")
			);
			item.setRestaurant(new Restaurant(res.getInt("r_id"), res.getString("r_name")));
			list.add(item);
		}
		
		DBHelper.safeClose(res);
		item = null;
		return list;
	}
	
	public List<Staff> getByExtra(String extra) throws SQLException {
		try{
			$conn = DBHelper.newInstance();
			return this.getByExtra($conn, extra);
		}finally{
			safeClose();
		}
	}

}

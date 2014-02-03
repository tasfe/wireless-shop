package com.ws.dao.staff;

import java.sql.SQLException;
import java.util.List;

import com.ws.pojo.staff.Staff;
import com.ws.util.DBHelper;

public interface IStaffDao {
	public static final String DB_T_NAME = DBHelper.dbName + ".ws_staff";
	
	public int getByExtraCount(DBHelper conn, String extra) throws SQLException;
	public List<Staff> getByExtra(DBHelper conn, String extra) throws SQLException;
	public List<Staff> getByExtra(String extra) throws SQLException;
	
}

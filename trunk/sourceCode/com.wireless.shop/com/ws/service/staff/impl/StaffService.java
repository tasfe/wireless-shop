package com.ws.service.staff.impl;

import java.sql.SQLException;

import com.ws.dao.staff.IStaffDao;
import com.ws.dao.staff.impl.StaffDao;
import com.ws.pojo.staff.Staff;
import com.ws.service.staff.IStaffService;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;
import com.ws.util.json.JObject;

public class StaffService extends OBeanBasic implements IStaffService{
	
	private IStaffDao staffDao = new StaffDao();
	
	/**
	 * 登陆
	 */
	public JObject adminLogin(Staff staff) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			StringBuilder extra = new StringBuilder();
			extra.append(" AND R.id = " + staff.getRid())
				.append(" AND S.name = '" + staff.getName() + "'")
				.append(" AND S.pwd = '" + staff.getPwd() + "'");
			if(staffDao.getByExtraCount($conn, extra.toString()) > 0){
				jobj.initTip(true, "登陆成功.");
				jobj.getOther().put("staff", staffDao.getByExtra($conn, extra.toString()).get(0));
			}else{
				jobj.initTip(false, "登陆失败, 用户名或密码错误.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		}finally{
			safeClose();
			staff = null;
		}
		return jobj;
	}

}

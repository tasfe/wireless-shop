package com.ws.service.dept.impl;

import java.sql.SQLException;
import java.util.List;

import com.ws.dao.dept.IDeptDao;
import com.ws.dao.dept.IKitchenDao;
import com.ws.dao.dept.impl.DeptDao;
import com.ws.dao.dept.impl.KitchenDao;
import com.ws.dao.system.IRestaurantDao;
import com.ws.dao.system.impl.RestaurantDao;
import com.ws.pojo.dept.Department;
import com.ws.pojo.exception.BusinessException;
import com.ws.service.dept.IDeptService;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;
import com.ws.util.json.JObject;

public class DeptService extends OBeanBasic implements IDeptService{
	
	private IRestaurantDao restaurantDao = new RestaurantDao();
	private IKitchenDao kitchenDao = new KitchenDao();
	private IDeptDao deptDao = new DeptDao();

	public JObject getAll(Integer start, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}
	public JObject getAll(Integer start, Integer limit, boolean includeKitchen) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 */
	public JObject getByRestaurantId(Integer start, Integer limit, int rid, boolean includeKitchen) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			String extra = " AND D.r_id = " + rid;
			List<Department> list = deptDao.getByExtra($conn, extra);
			if(!list.isEmpty()){
				if(includeKitchen){
					// TODO
					for(Department temp : list){
						temp.setKitchens(kitchenDao.getByDeptId(temp.getId()));
					}
				}
				jobj.setTotalProperty(list.size());
				jobj.setRoot(list);
				jobj.getOther().put("restaurant", restaurantDao.getById($conn, rid));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (Exception e) {
			e.printStackTrace();
			jobj.initTip(e);
		} finally {
			safeClose();
		}
		return jobj;
	}
	public JObject getByRestaurantId(Integer start, Integer limit, int rid) {
		return getByRestaurantId(start, limit, rid, false);
	}
	
	/**
	 * 
	 */
	public JObject insert(Department dept) {
		try{
			jobj = new JObject();
			deptDao.insert(dept);
			jobj.initTip(true, "操作成功, 已添加新部门信息.");
		} catch (BusinessException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (Exception e) {
			e.printStackTrace();
			jobj.initTip(e);
		}
		return jobj;
	}
	
	/**
	 * 
	 */
	public JObject update(Department update) {
		try{
			jobj = new JObject();
			deptDao.update(update);
			jobj.initTip(true, "操作成功, 已修改部门信息.");
		} catch (BusinessException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (Exception e) {
			e.printStackTrace();
			jobj.initTip(e);
		}
		return jobj;
	}

	/**
	 * 
	 */
	public JObject delete(int id) {
		try{
			jobj = new JObject();
			deptDao.delete(new Department(id, null, 0));
			jobj.initTip(true, "操作成功, 已删除部门信息.");
		} catch (BusinessException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (SQLException e) {
			e.printStackTrace();
			jobj.initTip(e);
		} catch (Exception e) {
			e.printStackTrace();
			jobj.initTip(e);
		}
		return jobj;
	}

}

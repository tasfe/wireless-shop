package com.ws.service.dept.impl;

import java.sql.SQLException;
import java.util.List;

import com.ws.dao.dept.IDeptDao;
import com.ws.dao.dept.IKitchenDao;
import com.ws.dao.dept.impl.DeptDao;
import com.ws.dao.dept.impl.KitchenDao;
import com.ws.dao.system.IRestaurantDao;
import com.ws.dao.system.impl.RestaurantDao;
import com.ws.pojo.dept.Kitchen;
import com.ws.pojo.exception.BusinessException;
import com.ws.service.dept.IKitchenService;
import com.ws.util.json.JObject;
import com.ws.util.DBHelper;
import com.ws.util.OBeanBasic;

public class KitchenService extends OBeanBasic implements IKitchenService{
	
	private IRestaurantDao restaurantDao = new RestaurantDao();
	private IDeptDao deptDao = new DeptDao();
	private IKitchenDao kitchenDao = new KitchenDao();
	
	@Override
	public JObject getAll(Integer start, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public JObject getByExtra(Integer start, Integer limit, int rid,
			int deptId, String deptName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public JObject getByRestaurantId(Integer start, Integer limit, int rid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 */
	public JObject getByDeptId(Integer start, Integer limit, int rid, int deptId) {
		try{
			jobj = new JObject();
			$conn = DBHelper.newInstance();
			String extra = " AND K.r_id = " + rid;
			if(deptId > 0){
				extra += (" AND K.dept_id = " + deptId);
			}
			extra += (" ORDER BY K.r_id ");
			List<Kitchen> list = kitchenDao.getByExtra($conn, extra);
 			if(list != null && !list.isEmpty()){
 				jobj.setRoot(list);
 				jobj.getOther().put("restaurant", restaurantDao.getById($conn, rid));
 				if(deptId > 0){
 					jobj.getOther().put("dept", deptDao.getById($conn, list.get(0).getDeptId()));
 				}
 			}
		} catch (BusinessException e) {
			e.printStackTrace();
			jobj.initTip(e);
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
	
	/**
	 * 
	 */
	public JObject insert(Kitchen insert) {
		try{
			jobj = new JObject();
			kitchenDao.insert(insert);
			jobj.initTip("操作成功, 已添加新分厨资料.");
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
	public JObject update(Kitchen update) {
		try{
			jobj = new JObject();
			kitchenDao.update(update);
			jobj.initTip("操作成功, 已修改新分厨资料.");
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
			kitchenDao.delete(new Kitchen(id, null));
			jobj.initTip("操作成功, 已删除新分厨资料.");
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

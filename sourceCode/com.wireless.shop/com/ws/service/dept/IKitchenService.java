package com.ws.service.dept;

import com.ws.pojo.dept.Kitchen;
import com.ws.util.json.JObject;

public interface IKitchenService {
	
	public JObject getAll(Integer start, Integer limit);
	
	public JObject getByExtra(Integer start, Integer limit, int rid, int deptId, String deptName);
	
	public JObject getByRestaurantId(Integer start, Integer limit, int rid);
	
	public JObject getByDeptId(Integer start, Integer limit, int rid, int deptId);
	
	public JObject insert(Kitchen insert);
	
	public JObject update(Kitchen update);
	
	public JObject delete(int id);
	
}

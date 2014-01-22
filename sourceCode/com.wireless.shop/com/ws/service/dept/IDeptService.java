package com.ws.service.dept;

import com.ws.pojo.dept.Department;
import com.ws.util.json.JObject;

public interface IDeptService {
	
	public JObject getAll(Integer start, Integer limit, boolean includeKitchen);
	public JObject getAll(Integer start, Integer limit);
	
	public JObject getByRestaurantId(Integer start, Integer limit, int rid, boolean includeKitchen);
	public JObject getByRestaurantId(Integer start, Integer limit, int rid);
	
	public JObject insert(Department insert);
	
	public JObject update(Department update);
	
	public JObject delete(int id);
	
}

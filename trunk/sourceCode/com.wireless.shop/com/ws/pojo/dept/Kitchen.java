package com.ws.pojo.dept;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ws.pojo.system.Restaurant;
import com.ws.util.json.Jsonable;

public class Kitchen implements Jsonable{
	private int id;					// 分厨编号
	private String name;			// 分厨名称
	private int deptId;				// 归属部门编号
	private int rid;				// 归属餐厅编号
	private Department dept;		// 归属部门
	private Restaurant restaurant;	// 归属餐厅
	
	public void init(int id, String name, int rid, int deptId){
		this.id = id;
		this.name = name;
		this.rid = rid;
		this.deptId = deptId;
	}
	public Kitchen(){}
	public Kitchen(int id, String name){
		this.init(id, name, 0, 0);
	}	
	public Kitchen(int id, String name, int rid, int deptId){
		this.init(id, name, rid, deptId);
	}
	public Kitchen(int id, String name, Department dept, Restaurant restaurant){
		this(id, name);
		if(dept != null){
			this.setDept(dept);
			this.setDeptId(dept.getId());
		}
		if(restaurant != null){
			this.setRestaurant(restaurant);
			this.setRid(restaurant.getId());
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	@Override
	public String toString() {
		return "then bean for Kitchen: id->" + this.id 
			+ ", name->" + this.name
			+ ", dept->" + this.dept != null ? this.dept.toString() : null
			+ ", restaurant->" + this.restaurant != null ? this.restaurant.toString() : null;
	}
	@Override
	public Map<String, Object> toJsonMap(int flag) {
		Map<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("id", this.id);
		jm.put("name", this.name);
		jm.put("deptId", this.getDeptId());
		jm.put("rid", this.getRid());
		if(flag > 0){
			if(flag == 1){
				
			}else if(flag == 2){
				if(this.dept != null){
					jm.put("dept", this.dept.toJsonMap(1));
				}
			}else if(flag == 3){
				jm.put("restaurant", this.restaurant.toJsonMap(1));
			}
		}else{
			if(this.dept != null) 
				jm.put("dept", this.dept.toJsonMap(1));
			if(this.restaurant != null)
				jm.put("restaurant", this.restaurant.toJsonMap(1));
		}
		return jm;
	}
	
}

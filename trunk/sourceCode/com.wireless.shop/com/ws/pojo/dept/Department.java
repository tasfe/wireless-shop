package com.ws.pojo.dept;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ws.pojo.system.Restaurant;
import com.ws.util.json.Jsonable;

public class Department implements Jsonable{
	private int id;					// 部门编号
	private String name;			// 部门名称
	private int rid;				// 归属餐厅编号
	private Restaurant restaurant;	// 归属餐厅
	private List<Kitchen> kitchens;
	
	public void init(int id, String name, int rid){
		this.id = id;
		this.name = name;
		this.rid = rid;
	}
	public Department(){}
	public Department(int id, String name, int rid){
		this.init(id, name, rid);
	}
	public Department(int id, String name, Restaurant restaurant){
		this.init(id, name, restaurant.getId());
		this.restaurant = restaurant;
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
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public List<Kitchen> getKitchens() {
		return kitchens;
	}
	public void setKitchens(List<Kitchen> kitchens) {
		this.kitchens = kitchens;
	}
	
	@Override
	public String toString() {
		return "then bean for Department: id->" + this.id + ", name->" + this.name;
	}
	
	/**
	 * 
	 */
	public Map<String, Object> toJsonMap(int flag) {
		Map<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("id", this.id);
		jm.put("name", this.name);
		jm.put("rid", this.getRid());
		
		List<Map<String, Object>> kml;
		if(flag > 0){
			if(flag == 1){
				
			}else if(flag == 2){
				if(this.restaurant != null)
					jm.put("restaurant", this.restaurant.toJsonMap(1));
			}else if(flag == 3){
				if(this.kitchens != null && !this.kitchens.isEmpty()){
					kml = new ArrayList<Map<String,Object>>();
					for(Kitchen temp : kitchens){
						kml.add(temp.toJsonMap(1));
					}
					jm.put("kitchens", kml);
				}
			}
		}else{
			if(this.restaurant != null)
				jm.put("restaurant", this.restaurant.toJsonMap(1));
			if(this.kitchens != null && !this.kitchens.isEmpty()){
				kml = new ArrayList<Map<String,Object>>();
				for(Kitchen temp : kitchens){
					kml.add(temp.toJsonMap(1));
				}
				jm.put("kitchens", kml);
			}
		}
		return jm;
	}
	
	
}

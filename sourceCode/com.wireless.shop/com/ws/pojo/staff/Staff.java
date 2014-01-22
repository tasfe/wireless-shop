package com.ws.pojo.staff;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ws.pojo.system.Restaurant;
import com.ws.util.json.Jsonable;

public class Staff implements Jsonable{
	
	private int id;
	private int rid;
	private String name;
	private String pwd;
	
	private Restaurant restaurant;
	
	public Staff init(int id, int rid, String name, String pwd){
		this.id = id;
		this.rid = rid;
		this.name = name;
		this.pwd = pwd;
		return this;
	}
	
	public Staff(){}
	public Staff(int id, int rid, String name){
		init(id, rid, name, null);
	}
	public Staff(int id, int rid, String name, String pwd){
		init(id, rid, name, pwd);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
	public String toString() {
		return "then bean for staff: id->" + id
				+ " ,rid:" + rid
				+ " ,name:" + name;
	}
	
	public Map<String, Object> toJsonMap(int flag) {
		Map<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("id", id);
		jm.put("rid", rid);
		jm.put("name", name);
		
		if(flag > 0){
			jm.put("pwd", pwd);
		}else{
			if(restaurant != null)
				jm.put("restaurant", restaurant.toJsonMap(1));
		}
		return jm;
	}

	
}

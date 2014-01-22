package com.ws.pojo.system;

public class PrintServerAccount {
	private int rid;
	private int id;
	private String name;
	private String pwd;
	private long brithDate;
	private Restaurant restaurant;
	
	public void init(int rid, int id, String name, String pwd, long brithDate){
		this.rid = rid;
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.brithDate = brithDate;
		this.id = (int) (Math.random() * 1000000);
	}
	public PrintServerAccount(){
		this.id = (int) (Math.random() * 1000000);
	}
	public PrintServerAccount(int rid, int id, String name, String pwd, long brithDate){
		init(rid, id, name, pwd, brithDate);
	}
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
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
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public long getBrithDate() {
		return brithDate;
	}
	public void setBrithDate(long brithDate) {
		this.brithDate = brithDate;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	
}

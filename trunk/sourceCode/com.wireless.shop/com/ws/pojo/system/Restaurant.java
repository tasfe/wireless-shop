package com.ws.pojo.system;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ws.util.DateUtil;
import com.ws.util.json.Jsonable;

public class Restaurant implements Jsonable{
	private int id;				// 餐厅编号
	private String name;		// 餐厅名称
	private long birthDate;		// 生成时间
	private long expireDate;	// 过期时间
	private String info;		// 公告
	private String tele;		// 电话
	private String address;		// 地址
	
	public Restaurant(){}

	public void init(int id, String name, long birthDate, long expireDate, 
			String info, String tele, String address){
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.expireDate = expireDate;
		this.info = info;
		this.tele = tele;
		this.address = address;
	}
	public Restaurant(int id, String name){
		this.init(id, name, 0, 0, "", "", "");
	}
	public Restaurant(int id, String name, long birthDate, long expireDate){
		this.init(id, name, birthDate, expireDate, "", "", "");
	}
	public Restaurant(int id, String name, long birthDate, long expireDate, 
			String info, String tele, String address){
		this.init(id, name, birthDate, expireDate, info, tele, address);
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
	public String getBirthDateFormat() {
		return DateUtil.format(birthDate);
	}
	public long getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}
	public String getExpireDateFormat() {
		return DateUtil.format(expireDate);
	}
	public long getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(long expireDate) {
		this.expireDate = expireDate;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "then bean for Restaurant: id->" + this.id + ", name->" + this.name;
	}

	@Override
	public Map<String, Object> toJsonMap(int flag) {
		Map<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("id", this.id);
		jm.put("name", this.name);
		if(flag > 0){
			
		}else{
			jm.put("birthDate", this.birthDate);
			jm.put("birthDateFormat", this.birthDate > 0 ? this.getBirthDateFormat() : "");
			jm.put("expireDate", this.expireDate);
			jm.put("expireDateFormat", this.expireDate > 0 ? this.getExpireDateFormat() : "");
			jm.put("info", this.info);
			jm.put("tele", this.tele);
			jm.put("address", this.address);
		}
//		return Collections.unmodifiableMap(jm);
		return jm;
	}
	
}

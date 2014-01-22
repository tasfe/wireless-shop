package com.ws.pojo.food;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ws.pojo.dept.Kitchen;
import com.ws.pojo.system.Restaurant;
import com.ws.util.PinyinUtil;
import com.ws.util.json.Jsonable;

public class Food implements Jsonable{
	
	public static final short STATUS_SPECIAL = 1 << 0;			/* 特价 */
	public static final short STATUS_RECOMMEND = 1 << 1;		/* 推荐 */ 
	public static final short STATUS_STOP = 1 << 2;				/* 停售 */
	public static final short STATUS_GIFT = 1 << 3;				/* 赠送 */
	public static final short STATUS_COMBO = 1 << 4;			/* 套菜 */
	
	private int id;
	private int rid;
	private int alias;
	private String name;
	private float price;
	private int kitchenId;
	private int status;
	private String img;
	private String desc;
	private Restaurant restaurant;
	private Kitchen kitchen;
	
	public void init(int id, int rid, int alias, String name, float price, int kitchenId, int status, String img, String desc){
		this.id = id;
		this.rid = rid;
		this.alias = alias;
		this.name = name;
		this.price = price;
		this.kitchenId = kitchenId;
		this.status = status;
		this.img = img;
		this.desc = desc;
	}
		
	public Food(){}
	public Food(int id, int rid, int alias, String name, float price, int kitchenId, int status, String img, String desc){
		this.init(id, rid, alias, name, price, kitchenId, status, img, desc);
	}
	public Food(int id, int rid, int alias, String name, float price, int kitchenId, int status){
		this.init(id, rid, alias, name, price, kitchenId, status, null, null);
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
	public int getAlias() {
		return alias;
	}
	public void setAlias(int alias) {
		this.alias = alias;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getKitchenId() {
		return kitchenId;
	}
	public void setKitchenId(int kitchenId) {
		this.kitchenId = kitchenId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getImg() {
		return img != null ? img.trim() : null;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDesc() {
		return desc != null ? desc.trim() : null;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public Kitchen getKitchen() {
		return kitchen;
	}
	public void setKitchen(Kitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	/**
	 * 是否特价菜
	 * @return
	 */
	public boolean isSpecial(){
		return (status & STATUS_SPECIAL) != 0;
	}
	public void setSpecial(boolean onOff){
		if(onOff) status |= STATUS_SPECIAL;
		else status &= ~STATUS_SPECIAL;
	}
	/**
	 * 是否推荐菜
	 * @return
	 */
	public boolean isRecommend(){
		return (status & STATUS_RECOMMEND) != 0;
	}
	public void setRecommend(boolean onOff){
		if(onOff) status |= STATUS_RECOMMEND;
		else status &= ~STATUS_RECOMMEND;
	}
	/**
	 * 是否停售
	 * @return
	 */
	public boolean isStop(){
		return (status & STATUS_STOP) != 0;
	}
	public void setStop(boolean onOff){
		if(onOff) status |= STATUS_STOP;
		else status &= ~STATUS_STOP;
	}
	/**
	 * 是否赠送
	 * @return
	 */
	public boolean isGift(){
		return (status & STATUS_GIFT) != 0;	
	}
	public void setGift(boolean onOff){
		if(onOff) status |= STATUS_GIFT;
		else status &= ~STATUS_GIFT;
	}
	/**
	 * 是否套菜
	 * @return
	 */
	public boolean isCombo(){
		return (status & STATUS_COMBO) != 0;
	}
	public void setCombo(boolean onOff){
		if(onOff) status |= STATUS_COMBO;
		else status &= ~STATUS_COMBO;
	}
	
	@Override
	public String toString() {
		return "then bean for food: id->" + id
			+ " ,alias:" + alias
			+ " ,name:" + name
			+ " ,kitchenId:" + kitchenId;
	}

	public Map<String, Object> toJsonMap(int flag) {
		Map<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("id", this.id);
		jm.put("rid", this.rid);
		jm.put("alias", this.alias);
		jm.put("name", this.name);
		jm.put("price", this.price);
		jm.put("pinyin", PinyinUtil.getFirstSpell(name));
		jm.put("kitchenId", this.kitchenId);
		jm.put("status", this.status);
		jm.put("img", this.img);
		jm.put("desc", this.desc);
		
		if(flag > 0){
			if(flag == 1){
				
			}else if(flag == 2){
				if(this.restaurant != null)
					jm.put("restaurant", this.restaurant.toJsonMap(1));
			}else if(flag == 2){
				if(this.kitchen != null){
					jm.put("kitchen", this.kitchen.toJsonMap(3));
				}
			}
		}else{
			if(this.restaurant != null)
				jm.put("restaurant", this.restaurant.toJsonMap(1));
			if(this.kitchen != null)
				jm.put("kitchen", this.kitchen.toJsonMap(1));
		}
		return jm;
	}

}

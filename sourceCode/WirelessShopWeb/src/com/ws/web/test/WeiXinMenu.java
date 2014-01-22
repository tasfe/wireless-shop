package com.ws.web.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeiXinMenu {
	enum Type{
		CLICK("click", "点击推送事件."),
		VIEW("view", "地址跳转事件."),
		SUB_BUTTON("sub_button", "菜单列表.");
		
		private Type(String val, String desc){
			this.val = val;
			this.desc = desc;
		}
		private String val;
		private String desc;
		public String getVal() {
			return val;
		}
		public String getDesc() {
			return desc;
		}
		@Override
		public String toString() {
			return "val:" + val + " ,desc:" + desc;
		}
	}
	
	private Type type;
	private String name;
	private String key;
	private String url;
	private List<WeiXinMenu> sub_button;
	
	private WeiXinMenu(){}
	
	/**
	 * 点击推送信息事件
	 * @param name
	 * @param key
	 * @return
	 */
	public static WeiXinMenu clickBuilder(String name, String key){
		WeiXinMenu click = new WeiXinMenu();
		click.setType(Type.CLICK);
		click.setName(name);
		click.setKey(key);
		
		return click;
	}
	/**
	 * 地址跳转事件
	 * @param name
	 * @param url
	 * @return
	 */
	public static WeiXinMenu viewBuilder(String name, String url){
		WeiXinMenu click = new WeiXinMenu();
		click.setType(Type.VIEW);
		click.setName(name);
		click.setUrl(url);
		
		return click;
	}
	
	/**
	 * 定义子菜单
	 * @param name
	 * @param sub_button
	 * @return
	 */
	public static WeiXinMenu subButtonBuilder(String name, List<WeiXinMenu> sub_button){
		WeiXinMenu click = new WeiXinMenu();
		click.setType(Type.SUB_BUTTON);
		click.setName(name);
		click.setSub_button(sub_button);
		
		return click;
	}
	
	public Map<String, Object> toJsonMap(){
		Map<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("name", this.name);
		
		if(type.equals(Type.CLICK)){
			jm.put("type", this.type.getVal());
			jm.put("key", this.key);
		}else if(type.equals(Type.VIEW)){
			jm.put("type", this.type.getVal());
			jm.put("url", this.url);
		}else if(type.equals(Type.SUB_BUTTON)){
			if(sub_button != null && !sub_button.isEmpty()){
				List<Map<String, Object>> sub = new ArrayList<Map<String,Object>>();
				for(WeiXinMenu temp : sub_button){
					sub.add(temp.toJsonMap());
				}
				jm.put("sub_button", sub);
			}
		}
		
		return jm;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<WeiXinMenu> getSub_button() {
		if(sub_button == null) sub_button = new ArrayList<WeiXinMenu>();
		return sub_button;
	}
	public void setSub_button(List<WeiXinMenu> sub_button) {
		this.sub_button = sub_button;
	}
	
	
}

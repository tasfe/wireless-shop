package com.ws.web.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ExtTreeNode{
	
	private String text;
	private List<ExtTreeNode> childer;
	private Map<Object, Object> other;
	
	public void init(String text,  Map<Object, Object> other){
		this.text = text;
		this.other = other;
	}
	public ExtTreeNode(){}
	public ExtTreeNode(String text){
		this.init(text, null);
	}
	public ExtTreeNode(String text, Map<Object, Object> other){
		this.init(text, other);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return this.childer != null && !this.childer.isEmpty() ? false : true;
	}
	public List<ExtTreeNode> getChilder() {
		return childer;
	}
	public void setChilder(List<ExtTreeNode> childer) {
		this.childer = childer;
	}
	public Map<Object, Object> getOther() {
		return other;
	}
	public void setOther(Map<Object, Object> other) {
		this.other = other;
	}
	public void setOther(Object[][] array) {
		if(this.other == null)
			this.other = new LinkedHashMap<Object, Object>();
		for(Object[] item : array){
			this.other.put(item[0].toString(), item[1]);
		}
	}
	
	@Override
	public String toString() {
		LinkedHashMap<String, Object> jm = new LinkedHashMap<String, Object>();
		jm.put("text", this.text);
		jm.put("leaf", this.isLeaf());
		if(!this.isLeaf()){
			StringBuilder temp = new StringBuilder();
			temp.append("[");
			for(int i = 0; i < this.childer.size(); i++){
				if(i > 0)
					temp.append(",");
				temp.append(this.childer.get(i).toString());
			}
			temp.append("]");
		}
		if(this.other != null && !this.other.isEmpty()){
			jm.put("other", this.other);
		}
		return JSONObject.toJSONString(jm);
	}
	
}

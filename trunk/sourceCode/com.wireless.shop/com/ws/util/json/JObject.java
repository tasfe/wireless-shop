package com.ws.util.json;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ws.pojo.exception.BusinessException;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class JObject {
	private boolean success = true;			// 操作状态
	private int code = 1111;				// 操作码
	private String title = "提示";			// 操作信息标题
	private String msg = "";				// 操作提示信息
	private int lv = 0;						// 操作等级
	private int totalProperty = 0;			// 数据数量
	private List<? extends Jsonable> root;	// 数据主体
	private Map<Object, Object> other;		// 其他附加信息
	
	private int jflag = 0;
	private JsonConfig jc;
	
	private int ERROR_CODE = 9999;
	
	public JObject(){
		this.root = new ArrayList<Jsonable>();
		this.other = new LinkedHashMap<Object, Object>();
	}
	
	public JObject(int totalProperty, List<? extends Jsonable> root){
		this();
		this.totalProperty = totalProperty;
		this.root = root;
	}
	
	public JObject(String msg){
		this.msg = msg;
	}
	
	public JObject(boolean success, String msg){
		this.initTip(success, this.title, msg, ERROR_CODE);
	}
	
	public JObject(boolean success, String title, String msg){
		this.initTip(success, title, msg, ERROR_CODE);
	}
	
	public JObject(boolean success, String title, String msg, int code){
		this.success = success;
		this.title = title;
		this.code = code;
		this.msg = msg;
	}
	
	/*-------------------------     initTip   --------------------------------*/
	public void initTip(String msg){
		this.msg = msg;
	}
	public void initTip(boolean success, String msg){
		this.initTip(success, this.title, msg, ERROR_CODE);
	}
	public void initTip(boolean success, String title, String msg){
		this.initTip(success, title, msg, ERROR_CODE);
	}
	public void initTip(boolean success, int code, String msg){
		this.initTip(success, this.title, msg, code);
	}
	public void initTip(boolean success, String title, String msg, int code){
		this.success = success;
		this.title = title;
		this.msg = msg;
		this.code = code;
	}
	
	/*-------------------------     exception   --------------------------------*/
	public void initTip(SQLException e){
		this.initTip(false, "系统异常", "操作失败, 数据库请求操作异常.", 9998);
	}
	public void initTip(BusinessException e){
		this.initTip(false, "响应结果", e.getMessage(), e.getCode());
	}
	public void initTip(Exception e){
		this.initTip(false, "响应结果", "操作失败, 数据库请求操作异常.", 9999);
	}
	
	/*-------------------------     toString   --------------------------------*/
	@SuppressWarnings("unchecked")
	private void changeToMap(Map<String, Object> mobj){
		if(mobj == null)
			return;
		Iterator<Entry<String, Object>> ite = mobj.entrySet().iterator();
		Map<String, Object> item = null;
		while(ite.hasNext()){	
			Entry<String, Object> entry = ite.next();
			if(entry != null){
				if(entry.getValue() instanceof List){
					List<?> list = (List<?>)entry.getValue();
					List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
					for(Object temp : list){
						if(temp instanceof Jsonable){
							item = new LinkedHashMap<String, Object>(((Jsonable)temp).toJsonMap(jflag));
							changeToMap(item);
							lm.add(item);
						}else if(temp instanceof Map){
							Map<String, Object> tempMap = (Map<String, Object>) temp;
							changeToMap(tempMap);
							lm.add(tempMap);
						}
					}
					entry.setValue(lm);
				}else if(entry.getValue() instanceof Jsonable){
					item = new LinkedHashMap<String, Object>(((Jsonable)entry.getValue()).toJsonMap(jflag));
					changeToMap(item);
					entry.setValue(item);
				}else if(entry.getValue() instanceof Map){
					item = new LinkedHashMap<String, Object>((Map<String, Object>)entry.getValue());
					changeToMap(item);
					entry.setValue(item);
				}
			}
		}
	}
	
	public JObject setJflag(int jflag) {
		this.jflag = jflag;
		return this;
	}
	private void initJsonConfig(){
		this.jc = new JsonConfig();
		this.jc.setIgnoreDefaultExcludes(false);       
		this.jc.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);     
	}
	
	@Override
	public String toString() {
		Map<String, Object> copy = new LinkedHashMap<String, Object>();
		copy.put("success", this.success);
		copy.put("code", this.code);
		copy.put("title", this.title);
		copy.put("msg", this.msg);
		copy.put("lv", this.lv);
		copy.put("totalProperty", this.totalProperty);
		copy.put("root", this.root);
		copy.put("other", this.other);
		changeToMap(copy);
		
		initJsonConfig();
		return JSONSerializer.toJSON(copy, this.jc).toString();
	}
	
	/**
	 * 读取配置文件
	 * @param filePath 配置文件绝对路径
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getConfig(String filePath) throws IOException{
		String config = "", data = null;
    	try{
    		BufferedReader br = new BufferedReader(new FileReader(new File(filePath))); 
    		while((data = br.readLine()) != null){
    			config += data;
    		}
    		br.close();
    	}catch(IOException e){
    		throw new IOException("配置文件读取出错, 文件不存在, 或文件路径配置不正确.");
    	}
		return JSONObject.fromObject(config);
	}
	/**
	 * 读取配置文件
	 * @param is 资源文件流
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getConfig(InputStream is) throws IOException{
		String config = "";
    	try{
    		ByteArrayOutputStream bais = new ByteArrayOutputStream();
    		int length = 1024, count = 0;
    		byte[] temp = new byte[length];
    		while((count = is.read(temp, 0, length)) != -1){
    			bais.write(temp, 0, count);
    		}
    		config = new String(temp, "UTF-8");
    		bais.flush();
    		bais.close();
    		temp = null;
    	}catch(IOException e){
    		throw new IOException("配置文件读取出错, 文件不存在, 或文件路径配置不正确.");
    	}
		return JSONObject.fromObject(config);
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getTotalProperty() {
		return totalProperty;
	}
	public void setTotalProperty(int totalProperty) {
		this.totalProperty = totalProperty;
	}
	public List<? extends Jsonable> getRoot() {
		return root;
	}
	public void setRoot(List<? extends Jsonable> root) {
		this.root = root;
	}
	public Map<Object, Object> getOther() {
		return other;
	}
	public void setOther(Map<Object, Object> other) {
		this.other = other;
	}
	public JsonConfig getJc() {
		return jc;
	}
	public void setJc(JsonConfig jc) {
		this.jc = jc;
	}
	public int getERROR_CODE() {
		return ERROR_CODE;
	}
	public void setERROR_CODE(int eRROR_CODE) {
		ERROR_CODE = eRROR_CODE;
	}

}

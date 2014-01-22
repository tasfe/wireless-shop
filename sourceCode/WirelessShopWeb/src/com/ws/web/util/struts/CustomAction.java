package com.ws.web.util.struts;

import com.opensymphony.xwork2.ActionSupport;
import com.ws.util.json.JObject;

public class CustomAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private String result;
	protected JObject jobj;

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setResult(StringBuilder result) {
		this.result = result.toString();
	}
	public void setResult(StringBuffer result) {
		this.result = result.toString();
	}
	public void setResult(JObject jobj) {
		this.result = jobj.toString();
	}
}

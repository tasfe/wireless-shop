package com.ws.util;

import java.sql.ResultSet;

import com.ws.util.json.JObject;


public class OBeanBasic {
	protected DBHelper $conn;
	protected ResultSet res;
	protected JObject jobj;
	
	protected void safeClose(){
		DBHelper.safeClose($conn);
		DBHelper.safeClose(res);
	}
}

package com.ws.util.json;

import java.util.Map;

public interface Jsonable {
	
	public Map<String, Object> toJsonMap(int flag);
	
}

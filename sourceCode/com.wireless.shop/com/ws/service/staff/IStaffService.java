package com.ws.service.staff;

import com.ws.pojo.staff.Staff;
import com.ws.util.json.JObject;

public interface IStaffService {
	
	public JObject adminLogin(Staff staff);
}

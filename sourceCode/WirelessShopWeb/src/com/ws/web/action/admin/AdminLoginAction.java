package com.ws.web.action.admin;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ws.pojo.staff.Staff;
import com.ws.service.staff.IStaffService;
import com.ws.service.staff.impl.StaffService;
import com.ws.web.filter.AdminBasicFilter;
import com.ws.web.util.struts.CustomAction;

public class AdminLoginAction extends CustomAction{

	private static final long serialVersionUID = 1L;
	
	private IStaffService staffService = new StaffService();
	private Staff staff = new Staff();
	
	public String execute() throws Exception {
		jobj = staffService.adminLogin(staff);
		if(jobj.isSuccess()){
			ServletActionContext.getRequest().getSession().setAttribute(AdminBasicFilter.LOGIN_USER_SESSION_KEY, jobj.getOther().get("staff"));
		}
		super.setResult(jobj);
		return ActionSupport.NONE;
	}
	
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
}

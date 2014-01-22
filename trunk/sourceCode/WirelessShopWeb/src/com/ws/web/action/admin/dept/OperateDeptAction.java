package com.ws.web.action.admin.dept;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.dept.Department;
import com.ws.service.dept.IDeptService;
import com.ws.service.dept.impl.DeptService;
import com.ws.web.util.struts.CustomAction;

public class OperateDeptAction extends CustomAction{
	
	private static final long serialVersionUID = 1L;
	
	private IDeptService deptService = new DeptService();
	
	private Department dept = new Department();
	
	/**
	 * 添加新部门
	 * @return
	 * @throws Exception
	 */
	public String insert() throws Exception{
		super.setResult(deptService.insert(dept));
		return Action.NONE;
	}
	
	/**
	 * 修改部门信息
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception{
		super.setResult(deptService.update(dept));
		return Action.NONE;
	}
	
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	
}

package com.ws.web.action.admin.dept;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.dept.Kitchen;
import com.ws.service.dept.IKitchenService;
import com.ws.service.dept.impl.KitchenService;
import com.ws.web.util.struts.CustomAction;

public class OperateKitchenAction extends CustomAction{
	
	private static final long serialVersionUID = 1L;
	private IKitchenService kitchenService = new KitchenService();
	
	private Kitchen kitchen = new Kitchen();
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String insert() throws Exception {
		super.setResult(kitchenService.insert(kitchen));
		return Action.NONE;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String update() throws Exception {
		super.setResult(kitchenService.update(kitchen));
		return Action.NONE;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception {
		super.setResult(kitchenService.delete(kitchen.getId()));
		return Action.NONE;
	}

	public Kitchen getKitchen() {
		return kitchen;
	}
	public void setKitchen(Kitchen kitchen) {
		this.kitchen = kitchen;
	}
	
}

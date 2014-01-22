package com.ws.web.action.share;

import java.util.List;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.dept.Kitchen;
import com.ws.service.dept.IKitchenService;
import com.ws.service.dept.impl.KitchenService;
import com.ws.util.json.Jsonable;
import com.ws.web.util.ExtTreeNode;
import com.ws.web.util.struts.CustomAction;

public class QueryKitchenAction extends CustomAction{
	
	private static final long serialVersionUID = 1L;
	private IKitchenService kitchenService = new KitchenService();
	
	private int rid;
	private Integer deptId;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String normal() throws Exception{
		super.setResult(kitchenService.getByDeptId(null, null, rid, deptId));
		return Action.NONE;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception{
		List<? extends Jsonable> list = kitchenService.getByDeptId(null, null, rid, deptId).getRoot();
		if(!list.isEmpty()){
			StringBuilder tree = new StringBuilder();
			tree.append("[");
			Kitchen item;
			ExtTreeNode node;
			for(int i = 0; i < list.size(); i++){
				if(i > 0)
					tree.append(",");
				item = (Kitchen) list.get(i);
				node = new ExtTreeNode(item.getName());
				node.setOther(new Object[][]{
					{"id", item.getId()}, 
					{"name", item.getName()}, 
					{"rid", item.getRid()},
					{"deptId", item.getDeptId()}					
				});
				tree.append(node.toString());
			}
			tree.append("]");
			super.setResult(tree);
		}
		return Action.NONE;
	}
	
	public void setRid(int rid) {
		this.rid = rid;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
}

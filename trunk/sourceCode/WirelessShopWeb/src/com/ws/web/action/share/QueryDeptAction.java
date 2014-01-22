package com.ws.web.action.share;

import java.util.List;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.dept.Department;
import com.ws.service.dept.IDeptService;
import com.ws.service.dept.impl.DeptService;
import com.ws.util.json.Jsonable;
import com.ws.web.util.ExtTreeNode;
import com.ws.web.util.struts.CustomAction;

public class QueryDeptAction extends CustomAction{
	
	private static final long serialVersionUID = 1L;
	
	private Department dept = new Department();
	private IDeptService deptService = new DeptService();
	
	/**
	 * 普通数据格式
	 * @return
	 * @throws Exception
	 */
	public String normal() throws Exception {
		super.setResult(deptService.getByRestaurantId(null, null, dept.getRid()));
		return Action.NONE;
	}
	
	/**
	 * 树形数据格式
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception {
		List<? extends Jsonable> list = deptService.getByRestaurantId(null, null, dept.getRid()).getRoot();
		if(!list.isEmpty()){
			StringBuilder tree = new StringBuilder();
			tree.append("[");
			Department item;
			ExtTreeNode node;
			for(int i = 0; i < list.size(); i++){
				if(i > 0)
					tree.append(",");
				item = (Department) list.get(i);
				node = new ExtTreeNode(item.getName());
				node.setOther(new Object[][]{{"id", item.getId()}, {"name", item.getName()}, {"rid", item.getRid()}});
				tree.append(node.toString());
			}
			tree.append("]");
			super.setResult(tree);
		}
		return Action.NONE;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String mobile() throws Exception {
		super.setResult(deptService.getByRestaurantId(null, null, dept.getRid(), true));
		return Action.NONE;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

}

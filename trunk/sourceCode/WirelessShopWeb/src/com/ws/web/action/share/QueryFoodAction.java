package com.ws.web.action.share;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.food.Food;
import com.ws.service.food.IFoodService;
import com.ws.service.food.impl.FoodService;
import com.ws.util.oss.OSSImageUtil;
import com.ws.util.oss.OSSParams;
import com.ws.web.init.InitServlet;
import com.ws.web.util.struts.CustomAction;

public class QueryFoodAction extends CustomAction{

	private static final long serialVersionUID = 1L;
	private IFoodService foodService = new FoodService();
	
	private Integer start;
	private Integer limit;
	private Food food = new Food();
	
	public String normal(){
		super.setResult(foodService.getByExtra(start, limit, food));
		return Action.NONE;
	}
	/**
	 * weixin
	 * @return
	 */
	public String weixin(){
		jobj = foodService.getByExtra(start, limit, food);
		jobj.setJflag(1);
		jobj.getOther().put("imgUrl", "http://" 
				+ InitServlet.getConfig().getString(OSSImageUtil.BUCKET_IMAGE_KEY) 
				+ "." 
				+ InitServlet.getConfig().getString(OSSParams.OSS_OUTER_POINT_KEY)
		);
		jobj.getOther().put("imgUrlDefault", OSSImageUtil.BUCKET_IMAGE_DEFAULT);
		super.setResult(jobj);
		return Action.NONE;
	}
	/**
	 * mobile
	 * @return
	 */
	public String mobile(){
		jobj = foodService.getByExtra(start, limit, food);
		jobj.setJflag(1);
		jobj.getOther().put("imgUrl", "http://" 
				+ InitServlet.getConfig().getString(OSSImageUtil.BUCKET_IMAGE_KEY) 
				+ "." 
				+ InitServlet.getConfig().getString(OSSParams.OSS_OUTER_POINT_KEY)
		);
		jobj.getOther().put("imgUrlDefault", OSSImageUtil.BUCKET_IMAGE_DEFAULT);
		super.setResult(jobj);
		return Action.NONE;
	}

	public void setStart(Integer start) {
		this.start = start;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	
}

package com.ws.web.action.admin.food;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.food.Food;
import com.ws.service.food.IFoodService;
import com.ws.service.food.impl.FoodService;
import com.ws.web.util.struts.CustomAction;

public class OperateFoodAction extends CustomAction{

	private static final long serialVersionUID = 1L;
	private IFoodService foodService = new FoodService();
	
	private Food food = new Food();
	
	public String insert(){
		super.setResult(foodService.insert(food));
		return Action.NONE;
	}
	
	public String update(){
		super.setResult(foodService.update(food));
		return Action.NONE;
	}
	
	public void setFood(Food food) {
		this.food = food;
	}
	public Food getFood() {
		return food;
	}
	
}

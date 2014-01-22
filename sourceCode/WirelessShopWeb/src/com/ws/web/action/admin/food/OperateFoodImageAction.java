package com.ws.web.action.admin.food;

import java.io.File;

import com.opensymphony.xwork2.Action;
import com.ws.pojo.food.Food;
import com.ws.service.food.IFoodService;
import com.ws.service.food.impl.FoodService;
import com.ws.util.json.JObject;
import com.ws.web.util.struts.CustomAction;

public class OperateFoodImageAction extends CustomAction{
	
	private static final long serialVersionUID = 1L;
	private IFoodService foodService = new FoodService();
	
	private File foodImage; //上传的文件
	private String foodImageFileName; //上传的文件
	private Food food = new Food();
	
	/**
	 * 删除图片
	 * @return
	 */
	public String delete(){
//		File tempFile = new File(this.getClass().getResource("/").getPath());
//		System.out.println("abs1:   "+tempFile.getAbsolutePath());
//		tempFile = new File(tempFile.getParent() + File.separator + "temp");
//		System.out.println("abs4:   "+tempFile.getAbsolutePath());
		jobj = new JObject();
		jobj.setMsg("delete image..........................");
		System.out.println("result:   "+jobj.getMsg());
		super.setResult(jobj);
		return Action.NONE;
	}
	
	/**
	 * 上传图片
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception{
		super.setResult(foodService.uploadImage(food, foodImage, foodImageFileName));
		return Action.NONE;
	}
	
	public void setFoodImage(File foodImage) {
		this.foodImage = foodImage;
	}

	public String getFoodImageFileName() {
		return foodImageFileName;
	}
	public void setFoodImageFileName(String foodImageFileName) {
		this.foodImageFileName = foodImageFileName;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
}

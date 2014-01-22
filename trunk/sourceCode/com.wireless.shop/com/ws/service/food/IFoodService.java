package com.ws.service.food;

import java.io.File;

import com.ws.pojo.food.Food;
import com.ws.util.json.JObject;

public interface IFoodService {
	public JObject getByExtra(Integer start, Integer limit, Food extra);
	public JObject insert(Food insert);
	public JObject update(Food update);
	public JObject uploadImage(Food upload, File img, String imgName);
	public JObject deleteImage(Food delete);
}

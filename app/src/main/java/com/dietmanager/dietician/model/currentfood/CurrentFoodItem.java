package com.dietmanager.dietician.model.currentfood;

import com.dietmanager.dietician.model.food.FoodItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentFoodItem implements Serializable {

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("current")
	private int current;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("dietitian")
	private Dietitian dietitian;

	@SerializedName("food_id")
	private int foodId;

	@SerializedName("day")
	private int day;

	@SerializedName("food")
	private FoodItem food;

	public int getDietitianId(){
		return dietitianId;
	}

	public int getCurrent(){
		return current;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public Dietitian getDietitian(){
		return dietitian;
	}

	public int getFoodId(){
		return foodId;
	}

	public int getDay(){
		return day;
	}

	public FoodItem getFood(){
		return food;
	}
}
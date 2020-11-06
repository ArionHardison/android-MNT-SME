package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Foodingredient implements Serializable {

	@SerializedName("ingredient")
	private Ingredient ingredient;

	@SerializedName("ingredient_id")
	private int ingredientId;

	@SerializedName("price")
	private String price;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("food_id")
	private int foodId;

	@SerializedName("status")
	private String status;

	public Ingredient getIngredient(){
		return ingredient;
	}

	public int getIngredientId(){
		return ingredientId;
	}

	public String getPrice(){
		return price;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public int getFoodId(){
		return foodId;
	}

	public String getStatus(){
		return status;
	}
}
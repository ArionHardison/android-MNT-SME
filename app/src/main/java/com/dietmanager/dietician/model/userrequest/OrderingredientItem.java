package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderingredientItem implements Serializable {

	@SerializedName("foodingredient")
	private Foodingredient foodingredient;

	@SerializedName("ingredient_id")
	private int ingredientId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	public Foodingredient getFoodingredient(){
		return foodingredient;
	}

	public int getIngredientId(){
		return ingredientId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public int getOrderId(){
		return orderId;
	}
}
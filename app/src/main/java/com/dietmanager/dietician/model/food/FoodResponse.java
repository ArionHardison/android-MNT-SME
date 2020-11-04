package com.dietmanager.dietician.model.food;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FoodResponse implements Serializable {

	@SerializedName("lunch")
	private List<FoodItem> lunch;

	@SerializedName("breakfast")
	private List<FoodItem> breakfast;

	@SerializedName("dinner")
	private List<FoodItem> dinner;

	@SerializedName("snacks")
	private List<FoodItem> snacks;

	public List<FoodItem> getLunch(){
		return lunch;
	}
	public List<FoodItem> geSnacks(){
		return snacks;
	}

	public List<FoodItem> getBreakfast(){
		return breakfast;
	}

	public List<FoodItem> getDinner(){
		return dinner;
	}
}
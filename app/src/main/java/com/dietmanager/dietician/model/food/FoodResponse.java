package com.dietmanager.dietician.model.food;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FoodResponse implements Serializable {

	@SerializedName("lunch")
	private List<FoodItem> lunch=new ArrayList<>();

	@SerializedName("breakfast")
	private List<FoodItem> breakfast=new ArrayList<>();

	@SerializedName("dinner")
	private List<FoodItem> dinner=new ArrayList<>();

	@SerializedName("snacks")
	private List<FoodItem> snacks=new ArrayList<>();

	public List<FoodItem> getLunch(){
		return lunch;
	}
	public List<FoodItem> getSnacks(){
		return snacks;
	}

	public List<FoodItem> getBreakfast(){
		return breakfast;
	}

	public List<FoodItem> getDinner(){
		return dinner;
	}
}
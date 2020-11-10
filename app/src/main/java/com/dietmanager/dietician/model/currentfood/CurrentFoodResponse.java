package com.dietmanager.dietician.model.currentfood;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CurrentFoodResponse implements Serializable {

	@SerializedName("CurrentFood")
	private List<CurrentFoodItem> currentFood;

	public List<CurrentFoodItem> getCurrentFood(){
		return currentFood;
	}
}
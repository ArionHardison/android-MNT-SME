package com.dietmanager.dietician.model.ingredients;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IngredientsResponse{

	@SerializedName("ingredients")
	private List<IngredientsItem> ingredients;

	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}
}
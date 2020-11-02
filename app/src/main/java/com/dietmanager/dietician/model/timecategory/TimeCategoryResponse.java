package com.dietmanager.dietician.model.timecategory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TimeCategoryResponse{

	@SerializedName("timeCategory")
	private List<TimeCategoryItem> timeCategory;

	public List<TimeCategoryItem> getTimeCategory(){
		return timeCategory;
	}
}
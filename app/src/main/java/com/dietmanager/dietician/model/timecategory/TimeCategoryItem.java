package com.dietmanager.dietician.model.timecategory;

import com.google.gson.annotations.SerializedName;

public class TimeCategoryItem{

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private String status;

	public String getName(){
		return name;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}
}
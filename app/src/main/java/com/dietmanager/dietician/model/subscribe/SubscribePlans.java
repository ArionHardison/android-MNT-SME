package com.dietmanager.dietician.model.subscribe;

import com.google.gson.annotations.SerializedName;

public class SubscribePlans{

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("expiry_date")
	private String expiryDate;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("plan_id")
	private int planId;

	public int getDietitianId(){
		return dietitianId;
	}

	public int getUserId(){
		return userId;
	}

	public String getExpiryDate(){
		return expiryDate;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public int getPlanId(){
		return planId;
	}
}
package com.dietmanager.dietician.model.subscribe;

import com.google.gson.annotations.SerializedName;

public class SubscribeItem{

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

	@SerializedName("user")
	private SubscribedUser user;

	@SerializedName("subscription_plan")
	private String subscriptionPlan;

	@SerializedName("status")
	private String status;

	@SerializedName("start_date")
	private String startDate;

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

	public SubscribedUser getUser(){
		return user;
	}

	public String getSubscriptionPlan(){
		return subscriptionPlan;
	}

	public String getStatus(){
		return status;
	}

	public String getStartDate(){
		return startDate;
	}
}
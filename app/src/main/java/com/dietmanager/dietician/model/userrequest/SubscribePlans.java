package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscribePlans implements Serializable {


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

	@SerializedName("subscription")
	private Subscription subscription;

	@SerializedName("plan_id")
	private int planId;

	public void setDietitianId(int dietitianId){
		this.dietitianId = dietitianId;
	}

	public int getDietitianId(){
		return dietitianId;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setExpiryDate(String expiryDate){
		this.expiryDate = expiryDate;
	}

	public String getExpiryDate(){
		return expiryDate;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSubscription(Subscription subscription){
		this.subscription = subscription;
	}

	public Subscription getSubscription(){
		return subscription;
	}

	public void setPlanId(int planId){
		this.planId = planId;
	}

	public int getPlanId(){
		return planId;
	}
}
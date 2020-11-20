package com.dietmanager.dietician.model.subscriptionplan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionPlanItem implements Serializable {

	@SerializedName("dietitian_id")
	private int dietitianId;
	@SerializedName("auto_assign")
	private int autoAssign;

	public int getAutoAssign() {
		return autoAssign;
	}

	@SerializedName("price")
	private String price;

	@SerializedName("no_of_days")
	private int noOfDays;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private String status;

	public int getDietitianId(){
		return dietitianId;
	}

	public String getPrice(){
		return price;
	}

	public int getNoOfDays(){
		return noOfDays;
	}

	public String getDescription(){
		return description;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getStatus(){
		return status;
	}
}
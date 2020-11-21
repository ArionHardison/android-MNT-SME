package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Subscription implements Serializable {

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("auto_assign")
	private int autoAssign;

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

	public void setDietitianId(int dietitianId){
		this.dietitianId = dietitianId;
	}

	public int getDietitianId(){
		return dietitianId;
	}

	public void setAutoAssign(int autoAssign){
		this.autoAssign = autoAssign;
	}

	public int getAutoAssign(){
		return autoAssign;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setNoOfDays(int noOfDays){
		this.noOfDays = noOfDays;
	}

	public int getNoOfDays(){
		return noOfDays;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
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

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
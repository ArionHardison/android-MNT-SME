package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Plan implements Serializable {

	@SerializedName("dietitian_id")
	private Integer dietitianId;

	@SerializedName("auto_assign")
	private Integer autoAssign;

	@SerializedName("price")
	private String price;

	@SerializedName("no_of_days")
	private Integer noOfDays;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private Integer id;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private String status;

	public void setDietitianId(Integer dietitianId){
		this.dietitianId = dietitianId;
	}

	public Integer getDietitianId(){
		return dietitianId;
	}

	public void setAutoAssign(Integer autoAssign){
		this.autoAssign = autoAssign;
	}

	public Integer getAutoAssign(){
		return autoAssign;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setNoOfDays(Integer noOfDays){
		this.noOfDays = noOfDays;
	}

	public Integer getNoOfDays(){
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

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
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
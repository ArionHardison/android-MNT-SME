package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredient implements Serializable {

	@SerializedName("dietitian_id")
	private Object dietitianId;

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("unit_type_id")
	private int unitTypeId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("status")
	private String status;

	public Object getDietitianId(){
		return dietitianId;
	}

	public String getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public int getUnitTypeId(){
		return unitTypeId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getAvatar(){
		return avatar;
	}

	public String getStatus(){
		return status;
	}
}
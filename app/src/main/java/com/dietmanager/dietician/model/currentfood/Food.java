package com.dietmanager.dietician.model.currentfood;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food implements Serializable {

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("code")
	private Object code;

	@SerializedName("price")
	private String price;

	@SerializedName("time_category_id")
	private String timeCategoryId;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("days")
	private int days;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("status")
	private String status;

	@SerializedName("who")
	private String who;

	public int getDietitianId(){
		return dietitianId;
	}

	public Object getCode(){
		return code;
	}

	public String getPrice(){
		return price;
	}

	public String getTimeCategoryId(){
		return timeCategoryId;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getDays(){
		return days;
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

	public String getWho(){
		return who;
	}
}
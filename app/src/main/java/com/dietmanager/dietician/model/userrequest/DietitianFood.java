package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DietitianFood implements Serializable {

	@SerializedName("dietitian_id")
	private Integer dietitianId;

	@SerializedName("current")
	private Integer current;

	@SerializedName("category_id")
	private Integer categoryId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private Integer id;

	@SerializedName("food_id")
	private Integer foodId;

	@SerializedName("day")
	private Integer day;

	@SerializedName("plan")
	private Plan plan;

	@SerializedName("plan_id")
	private Integer planId;

	public void setDietitianId(Integer dietitianId){
		this.dietitianId = dietitianId;
	}

	public Integer getDietitianId(){
		return dietitianId;
	}

	public void setCurrent(Integer current){
		this.current = current;
	}

	public Integer getCurrent(){
		return current;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getCategoryId(){
		return categoryId;
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

	public void setFoodId(Integer foodId){
		this.foodId = foodId;
	}

	public Integer getFoodId(){
		return foodId;
	}

	public void setDay(Integer day){
		this.day = day;
	}

	public Integer getDay(){
		return day;
	}

	public void setPlan(Plan plan){
		this.plan = plan;
	}

	public Plan getPlan(){
		return plan;
	}

	public void setPlanId(Integer planId){
		this.planId = planId;
	}

	public Integer getPlanId(){
		return planId;
	}
}
package com.dietmanager.dietician.model.ingredients;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IngredientsItem implements Serializable {

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("isChecked")
	private boolean isChecked=false;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("unit_type_id")
	private int unitTypeId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("unit_type")
	private UnitType unitType;

	@SerializedName("status")
	private String status;

	public Object getDietitianId(){
		return dietitianId;
	}

	public void setDietitianId(int dietitianId) {
		this.dietitianId = dietitianId;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setUnitTypeId(int unitTypeId) {
		this.unitTypeId = unitTypeId;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Object getAvatar(){
		return avatar;
	}

	public UnitType getUnitType(){
		return unitType;
	}

	public String getStatus(){
		return status;
	}
}
package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestFilterItem implements Serializable {

	@SerializedName("chef_id")
	private int chefId;

	@SerializedName("order_request_id")
	private int orderRequestId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setChefId(int chefId){
		this.chefId = chefId;
	}

	public int getChefId(){
		return chefId;
	}

	public void setOrderRequestId(int orderRequestId){
		this.orderRequestId = orderRequestId;
	}

	public int getOrderRequestId(){
		return orderRequestId;
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
}
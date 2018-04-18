package com.tomoeats.restaurant.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class DisputesItem{

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("type")
	private String type;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("disputecomment")
	private List<Object> disputecomment;

	@SerializedName("shop_id")
	private int shopId;

	@SerializedName("created_to")
	private String createdTo;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("order_disputehelp_id")
	private int orderDisputehelpId;

	@SerializedName("dispute_help")
	private Object disputeHelp;

	@SerializedName("transporter_id")
	private int transporterId;

	@SerializedName("status")
	private String status;

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

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setDisputecomment(List<Object> disputecomment){
		this.disputecomment = disputecomment;
	}

	public List<Object> getDisputecomment(){
		return disputecomment;
	}

	public void setShopId(int shopId){
		this.shopId = shopId;
	}

	public int getShopId(){
		return shopId;
	}

	public void setCreatedTo(String createdTo){
		this.createdTo = createdTo;
	}

	public String getCreatedTo(){
		return createdTo;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	public void setOrderDisputehelpId(int orderDisputehelpId){
		this.orderDisputehelpId = orderDisputehelpId;
	}

	public int getOrderDisputehelpId(){
		return orderDisputehelpId;
	}

	public void setDisputeHelp(Object disputeHelp){
		this.disputeHelp = disputeHelp;
	}

	public Object getDisputeHelp(){
		return disputeHelp;
	}

	public void setTransporterId(int transporterId){
		this.transporterId = transporterId;
	}

	public int getTransporterId(){
		return transporterId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
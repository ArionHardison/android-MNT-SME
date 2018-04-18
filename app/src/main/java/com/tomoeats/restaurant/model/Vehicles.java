package com.tomoeats.restaurant.model;


import com.google.gson.annotations.SerializedName;


public class Vehicles{

	@SerializedName("vehicle_no")
	private String vehicleNo;

	@SerializedName("id")
	private int id;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("transporter_id")
	private int transporterId;

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleNo(){
		return vehicleNo;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public void setTransporterId(int transporterId){
		this.transporterId = transporterId;
	}

	public int getTransporterId(){
		return transporterId;
	}
}
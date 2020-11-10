package com.dietmanager.dietician.model.currentfood;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Dietitian implements Serializable {

	@SerializedName("unique_id")
	private String uniqueId;

	@SerializedName("address")
	private Object address;

	@SerializedName("gender")
	private Object gender;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("latitude")
	private Object latitude;

	@SerializedName("mobile")
	private long mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("description")
	private Object description;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("avatar")
	private Object avatar;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("country_code")
	private Object countryCode;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("email")
	private String email;

	@SerializedName("longitude")
	private Object longitude;

	@SerializedName("status")
	private String status;

	public String getUniqueId(){
		return uniqueId;
	}

	public Object getAddress(){
		return address;
	}

	public Object getGender(){
		return gender;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public Object getLatitude(){
		return latitude;
	}

	public long getMobile(){
		return mobile;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public Object getDescription(){
		return description;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public Object getAvatar(){
		return avatar;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public Object getCountryCode(){
		return countryCode;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public Object getLongitude(){
		return longitude;
	}

	public String getStatus(){
		return status;
	}
}
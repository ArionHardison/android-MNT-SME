package com.tomoeats.restaurant.model.ordernew;


import com.google.gson.annotations.SerializedName;


public class Shop{

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("rating")
	private int rating;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("pure_veg")
	private int pureVeg;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("estimated_delivery_time")
	private int estimatedDeliveryTime;

	@SerializedName("id")
	private int id;

	@SerializedName("default_banner")
	private Object defaultBanner;

	@SerializedName("maps_address")
	private String mapsAddress;

	@SerializedName("popular")
	private int popular;

	@SerializedName("email")
	private String email;

	@SerializedName("offer_min_amount")
	private int offerMinAmount;

	@SerializedName("longitude")
	private double longitude;

	@SerializedName("offer_percent")
	private int offerPercent;

	@SerializedName("address")
	private String address;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("rating_status")
	private int ratingStatus;

	@SerializedName("phone")
	private String phone;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("name")
	private String name;

	@SerializedName("status")
	private String status;

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCreatedAt(Object createdAt){
		this.createdAt = createdAt;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public void setPureVeg(int pureVeg){
		this.pureVeg = pureVeg;
	}

	public int getPureVeg(){
		return pureVeg;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setEstimatedDeliveryTime(int estimatedDeliveryTime){
		this.estimatedDeliveryTime = estimatedDeliveryTime;
	}

	public int getEstimatedDeliveryTime(){
		return estimatedDeliveryTime;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDefaultBanner(Object defaultBanner){
		this.defaultBanner = defaultBanner;
	}

	public Object getDefaultBanner(){
		return defaultBanner;
	}

	public void setMapsAddress(String mapsAddress){
		this.mapsAddress = mapsAddress;
	}

	public String getMapsAddress(){
		return mapsAddress;
	}

	public void setPopular(int popular){
		this.popular = popular;
	}

	public int getPopular(){
		return popular;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setOfferMinAmount(int offerMinAmount){
		this.offerMinAmount = offerMinAmount;
	}

	public int getOfferMinAmount(){
		return offerMinAmount;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	public void setOfferPercent(int offerPercent){
		this.offerPercent = offerPercent;
	}

	public int getOfferPercent(){
		return offerPercent;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public void setRatingStatus(int ratingStatus){
		this.ratingStatus = ratingStatus;
	}

	public int getRatingStatus(){
		return ratingStatus;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setDeviceToken(String deviceToken){
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
package com.dietmanager.dietician.model.userrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

	@SerializedName("user")
	private User user;

	@SerializedName("stripe_cust_id")
	private Object stripeCustId;

	@SerializedName("customer_support")
	private Object customerSupport;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("wallet_balance")
	private int walletBalance;

	@SerializedName("subscribe_plans")
	private SubscribePlans subscribePlans;

	public SubscribePlans getSubscribePlans() {
		return subscribePlans;
	}

	@SerializedName("device_type")
	private String deviceType;
	@SerializedName("map_address")
	private String mapAddress;
	@SerializedName("latitude")
	private Double latitude;
	@SerializedName("longitude")
	private Double longitude;

	public String getMapAddress() {
		return mapAddress;
	}

	@SerializedName("otp")
	private String otp;

	@SerializedName("avatar")
	private Object avatar;

	@SerializedName("cuisines")
	private Object cuisines;

	@SerializedName("phone")
	private String phone;

	@SerializedName("social_unique_id")
	private Object socialUniqueId;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("referral_code")
	private Object referralCode;

	@SerializedName("name")
	private String name;

	@SerializedName("referral_amount")
	private String referralAmount;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@SerializedName("id")
	private int id;

	@SerializedName("login_by")
	private String loginBy;

	@SerializedName("email")
	private String email;

	@SerializedName("braintree_id")
	private Object braintreeId;

	public User getUser(){
		return user;
	}

	public Object getStripeCustId(){
		return stripeCustId;
	}

	public Object getCustomerSupport(){
		return customerSupport;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public int getWalletBalance(){
		return walletBalance;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public String getOtp(){
		return otp;
	}

	public Object getAvatar(){
		return avatar;
	}

	public Object getCuisines(){
		return cuisines;
	}

	public String getPhone(){
		return phone;
	}

	public Object getSocialUniqueId(){
		return socialUniqueId;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public Object getReferralCode(){
		return referralCode;
	}

	public String getName(){
		return name;
	}

	public String getReferralAmount(){
		return referralAmount;
	}

	public int getId(){
		return id;
	}

	public String getLoginBy(){
		return loginBy;
	}

	public String getEmail(){
		return email;
	}

	public Object getBraintreeId(){
		return braintreeId;
	}
}
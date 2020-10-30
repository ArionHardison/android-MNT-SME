package com.dietmanager.dietician.model.subscribe;

import com.google.gson.annotations.SerializedName;

public class SubscribedUser {
	@SerializedName("stripe_cust_id")
	private Object stripeCustId;

	@SerializedName("customer_support")
	private Object customerSupport;

	@SerializedName("device_id")
	private Object deviceId;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("wallet_balance")
	private int walletBalance;

	@SerializedName("device_type")
	private String deviceType;

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
	private Object deviceToken;

	@SerializedName("referral_code")
	private Object referralCode;

	@SerializedName("name")
	private String name;

	@SerializedName("referral_amount")
	private String referralAmount;

	@SerializedName("map_address")
	private String mapAddress;

	@SerializedName("id")
	private int id;

	@SerializedName("login_by")
	private String loginBy;

	@SerializedName("email")
	private String email;

	@SerializedName("braintree_id")
	private Object braintreeId;

	@SerializedName("longitude")
	private String longitude;

	public Object getStripeCustId(){
		return stripeCustId;
	}

	public Object getCustomerSupport(){
		return customerSupport;
	}

	public Object getDeviceId(){
		return deviceId;
	}

	public String getLatitude(){
		return latitude;
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

	public Object getDeviceToken(){
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

	public String getMapAddress(){
		return mapAddress;
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

	public String getLongitude(){
		return longitude;
	}
}
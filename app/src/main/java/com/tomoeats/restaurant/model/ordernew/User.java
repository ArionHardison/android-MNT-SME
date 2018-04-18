package com.tomoeats.restaurant.model.ordernew;


import com.google.gson.annotations.SerializedName;


public class User{

	@SerializedName("stripe_cust_id")
	private Object stripeCustId;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("wallet_balance")
	private int walletBalance;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("otp")
	private String otp;

	@SerializedName("avatar")
	private Object avatar;

	@SerializedName("phone")
	private String phone;

	@SerializedName("social_unique_id")
	private String socialUniqueId;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("login_by")
	private String loginBy;

	@SerializedName("email")
	private String email;

	@SerializedName("braintree_id")
	private Object braintreeId;

	public void setStripeCustId(Object stripeCustId){
		this.stripeCustId = stripeCustId;
	}

	public Object getStripeCustId(){
		return stripeCustId;
	}

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public void setWalletBalance(int walletBalance){
		this.walletBalance = walletBalance;
	}

	public int getWalletBalance(){
		return walletBalance;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public void setOtp(String otp){
		this.otp = otp;
	}

	public String getOtp(){
		return otp;
	}

	public void setAvatar(Object avatar){
		this.avatar = avatar;
	}

	public Object getAvatar(){
		return avatar;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setSocialUniqueId(String socialUniqueId){
		this.socialUniqueId = socialUniqueId;
	}

	public String getSocialUniqueId(){
		return socialUniqueId;
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

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLoginBy(String loginBy){
		this.loginBy = loginBy;
	}

	public String getLoginBy(){
		return loginBy;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setBraintreeId(Object braintreeId){
		this.braintreeId = braintreeId;
	}

	public Object getBraintreeId(){
		return braintreeId;
	}
}
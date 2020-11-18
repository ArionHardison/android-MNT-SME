package com.dietmanager.dietician.model.initeduser;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("stripe_cust_id")
	private Object stripeCustId;

	@SerializedName("customer_support")
	private Object customerSupport;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("latitude")
	private Object latitude;

	@SerializedName("wallet_balance")
	private int walletBalance;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("otp")
	private String otp;

	@SerializedName("avatar")
	private Object avatar;

	@SerializedName("is_verified")
	private int isVerified;

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

	@SerializedName("map_address")
	private String mapAddress;
	@SerializedName("rating")
	private String rating="0";

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@SerializedName("id")
	private int id;

	@SerializedName("login_by")
	private String loginBy;

	@SerializedName("email")
	private String email;

	@SerializedName("braintree_id")
	private Object braintreeId;

	@SerializedName("longitude")
	private Object longitude;

	public void setStripeCustId(Object stripeCustId){
		this.stripeCustId = stripeCustId;
	}

	public Object getStripeCustId(){
		return stripeCustId;
	}

	public void setCustomerSupport(Object customerSupport){
		this.customerSupport = customerSupport;
	}

	public Object getCustomerSupport(){
		return customerSupport;
	}

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public void setLatitude(Object latitude){
		this.latitude = latitude;
	}

	public Object getLatitude(){
		return latitude;
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

	public void setIsVerified(int isVerified){
		this.isVerified = isVerified;
	}

	public int getIsVerified(){
		return isVerified;
	}

	public void setCuisines(Object cuisines){
		this.cuisines = cuisines;
	}

	public Object getCuisines(){
		return cuisines;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setSocialUniqueId(Object socialUniqueId){
		this.socialUniqueId = socialUniqueId;
	}

	public Object getSocialUniqueId(){
		return socialUniqueId;
	}

	public void setDeviceToken(String deviceToken){
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public void setReferralCode(Object referralCode){
		this.referralCode = referralCode;
	}

	public Object getReferralCode(){
		return referralCode;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setReferralAmount(String referralAmount){
		this.referralAmount = referralAmount;
	}

	public String getReferralAmount(){
		return referralAmount;
	}

	public void setMapAddress(String mapAddress){
		this.mapAddress = mapAddress;
	}

	public String getMapAddress(){
		return mapAddress;
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

	public void setLongitude(Object longitude){
		this.longitude = longitude;
	}

	public Object getLongitude(){
		return longitude;
	}
}
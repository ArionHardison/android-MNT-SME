package com.dietmanager.dietician.model.subscribe;

import com.google.gson.annotations.SerializedName;

public class SubscribeItem{

	@SerializedName("transaction_id")
	private Object transactionId;

	@SerializedName("payment_mode")
	private String paymentMode;

	@SerializedName("gross")
	private String gross;

	@SerializedName("expiry_date")
	private String expiryDate;

	@SerializedName("unsubscribe_date")
	private Object unsubscribeDate;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("tax")
	private String tax;

	@SerializedName("is_wallet")
	private int isWallet;

	@SerializedName("card_id")
	private Object cardId;

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("admin_commission")
	private String adminCommission;

	@SerializedName("dietitian_commission")
	private String dietitianCommission;

	@SerializedName("total")
	private String total;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("id")
	private int id;

	@SerializedName("is_paid")
	private int isPaid;

	@SerializedName("user")
	private User user;

	@SerializedName("plan_id")
	private int planId;

	@SerializedName("wallet_amount")
	private String walletAmount;

	public void setTransactionId(Object transactionId){
		this.transactionId = transactionId;
	}

	public Object getTransactionId(){
		return transactionId;
	}

	public void setPaymentMode(String paymentMode){
		this.paymentMode = paymentMode;
	}

	public String getPaymentMode(){
		return paymentMode;
	}

	public void setGross(String gross){
		this.gross = gross;
	}

	public String getGross(){
		return gross;
	}

	public void setExpiryDate(String expiryDate){
		this.expiryDate = expiryDate;
	}

	public String getExpiryDate(){
		return expiryDate;
	}

	public void setUnsubscribeDate(Object unsubscribeDate){
		this.unsubscribeDate = unsubscribeDate;
	}

	public Object getUnsubscribeDate(){
		return unsubscribeDate;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTax(String tax){
		this.tax = tax;
	}

	public String getTax(){
		return tax;
	}

	public void setIsWallet(int isWallet){
		this.isWallet = isWallet;
	}

	public int getIsWallet(){
		return isWallet;
	}

	public void setCardId(Object cardId){
		this.cardId = cardId;
	}

	public Object getCardId(){
		return cardId;
	}

	public void setDietitianId(int dietitianId){
		this.dietitianId = dietitianId;
	}

	public int getDietitianId(){
		return dietitianId;
	}

	public void setAdminCommission(String adminCommission){
		this.adminCommission = adminCommission;
	}

	public String getAdminCommission(){
		return adminCommission;
	}

	public void setDietitianCommission(String dietitianCommission){
		this.dietitianCommission = dietitianCommission;
	}

	public String getDietitianCommission(){
		return dietitianCommission;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
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

	public void setIsPaid(int isPaid){
		this.isPaid = isPaid;
	}

	public int getIsPaid(){
		return isPaid;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setPlanId(int planId){
		this.planId = planId;
	}

	public int getPlanId(){
		return planId;
	}

	public void setWalletAmount(String walletAmount){
		this.walletAmount = walletAmount;
	}

	public String getWalletAmount(){
		return walletAmount;
	}
}
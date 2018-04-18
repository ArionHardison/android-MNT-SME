package com.tomoeats.restaurant.model;


import com.google.gson.annotations.SerializedName;


public class Invoice{

	@SerializedName("payment_mode")
	private String paymentMode;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("gross")
	private Double gross;

	@SerializedName("discount")
	private Double discount;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("tax")
	private Double tax;

	@SerializedName("total_pay")
	private Double totalPay;

	@SerializedName("ripple_price")
	private String ripplePrice;

	@SerializedName("delivery_charge")
	private Double deliveryCharge;

	@SerializedName("SGST")
	private double sGST;

	@SerializedName("payable")
	private Double payable;

	@SerializedName("payment_id")
	private String paymentId;

	@SerializedName("paid")
	private Double paid;

	@SerializedName("tender_pay")
	private Double tenderPay;

	@SerializedName("CGST")
	private double cGST;

	@SerializedName("id")
	private int id;

	@SerializedName("net")
	private Double net;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("wallet_amount")
	private Double walletAmount;

	@SerializedName("status")
	private String status;

	public void setPaymentMode(String paymentMode){
		this.paymentMode = paymentMode;
	}

	public String getPaymentMode(){
		return paymentMode;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setGross(Double gross){
		this.gross = gross;
	}

	public Double getGross(){
		return gross;
	}

	public void setDiscount(Double discount){
		this.discount = discount;
	}

	public Double getDiscount(){
		return discount;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTax(Double tax){
		this.tax = tax;
	}

	public Double getTax(){
		return tax;
	}

	public void setTotalPay(Double totalPay){
		this.totalPay = totalPay;
	}

	public Double getTotalPay(){
		return totalPay;
	}

	public void setRipplePrice(String ripplePrice){
		this.ripplePrice = ripplePrice;
	}

	public String getRipplePrice(){
		return ripplePrice;
	}

	public void setDeliveryCharge(Double deliveryCharge){
		this.deliveryCharge = deliveryCharge;
	}

	public Double getDeliveryCharge(){
		return deliveryCharge;
	}

	public void setSGST(double sGST){
		this.sGST = sGST;
	}

	public double getSGST(){
		return sGST;
	}

	public void setPayable(Double payable){
		this.payable = payable;
	}

	public Double getPayable(){
		return payable;
	}

	public void setPaymentId(String paymentId){
		this.paymentId = paymentId;
	}

	public String getPaymentId(){
		return paymentId;
	}

	public void setPaid(Double paid){
		this.paid = paid;
	}

	public Double getPaid(){
		return paid;
	}

	public void setTenderPay(Double tenderPay){
		this.tenderPay = tenderPay;
	}

	public Double getTenderPay(){
		return tenderPay;
	}

	public void setCGST(double cGST){
		this.cGST = cGST;
	}

	public double getCGST(){
		return cGST;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setNet(Double net){
		this.net = net;
	}

	public Double getNet(){
		return net;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	public void setWalletAmount(Double walletAmount){
		this.walletAmount = walletAmount;
	}

	public Double getWalletAmount(){
		return walletAmount;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
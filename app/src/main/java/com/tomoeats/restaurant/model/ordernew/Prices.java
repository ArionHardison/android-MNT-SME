package com.tomoeats.restaurant.model.ordernew;


import com.google.gson.annotations.SerializedName;


public class Prices{

	@SerializedName("price")
	private int price;

	@SerializedName("discount")
	private int discount;

	@SerializedName("currency")
	private String currency;

	@SerializedName("id")
	private int id;

	@SerializedName("discount_type")
	private String discountType;

	@SerializedName("orignal_price")
	private int orignalPrice;

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setDiscount(int discount){
		this.discount = discount;
	}

	public int getDiscount(){
		return discount;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDiscountType(String discountType){
		this.discountType = discountType;
	}

	public String getDiscountType(){
		return discountType;
	}

	public void setOrignalPrice(int orignalPrice){
		this.orignalPrice = orignalPrice;
	}

	public int getOrignalPrice(){
		return orignalPrice;
	}
}
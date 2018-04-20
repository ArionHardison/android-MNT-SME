package com.tomoeats.restaurant.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Prices implements Parcelable{

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

	protected Prices(Parcel in) {
		price = in.readInt();
		discount = in.readInt();
		currency = in.readString();
		id = in.readInt();
		discountType = in.readString();
		orignalPrice = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(price);
		dest.writeInt(discount);
		dest.writeString(currency);
		dest.writeInt(id);
		dest.writeString(discountType);
		dest.writeInt(orignalPrice);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Prices> CREATOR = new Parcelable.Creator<Prices>() {
		@Override
		public Prices createFromParcel(Parcel in) {
			return new Prices(in);
		}

		@Override
		public Prices[] newArray(int size) {
			return new Prices[size];
		}
	};
}
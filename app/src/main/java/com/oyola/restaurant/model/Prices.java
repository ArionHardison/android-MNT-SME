package com.oyola.restaurant.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Prices implements Parcelable {

    @SerializedName("price")
    private int price;
    @SerializedName("discount")
    private double discount;
    @SerializedName("currency")
    private String currency;
    @SerializedName("id")
    private int id;
    @SerializedName("discount_type")
    private String discountType;
    @SerializedName("orignal_price")
    private Double orignalPrice;

    protected Prices(Parcel in) {
        price = in.readInt();
        discount = in.readDouble();
        currency = in.readString();
        id = in.readInt();
        discountType = in.readString();
        if (in.readByte() == 0) {
            orignalPrice = null;
        } else {
            orignalPrice = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeDouble(discount);
        dest.writeString(currency);
        dest.writeInt(id);
        dest.writeString(discountType);
        if (orignalPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(orignalPrice);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Prices> CREATOR = new Creator<Prices>() {
        @Override
        public Prices createFromParcel(Parcel in) {
            return new Prices(in);
        }

        @Override
        public Prices[] newArray(int size) {
            return new Prices[size];
        }
    };

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getOrignalPrice() {
        return orignalPrice;
    }

    public void setOrignalPrice(Double orignalPrice) {
        this.orignalPrice = orignalPrice;
    }

}
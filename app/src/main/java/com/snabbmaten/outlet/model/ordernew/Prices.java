package com.snabbmaten.outlet.model.ordernew;


import com.google.gson.annotations.SerializedName;


public class Prices {

    @SerializedName("price")
    private double price;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
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
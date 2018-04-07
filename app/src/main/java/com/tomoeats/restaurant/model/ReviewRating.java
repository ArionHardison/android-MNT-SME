package com.tomoeats.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRating {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_rating")
    @Expose
    private Integer userRating;
    @SerializedName("user_comment")
    @Expose
    private Object userComment;
    @SerializedName("transporter_id")
    @Expose
    private Object transporterId;
    @SerializedName("transporter_rating")
    @Expose
    private Object transporterRating;
    @SerializedName("transporter_comment")
    @Expose
    private Object transporterComment;
    @SerializedName("shop_id")
    @Expose
    private Object shopId;
    @SerializedName("shop_rating")
    @Expose
    private Object shopRating;
    @SerializedName("shop_comment")
    @Expose
    private Object shopComment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public Object getUserComment() {
        return userComment;
    }

    public void setUserComment(Object userComment) {
        this.userComment = userComment;
    }

    public Object getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(Object transporterId) {
        this.transporterId = transporterId;
    }

    public Object getTransporterRating() {
        return transporterRating;
    }

    public void setTransporterRating(Object transporterRating) {
        this.transporterRating = transporterRating;
    }

    public Object getTransporterComment() {
        return transporterComment;
    }

    public void setTransporterComment(Object transporterComment) {
        this.transporterComment = transporterComment;
    }

    public Object getShopId() {
        return shopId;
    }

    public void setShopId(Object shopId) {
        this.shopId = shopId;
    }

    public Object getShopRating() {
        return shopRating;
    }

    public void setShopRating(Object shopRating) {
        this.shopRating = shopRating;
    }

    public Object getShopComment() {
        return shopComment;
    }

    public void setShopComment(Object shopComment) {
        this.shopComment = shopComment;
    }

}
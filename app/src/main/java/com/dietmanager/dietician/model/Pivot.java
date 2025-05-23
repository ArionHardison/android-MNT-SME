package com.dietmanager.dietician.model;

/**
 * Created by Tamil on 3/16/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pivot {

    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("cuisine_id")
    @Expose
    private Integer cuisineId;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(Integer cuisineId) {
        this.cuisineId = cuisineId;
    }

}

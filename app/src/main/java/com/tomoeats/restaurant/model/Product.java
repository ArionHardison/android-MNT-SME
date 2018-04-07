package com.tomoeats.restaurant.model;

/**
 * Created by Tamil on 3/1/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("food_type")
    @Expose
    private String foodType;
    @SerializedName("cuisine_id")
    @Expose
    private Integer cuisineId;
    @SerializedName("avalability")
    @Expose
    private Integer avalability;
    @SerializedName("max_quantity")
    @Expose
    private Integer maxQuantity;
    @SerializedName("featured")
    @Expose
    private Integer featured;
    @SerializedName("addon_status")
    @Expose
    private Integer addonStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("prices")
    @Expose
    private Prices prices;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public Integer getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(Integer cuisineId) {
        this.cuisineId = cuisineId;
    }

    public Integer getAvalability() {
        return avalability;
    }

    public void setAvalability(Integer avalability) {
        this.avalability = avalability;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Integer getFeatured() {
        return featured;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }

    public Integer getAddonStatus() {
        return addonStatus;
    }

    public void setAddonStatus(Integer addonStatus) {
        this.addonStatus = addonStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Prices getPrices() {
        return prices;
    }

    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}

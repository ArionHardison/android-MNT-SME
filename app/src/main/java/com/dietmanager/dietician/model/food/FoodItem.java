package com.dietmanager.dietician.model.food;

import com.dietmanager.dietician.model.userrequest.Foodingredient;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FoodItem implements Serializable {

    @SerializedName("dietitian_id")
    private Object dietitianId;

    @SerializedName("price")
    private String price;

    @SerializedName("time_category_id")
    private String timeCategoryId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("carbohydrates")
    private String carbohydrates;

    @SerializedName("fat")
    private String fat;

    @SerializedName("protein")
    private String protein;

    @SerializedName("days")
    private int days;

    @SerializedName("id")
    private int id;
    @SerializedName("checked")
    private int checked;

    @SerializedName("avatar")
    private String avatar = "";

    @SerializedName("status")
    private String status;

    @SerializedName("who")
    private String who;

    @SerializedName("food_ingredients")
    private List<Foodingredient> food_ingredients;

    public Object getDietitianId() {
        return dietitianId;
    }

    public int getChecked() {
        return checked;
    }

    public List<Foodingredient> getFood_ingredients() {
        return food_ingredients;
    }

    public String getPrice() {
        return price;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public String getFat() {
        return fat;
    }

    public String getProtein() {
        return protein;
    }

    public String getTimeCategoryId() {
        return timeCategoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getDays() {
        return days;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStatus() {
        return status;
    }

    public String getWho() {
        return who;
    }
}
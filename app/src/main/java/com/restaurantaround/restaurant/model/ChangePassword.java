package com.restaurantaround.restaurant.model;

import com.google.gson.annotations.SerializedName;

public class ChangePassword {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
package com.dietmanager.dietician.model;

import com.dietmanager.dietician.model.subscribe.SubscribedUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamil on 9/28/2017.
 */

public class ForgotPassword {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private SubscribedUser user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SubscribedUser getUser() {
        return user;
    }

    public void setUser(SubscribedUser user) {
        this.user = user;
    }

}
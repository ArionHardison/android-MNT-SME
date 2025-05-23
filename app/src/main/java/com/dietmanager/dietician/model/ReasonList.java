package com.dietmanager.dietician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReasonList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("reason")
    @Expose
    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

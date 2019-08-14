package com.tringtwentyfour.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CancelReasons {

    @SerializedName("reason_list")
    @Expose
    private List<ReasonList> reasonList = null;

    public List<ReasonList> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<ReasonList> reasonList) {
        this.reasonList = reasonList;
    }

}

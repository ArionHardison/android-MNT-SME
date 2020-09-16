package com.oyola.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevenueCountModel {

    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("monthName")
    @Expose
    private String monthName;
    @SerializedName("count")
    @Expose
    private int count;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

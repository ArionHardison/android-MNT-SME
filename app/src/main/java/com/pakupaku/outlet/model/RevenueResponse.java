package com.pakupaku.outlet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RevenueResponse {

    @SerializedName("TotalRevenue")
    @Expose
    private int totalRevenue;
    @SerializedName("OrderReceivedToday")
    @Expose
    private Integer orderReceivedToday;
    @SerializedName("OrderDeliveredToday")
    @Expose
    private Integer orderDeliveredToday;
    @SerializedName("OrderIncomeMonthly")
    @Expose
    private int orderIncomeMonthly;
    @SerializedName("OrderIncomeToday")
    @Expose
    private int orderIncomeToday;
    @SerializedName("complete_cancel")
    @Expose
    private List<CompleteCancel> completeCancel = null;

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(int totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getOrderReceivedToday() {
        return orderReceivedToday;
    }

    public void setOrderReceivedToday(Integer orderReceivedToday) {
        this.orderReceivedToday = orderReceivedToday;
    }

    public Integer getOrderDeliveredToday() {
        return orderDeliveredToday;
    }

    public void setOrderDeliveredToday(Integer orderDeliveredToday) {
        this.orderDeliveredToday = orderDeliveredToday;
    }

    public int getOrderIncomeMonthly() {
        return orderIncomeMonthly;
    }

    public void setOrderIncomeMonthly(int orderIncomeMonthly) {
        this.orderIncomeMonthly = orderIncomeMonthly;
    }

    public int getOrderIncomeToday() {
        return orderIncomeToday;
    }

    public void setOrderIncomeToday(int orderIncomeToday) {
        this.orderIncomeToday = orderIncomeToday;
    }

    public List<CompleteCancel> getCompleteCancel() {
        return completeCancel;
    }

    public void setCompleteCancel(List<CompleteCancel> completeCancel) {
        this.completeCancel = completeCancel;
    }

}

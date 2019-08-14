package com.tringtwentyfour.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RevenueResponse {

    @SerializedName("TotalRevenue")
    @Expose
    private double totalRevenue;
    @SerializedName("OrderReceivedToday")
    @Expose
    private Integer orderReceivedToday;
    @SerializedName("OrderDeliveredToday")
    @Expose
    private Integer orderDeliveredToday;
    @SerializedName("OrderIncomeMonthly")
    @Expose
    private double orderIncomeMonthly;
    @SerializedName("OrderIncomeToday")
    @Expose
    private double orderIncomeToday;
    @SerializedName("complete_cancel")
    @Expose
    private List<CompleteCancel> completeCancel = null;

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
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

    public double getOrderIncomeMonthly() {
        return orderIncomeMonthly;
    }

    public void setOrderIncomeMonthly(double orderIncomeMonthly) {
        this.orderIncomeMonthly = orderIncomeMonthly;
    }

    public double getOrderIncomeToday() {
        return orderIncomeToday;
    }

    public void setOrderIncomeToday(double orderIncomeToday) {
        this.orderIncomeToday = orderIncomeToday;
    }

    public List<CompleteCancel> getCompleteCancel() {
        return completeCancel;
    }

    public void setCompleteCancel(List<CompleteCancel> completeCancel) {
        this.completeCancel = completeCancel;
    }
}

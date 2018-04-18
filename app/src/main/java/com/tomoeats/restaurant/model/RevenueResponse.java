
package com.tomoeats.restaurant.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevenueResponse {

    @SerializedName("TotalRevenue")
    @Expose
    private Double totalRevenue;
    @SerializedName("OrderReceivedToday")
    @Expose
    private Double orderReceivedToday;
    @SerializedName("OrderDeliveredToday")
    @Expose
    private Double orderDeliveredToday;
    @SerializedName("OrderIncomeMonthly")
    @Expose
    private Double orderIncomeMonthly;
    @SerializedName("OrderIncomeToday")
    @Expose
    private Double orderIncomeToday;
    @SerializedName("complete_cancel")
    @Expose
    private List<CompleteCancel> completeCancel = null;

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getOrderReceivedToday() {
        return orderReceivedToday;
    }

    public void setOrderReceivedToday(Double orderReceivedToday) {
        this.orderReceivedToday = orderReceivedToday;
    }

    public Double getOrderDeliveredToday() {
        return orderDeliveredToday;
    }

    public void setOrderDeliveredToday(Double orderDeliveredToday) {
        this.orderDeliveredToday = orderDeliveredToday;
    }

    public Double getOrderIncomeMonthly() {
        return orderIncomeMonthly;
    }

    public void setOrderIncomeMonthly(Double orderIncomeMonthly) {
        this.orderIncomeMonthly = orderIncomeMonthly;
    }

    public Double getOrderIncomeToday() {
        return orderIncomeToday;
    }

    public void setOrderIncomeToday(Double orderIncomeToday) {
        this.orderIncomeToday = orderIncomeToday;
    }

    public List<CompleteCancel> getCompleteCancel() {
        return completeCancel;
    }

    public void setCompleteCancel(List<CompleteCancel> completeCancel) {
        this.completeCancel = completeCancel;
    }

}

package com.oyola.restaurant.model;

import java.util.List;

public class OngoingHistoryModel {

    private String headerName;
    private List<Order> orderList;

    public OngoingHistoryModel(String headerName, List<Order> orderList) {
        this.headerName = headerName;
        this.orderList = orderList;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

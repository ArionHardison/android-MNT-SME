package com.dietmanager.restaurant.model;

import java.util.List;

public class SectionHeaderItem {

    private String sectionTitle;
    private List<Order> orderList;

    public SectionHeaderItem(String sectionTitle, List<Order> orderList) {
        this.sectionTitle = sectionTitle;
        this.orderList = orderList;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

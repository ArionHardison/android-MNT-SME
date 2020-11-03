package com.dietmanager.dietician.model;

import java.io.Serializable;

public class SpinnerItem implements Serializable {
    private String name;
    private String price;
    private int id;
    private boolean value;

    public SpinnerItem(String name, boolean value,int id,String price) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}

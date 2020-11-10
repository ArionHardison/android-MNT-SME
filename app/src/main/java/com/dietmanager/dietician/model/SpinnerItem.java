package com.dietmanager.dietician.model;

import java.io.Serializable;

public class SpinnerItem implements Serializable {
    private String name;
    private String quantity;
    private String unitType;
    private int id;
    private boolean value;

    public SpinnerItem(String name, boolean value,int id,String quantity,String unitType) {
        this.name = name;
        this.id = id;
        this.value = value;
        this.quantity = quantity;
        this.unitType = quantity;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

package com.dietmanager.dietician.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prasanth on 13-11-2019.
 */
public class ImageGallery implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cuisine_id")
    @Expose
    private Integer cuisineId;
    @SerializedName("image")
    @Expose
    private String image;
    boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(Integer cuisineId) {
        this.cuisineId = cuisineId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

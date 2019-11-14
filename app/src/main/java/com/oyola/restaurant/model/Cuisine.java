package com.oyola.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tamil on 3/16/2018.
 */

public class Cuisine {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pivot")
    @Expose
    private Pivot pivot;
    @SerializedName("image_gallery")
    @Expose
    private List<ImageGallery> imageGallery;

    private boolean selected = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<ImageGallery> getImageGallery() {
        return imageGallery;
    }

    public void setImageGallery(List<ImageGallery> imageGallery) {
        this.imageGallery = imageGallery;
    }
}
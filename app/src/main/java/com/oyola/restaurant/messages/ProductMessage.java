package com.oyola.restaurant.messages;

import java.io.File;

public class ProductMessage {
    private String strProductName = "";
    private String strProductDescription = "";
    private String strProductStatus = "";
    private String strProductOrder = "0";
    private String strProductCategory = "";
    private File productImageFile;
    private File featuredImageFile;
    private String strCuisineId = "";
    private String isFeatured = "";
    private String imageGalleryId = "";
    private String featuredGalleryId = "";
    private String imageGalleryUrl = "";
    private String featuredGalleryUrl = "";
    private String productIngredients = "";
    private String strSelectedFoodType = "";
    private String strCalorieValue = "";

    public String getStrCuisineId() {
        return strCuisineId;
    }

    public void setStrCuisineId(String strCuisineId) {
        this.strCuisineId = strCuisineId;
    }

    public String getStrProductName() {
        return strProductName;
    }

    public void setStrProductName(String strProductName) {
        this.strProductName = strProductName;
    }

    public String getStrProductDescription() {
        return strProductDescription;
    }

    public void setStrProductDescription(String strProductDescription) {
        this.strProductDescription = strProductDescription;
    }

    public String getStrProductStatus() {
        return strProductStatus;
    }

    public void setStrProductStatus(String strProductStatus) {
        this.strProductStatus = strProductStatus;
    }

    public String getStrProductOrder() {
        return strProductOrder;
    }

    public void setStrProductOrder(String strProductOrder) {
        this.strProductOrder = strProductOrder;
    }

    public String getStrProductCategory() {
        return strProductCategory;
    }

    public void setStrProductCategory(String strProductCategory) {
        this.strProductCategory = strProductCategory;
    }

    public File getProductImageFile() {
        return productImageFile;
    }

    public void setProductImageFile(File productImageFile) {
        this.productImageFile = productImageFile;
    }

    public File getFeaturedImageFile() {
        return featuredImageFile;
    }

    public void setFeaturedImageFile(File featuredImageFile) {
        this.featuredImageFile = featuredImageFile;
    }

    public String getStrSelectedFoodType() {
        return strSelectedFoodType;
    }

    public void setStrSelectedFoodType(String strSelectedFoodType) {
        this.strSelectedFoodType = strSelectedFoodType;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public String getImageGalleryId() {
        return imageGalleryId;
    }

    public void setImageGalleryId(String imageGalleryId) {
        this.imageGalleryId = imageGalleryId;
    }


    public String getFeaturedGalleryId() {
        return featuredGalleryId;
    }

    public void setFeaturedGalleryId(String featuredGalleryId) {
        this.featuredGalleryId = featuredGalleryId;
    }

    public String getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(String productIngredients) {
        this.productIngredients = productIngredients;
    }

    public String getImageGalleryUrl() {
        return imageGalleryUrl;
    }

    public void setImageGalleryUrl(String imageGalleryUrl) {
        this.imageGalleryUrl = imageGalleryUrl;
    }

    public String getFeaturedGalleryUrl() {
        return featuredGalleryUrl;
    }

    public void setFeaturedGalleryUrl(String featuredGalleryUrl) {
        this.featuredGalleryUrl = featuredGalleryUrl;
    }

    public String getStrCalorieValue() {
        return strCalorieValue;
    }

    public void setStrCalorieValue(String strCalorieValue) {
        this.strCalorieValue = strCalorieValue;
    }
}

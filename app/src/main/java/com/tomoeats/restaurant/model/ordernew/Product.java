package com.tomoeats.restaurant.model.ordernew;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Product{

	@SerializedName("featured")
	private int featured;

	@SerializedName("images")
	private List<Object> images;

	@SerializedName("max_quantity")
	private int maxQuantity;

	@SerializedName("description")
	private String description;

	@SerializedName("cuisine_id")
	private int cuisineId;

	@SerializedName("avalability")
	private int avalability;

	@SerializedName("food_type")
	private String foodType;

	@SerializedName("shop_id")
	private int shopId;

	@SerializedName("addon_status")
	private int addonStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("position")
	private int position;

	@SerializedName("prices")
	private Prices prices;

	@SerializedName("status")
	private String status;

	public void setFeatured(int featured){
		this.featured = featured;
	}

	public int getFeatured(){
		return featured;
	}

	public void setImages(List<Object> images){
		this.images = images;
	}

	public List<Object> getImages(){
		return images;
	}

	public void setMaxQuantity(int maxQuantity){
		this.maxQuantity = maxQuantity;
	}

	public int getMaxQuantity(){
		return maxQuantity;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCuisineId(int cuisineId){
		this.cuisineId = cuisineId;
	}

	public int getCuisineId(){
		return cuisineId;
	}

	public void setAvalability(int avalability){
		this.avalability = avalability;
	}

	public int getAvalability(){
		return avalability;
	}

	public void setFoodType(String foodType){
		this.foodType = foodType;
	}

	public String getFoodType(){
		return foodType;
	}

	public void setShopId(int shopId){
		this.shopId = shopId;
	}

	public int getShopId(){
		return shopId;
	}

	public void setAddonStatus(int addonStatus){
		this.addonStatus = addonStatus;
	}

	public int getAddonStatus(){
		return addonStatus;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getPosition(){
		return position;
	}

	public void setPrices(Prices prices){
		this.prices = prices;
	}

	public Prices getPrices(){
		return prices;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
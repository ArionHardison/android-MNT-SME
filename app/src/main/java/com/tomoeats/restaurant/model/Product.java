package com.tomoeats.restaurant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Product implements Parcelable{

	@SerializedName("featured")
	private int featured;

	@SerializedName("images")
	private List<Image> images;

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

	public void setImages(List<Image> images){
		this.images = images;
	}

	public List<Image> getImages(){
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

	protected Product(Parcel in) {
		featured = in.readInt();
		maxQuantity = in.readInt();
		description = in.readString();
		cuisineId = in.readInt();
		avalability = in.readInt();
		foodType = in.readString();
		shopId = in.readInt();
		addonStatus = in.readInt();
		name = in.readString();
		id = in.readInt();
		position = in.readInt();
		prices = (Prices) in.readValue(Prices.class.getClassLoader());
		status = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(featured);
		dest.writeInt(maxQuantity);
		dest.writeString(description);
		dest.writeInt(cuisineId);
		dest.writeInt(avalability);
		dest.writeString(foodType);
		dest.writeInt(shopId);
		dest.writeInt(addonStatus);
		dest.writeString(name);
		dest.writeInt(id);
		dest.writeInt(position);
		dest.writeValue(prices);
		dest.writeString(status);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

}
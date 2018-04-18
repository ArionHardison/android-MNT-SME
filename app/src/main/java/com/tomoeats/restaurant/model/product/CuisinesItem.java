package com.tomoeats.restaurant.model.product;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class CuisinesItem implements Parcelable{

	@SerializedName("name")
	private String name;

	@SerializedName("pivot")
	private Pivot pivot;

	@SerializedName("id")
	private int id;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPivot(Pivot pivot){
		this.pivot = pivot;
	}

	public Pivot getPivot(){
		return pivot;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	protected CuisinesItem(Parcel in) {
		name = in.readString();
		pivot = (Pivot) in.readValue(Pivot.class.getClassLoader());
		id = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeValue(pivot);
		dest.writeInt(id);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<CuisinesItem> CREATOR = new Parcelable.Creator<CuisinesItem>() {
		@Override
		public CuisinesItem createFromParcel(Parcel in) {
			return new CuisinesItem(in);
		}

		@Override
		public CuisinesItem[] newArray(int size) {
			return new CuisinesItem[size];
		}
	};
}
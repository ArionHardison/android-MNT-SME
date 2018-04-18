package com.tomoeats.restaurant.model.product;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Productcuisines implements Parcelable{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

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

	protected Productcuisines(Parcel in) {
		name = in.readString();
		id = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(id);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Productcuisines> CREATOR = new Parcelable.Creator<Productcuisines>() {
		@Override
		public Productcuisines createFromParcel(Parcel in) {
			return new Productcuisines(in);
		}

		@Override
		public Productcuisines[] newArray(int size) {
			return new Productcuisines[size];
		}
	};
}
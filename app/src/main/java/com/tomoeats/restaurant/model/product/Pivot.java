package com.tomoeats.restaurant.model.product;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Pivot implements Parcelable{

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("product_id")
	private int productId;

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	protected Pivot(Parcel in) {
		categoryId = in.readInt();
		productId = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(categoryId);
		dest.writeInt(productId);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Pivot> CREATOR = new Parcelable.Creator<Pivot>() {
		@Override
		public Pivot createFromParcel(Parcel in) {
			return new Pivot(in);
		}

		@Override
		public Pivot[] newArray(int size) {
			return new Pivot[size];
		}
	};
}
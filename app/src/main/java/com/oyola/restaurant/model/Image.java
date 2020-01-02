package com.oyola.restaurant.model;

/**
 * Created by Tamil on 3/16/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements Parcelable {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("image_gallery_id")
    @Expose
    private Integer imageGalleryId;
    @SerializedName("featuredimage_gallery_id")
    @Expose
    private Integer featuredImageGalleryId;


    protected Image(Parcel in) {
        url = in.readString();
        if (in.readByte() == 0) {
            position = null;
        } else {
            position = in.readInt();
        }
        if (in.readByte() == 0) {
            imageGalleryId = null;
        } else {
            imageGalleryId = in.readInt();
        }

        if (in.readByte() == 0) {
            featuredImageGalleryId = null;
        } else {
            featuredImageGalleryId = in.readInt();
        }

    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }


    public Integer getImageGalleryId() {
        return imageGalleryId;
    }

    public void setImageGalleryId(Integer imageGalleryId) {
        this.imageGalleryId = imageGalleryId;
    }


    public Integer getFeaturedImageGalleryId() {
        return featuredImageGalleryId;
    }

    public void setFeaturedImageGalleryId(Integer featuredImageGalleryId) {
        this.featuredImageGalleryId = featuredImageGalleryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        if (position == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(position);
        }
        if (imageGalleryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imageGalleryId);
        }
        if (featuredImageGalleryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(featuredImageGalleryId);
        }
    }
}
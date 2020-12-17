package com.dietmanager.dietician.model;

/**
 * Created by Tamil on 12/28/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("stripe_connect_url")
    @Expose
    private String stripeConnectUrl;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("stripe_cust_id")
    @Expose
    private String stripe_cust_id;
    @SerializedName("mobile")
    @Expose
    private String phone;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("default_banner")
    @Expose
    private String defaultBanner;
    @SerializedName("description")
    @Expose
    private String description="";
    @SerializedName("dob")
    @Expose
    private String dob="";
    @SerializedName("experience")
    @Expose
    private String experience="";
    @SerializedName("title")
    @Expose
    private String title="";
    @SerializedName("fb_link")
    @Expose
    private String fb_link="";
    @SerializedName("twitter_link")
    @Expose
    private String twitter_link="";
    @SerializedName("flickr_link")
    @Expose
    private String flickr_link="";
    @SerializedName("offer_min_amount")
    @Expose
    private Integer offerMinAmount;
    @SerializedName("offer_percent")
    @Expose
    private Integer offerPercent;
    @SerializedName("estimated_delivery_time")
    @Expose
    private Integer estimatedDeliveryTime;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("maps_address")
    @Expose
    private String mapsAddress;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("pure_veg")
    @Expose
    private Integer pureVeg;
    @SerializedName("popular")
    @Expose
    private Integer popular;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("rating_status")
    @Expose
    private Integer ratingStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("wallet_balance")
    @Expose
    private Double walletBalance;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("currency")
    @Expose
    private String currency="$";
    @SerializedName("cuisines")
    @Expose
    private List<Cuisine> cuisines = null;
    @SerializedName("timings")
    @Expose
    private List<Timing> timings = null;

    @SerializedName("country_code")
    @Expose
    private String country_code = "";

    @SerializedName("image_gallery_id")
    @Expose
    private Integer imageGalleyId ;

    @SerializedName("halal")
    @Expose
    private Integer halal ;

    @SerializedName("free_delivery")
    @Expose
    private Integer freeDelivery ;

    @SerializedName("bank")
    @Expose
    private BankDetails bank;
    @SerializedName("deliveryoption")
    @Expose
    private List<DeliveryOption> deliveryOptionList=null;
    @SerializedName("training_module")
    @Expose
    private List<TrainingModule> trainingModules=null;

    @SerializedName("image_banner_id")
    @Expose
    private Integer imageBannerId ;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDefaultBanner() {
        return defaultBanner;
    }

    public void setDefaultBanner(String defaultBanner) {
        this.defaultBanner = defaultBanner;
    }

    public String getStripe_cust_id() {
        return stripe_cust_id;
    }

    public void setStripe_cust_id(String stripe_cust_id) {
        this.stripe_cust_id = stripe_cust_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOfferMinAmount() {
        return offerMinAmount;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public void setOfferMinAmount(Integer offerMinAmount) {
        this.offerMinAmount = offerMinAmount;
    }

    public Integer getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(Integer offerPercent) {
        this.offerPercent = offerPercent;
    }

    public Integer getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Integer estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapsAddress() {
        return mapsAddress;
    }

    public void setMapsAddress(String mapsAddress) {
        this.mapsAddress = mapsAddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getPureVeg() {
        return pureVeg;
    }

    public void setPureVeg(Integer pureVeg) {
        this.pureVeg = pureVeg;
    }

    public Integer getPopular() {
        return popular;
    }

    public void setPopular(Integer popular) {
        this.popular = popular;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFb_link() {
        return fb_link;
    }

    public void setFb_link(String fb_link) {
        this.fb_link = fb_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getFlickr_link() {
        return flickr_link;
    }

    public void setFlickr_link(String flickr_link) {
        this.flickr_link = flickr_link;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(Integer ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }

    public List<Timing> getTimings() {
        return timings;
    }

    public void setTimings(List<Timing> timings) {
        this.timings = timings;
    }

    public List<DeliveryOption> getDeliveryOptionList() {
        return deliveryOptionList;
    }

    public void setDeliveryOptionList(List<DeliveryOption> deliveryOptionList) {
        this.deliveryOptionList = deliveryOptionList;
    }

    public BankDetails getBank() {
        return bank;
    }

    public void setBank(BankDetails bank) {
        this.bank = bank;
    }

    public Integer getImageGalleyId() {
        return imageGalleyId;
    }

    public void setImageGalleyId(Integer imageGalleyId) {
        this.imageGalleyId = imageGalleyId;
    }

    public Integer getHalal() {
        return halal;
    }

    public void setHalal(Integer halal) {
        this.halal = halal;
    }

    public Integer getFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(Integer freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public Integer getImageBannerId() {
        return imageBannerId;
    }

    public void setImageBannerId(Integer imageBannerId) {
        this.imageBannerId = imageBannerId;
    }

    public List<TrainingModule> getTrainingModules() {
        return trainingModules;
    }

    public String getStripeConnectUrl() {
        return stripeConnectUrl;
    }

    public void setStripeConnectUrl(String stripeConnectUrl) {
        this.stripeConnectUrl = stripeConnectUrl;
    }
    public void setTrainingModules(List<TrainingModule> trainingModules) {
        this.trainingModules = trainingModules;
    }
}



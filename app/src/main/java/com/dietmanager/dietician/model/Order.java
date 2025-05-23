package com.dietmanager.dietician.model;

import com.dietmanager.dietician.model.subscribe.SubscribedUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.dietmanager.dietician.model.ordernew.Reviewrating;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("shift_id")
    @Expose
    private Object shiftId;
    @SerializedName("user_address_id")
    @Expose
    private Integer userAddressId;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("transporter_id")
    @Expose
    private Object transporterId;
    @SerializedName("transporter_vehicle_id")
    @Expose
    private Object transporterVehicleId;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("route_key")
    @Expose
    private String routeKey;
    @SerializedName("dispute")
    @Expose
    private String dispute;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("order_otp")
    @Expose
    private Integer orderOtp;
    @SerializedName("order_ready_time")
    @Expose
    private Integer orderReadyTime;
    @SerializedName("order_ready_status")
    @Expose
    private Integer orderReadyStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("pickup_from_restaurants")
    @Expose
    private Integer pickUpRestaurant;
    @SerializedName("schedule_status")
    @Expose
    private Integer scheduleStatus;
    @SerializedName("user")
    @Expose
    private SubscribedUser user;
    @SerializedName("transporter")
    @Expose
    private Transporter transporter;
    @SerializedName("vehicles")
    @Expose
    private Vehicles vehicles;
    @SerializedName("invoice")
    @Expose
    private Invoice invoice;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("ordertiming")
    @Expose
    private List<Ordertiming> ordertiming = null;
    @SerializedName("disputes")
    @Expose
    private List<Dispute> disputes = null;
    @SerializedName("reviewrating")
    @Expose
    private Reviewrating reviewrating;

    public Date getCreatedAtDate() {
        //2020-07-10 15:18:15
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdAtDate = dateFormat.parse(createdAt);
            return createdAtDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getShiftId() {
        return shiftId;
    }

    public void setShiftId(Object shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Integer userAddressId) {
        this.userAddressId = userAddressId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Object getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(Object transporterId) {
        this.transporterId = transporterId;
    }

    public Object getTransporterVehicleId() {
        return transporterVehicleId;
    }

    public void setTransporterVehicleId(Object transporterVehicleId) {
        this.transporterVehicleId = transporterVehicleId;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getOrderOtp() {
        return orderOtp;
    }

    public void setOrderOtp(Integer orderOtp) {
        this.orderOtp = orderOtp;
    }

    public Integer getOrderReadyTime() {
        return orderReadyTime;
    }

    public void setOrderReadyTime(Integer orderReadyTime) {
        this.orderReadyTime = orderReadyTime;
    }

    public Integer getOrderReadyStatus() {
        return orderReadyStatus;
    }

    public void setOrderReadyStatus(Integer orderReadyStatus) {
        this.orderReadyStatus = orderReadyStatus;
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

    public Integer getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Integer scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public SubscribedUser getUser() {
        return user;
    }

    public void setUser(SubscribedUser user) {
        this.user = user;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public Vehicles getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Ordertiming> getOrdertiming() {
        return ordertiming;
    }

    public void setOrdertiming(List<Ordertiming> ordertiming) {
        this.ordertiming = ordertiming;
    }

    public List<Dispute> getDisputes() {
        return disputes;
    }

    public void setDisputes(List<Dispute> disputes) {
        this.disputes = disputes;
    }

    public Reviewrating getReviewrating() {
        return reviewrating;
    }

    public void setReviewrating(Reviewrating reviewrating) {
        this.reviewrating = reviewrating;
    }

    public Integer getPickUpRestaurant() {
        return pickUpRestaurant;
    }

    public void setPickUpRestaurant(Integer pickUpRestaurant) {
        this.pickUpRestaurant = pickUpRestaurant;
    }
}
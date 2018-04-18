package com.tomoeats.restaurant.model.ordernew;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Order{

	@SerializedName("reason")
	private Object reason;

	@SerializedName("note")
	private Object note;

	@SerializedName("shop")
	private Shop shop;

	@SerializedName("reviewrating")
	private Reviewrating reviewrating;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("vehicles")
	private Vehicles vehicles;

	@SerializedName("order_otp")
	private int orderOtp;

	@SerializedName("shift_id")
	private int shiftId;

	@SerializedName("invoice_id")
	private String invoiceId;

	@SerializedName("order_ready_time")
	private int orderReadyTime;

	@SerializedName("schedule_status")
	private int scheduleStatus;

	@SerializedName("id")
	private int id;

	@SerializedName("transporter_vehicle_id")
	private int transporterVehicleId;

	@SerializedName("transporter_id")
	private int transporterId;

	@SerializedName("dispute")
	private String dispute;

	@SerializedName("address")
	private Address address;

	@SerializedName("route_key")
	private String routeKey;

	@SerializedName("disputes")
	private List<DisputesItem> disputes;

	@SerializedName("shop_id")
	private int shopId;

	@SerializedName("delivery_date")
	private String deliveryDate;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("transporter")
	private Transporter transporter;

	@SerializedName("user_address_id")
	private int userAddressId;

	@SerializedName("order_ready_status")
	private int orderReadyStatus;

	@SerializedName("invoice")
	private Invoice invoice;

	@SerializedName("ordertiming")
	private List<OrdertimingItem> ordertiming;

	@SerializedName("user")
	private User user;

	@SerializedName("items")
	private List<ItemsItem> items;

	@SerializedName("status")
	private String status;

	public void setReason(Object reason){
		this.reason = reason;
	}

	public Object getReason(){
		return reason;
	}

	public void setNote(Object note){
		this.note = note;
	}

	public Object getNote(){
		return note;
	}

	public void setShop(Shop shop){
		this.shop = shop;
	}

	public Shop getShop(){
		return shop;
	}

	public void setReviewrating(Reviewrating reviewrating){
		this.reviewrating = reviewrating;
	}

	public Reviewrating getReviewrating(){
		return reviewrating;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setVehicles(Vehicles vehicles){
		this.vehicles = vehicles;
	}

	public Vehicles getVehicles(){
		return vehicles;
	}

	public void setOrderOtp(int orderOtp){
		this.orderOtp = orderOtp;
	}

	public int getOrderOtp(){
		return orderOtp;
	}

	public void setShiftId(int shiftId){
		this.shiftId = shiftId;
	}

	public int getShiftId(){
		return shiftId;
	}

	public void setInvoiceId(String invoiceId){
		this.invoiceId = invoiceId;
	}

	public String getInvoiceId(){
		return invoiceId;
	}

	public void setOrderReadyTime(int orderReadyTime){
		this.orderReadyTime = orderReadyTime;
	}

	public int getOrderReadyTime(){
		return orderReadyTime;
	}

	public void setScheduleStatus(int scheduleStatus){
		this.scheduleStatus = scheduleStatus;
	}

	public int getScheduleStatus(){
		return scheduleStatus;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTransporterVehicleId(int transporterVehicleId){
		this.transporterVehicleId = transporterVehicleId;
	}

	public int getTransporterVehicleId(){
		return transporterVehicleId;
	}

	public void setTransporterId(int transporterId){
		this.transporterId = transporterId;
	}

	public int getTransporterId(){
		return transporterId;
	}

	public void setDispute(String dispute){
		this.dispute = dispute;
	}

	public String getDispute(){
		return dispute;
	}

	public void setAddress(Address address){
		this.address = address;
	}

	public Address getAddress(){
		return address;
	}

	public void setRouteKey(String routeKey){
		this.routeKey = routeKey;
	}

	public String getRouteKey(){
		return routeKey;
	}

	public void setDisputes(List<DisputesItem> disputes){
		this.disputes = disputes;
	}

	public List<DisputesItem> getDisputes(){
		return disputes;
	}

	public void setShopId(int shopId){
		this.shopId = shopId;
	}

	public int getShopId(){
		return shopId;
	}

	public void setDeliveryDate(String deliveryDate){
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryDate(){
		return deliveryDate;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setTransporter(Transporter transporter){
		this.transporter = transporter;
	}

	public Transporter getTransporter(){
		return transporter;
	}

	public void setUserAddressId(int userAddressId){
		this.userAddressId = userAddressId;
	}

	public int getUserAddressId(){
		return userAddressId;
	}

	public void setOrderReadyStatus(int orderReadyStatus){
		this.orderReadyStatus = orderReadyStatus;
	}

	public int getOrderReadyStatus(){
		return orderReadyStatus;
	}

	public void setInvoice(Invoice invoice){
		this.invoice = invoice;
	}

	public Invoice getInvoice(){
		return invoice;
	}

	public void setOrdertiming(List<OrdertimingItem> ordertiming){
		this.ordertiming = ordertiming;
	}

	public List<OrdertimingItem> getOrdertiming(){
		return ordertiming;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setItems(List<ItemsItem> items){
		this.items = items;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
package com.dietmanager.restaurant.model;


import com.google.gson.annotations.SerializedName;


public class Invoice {

    @SerializedName("payment_mode")
    private String paymentMode;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("gross")
    private double gross;

    @SerializedName("discount")
    private double discount;

    @SerializedName("promocode_amount")
    private double promocode_amount;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("tax")
    private double tax;

    @SerializedName("total_pay")
    private double totalPay;

    @SerializedName("ripple_price")
    private String ripplePrice;

    @SerializedName("delivery_charge")
    private double deliveryCharge;

    @SerializedName("SGST")
    private double sGST;

    @SerializedName("payable")
    private double payable;

    @SerializedName("payment_id")
    private String paymentId;

    @SerializedName("paid")
    private Integer paid;

    @SerializedName("tender_pay")
    private double tenderPay;

    @SerializedName("CGST")
    private double cGST;

    @SerializedName("id")
    private int id;

    @SerializedName("net")
    private double net;

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("wallet_amount")
    private double walletAmount;

    @SerializedName("status")
    private String status;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getGross() {
        return gross;
    }

    public void setGross(int gross) {
        this.gross = gross;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPromocode_amount() {
        return promocode_amount;
    }

    public void setPromocode_amount(double promocode_amount) {
        this.promocode_amount = promocode_amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public String getRipplePrice() {
        return ripplePrice;
    }

    public void setRipplePrice(String ripplePrice) {
        this.ripplePrice = ripplePrice;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public double getSGST() {
        return sGST;
    }

    public void setSGST(int sGST) {
        this.sGST = sGST;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(int payable) {
        this.payable = payable;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public double getTenderPay() {
        return tenderPay;
    }

    public void setTenderPay(int tenderPay) {
        this.tenderPay = tenderPay;
    }

    public double getCGST() {
        return cGST;
    }

    public void setCGST(int cGST) {
        this.cGST = cGST;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNet() {
        return net;
    }

    public void setNet(int net) {
        this.net = net;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(int walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
package com.pakupaku.outlet.model;


import com.google.gson.annotations.SerializedName;


public class Invoice {

    @SerializedName("payment_mode")
    private String paymentMode;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("gross")
    private int gross;

    @SerializedName("discount")
    private int discount;

    @SerializedName("promocode_amount")
    private int promocode_amount;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("tax")
    private int tax;

    @SerializedName("total_pay")
    private int totalPay;

    @SerializedName("ripple_price")
    private String ripplePrice;

    @SerializedName("delivery_charge")
    private int deliveryCharge;

    @SerializedName("SGST")
    private int sGST;

    @SerializedName("payable")
    private int payable;

    @SerializedName("payment_id")
    private String paymentId;

    @SerializedName("paid")
    private int paid;

    @SerializedName("tender_pay")
    private int tenderPay;

    @SerializedName("CGST")
    private int cGST;

    @SerializedName("id")
    private int id;

    @SerializedName("net")
    private int net;

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("wallet_amount")
    private int walletAmount;

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

    public int getGross() {
        return gross;
    }

    public void setGross(int gross) {
        this.gross = gross;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getPromocode_amount() {
        return promocode_amount;
    }

    public void setPromocode_amount(int promocode_amount) {
        this.promocode_amount = promocode_amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getTotalPay() {
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

    public int getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public int getSGST() {
        return sGST;
    }

    public void setSGST(int sGST) {
        this.sGST = sGST;
    }

    public int getPayable() {
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

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getTenderPay() {
        return tenderPay;
    }

    public void setTenderPay(int tenderPay) {
        this.tenderPay = tenderPay;
    }

    public int getCGST() {
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

    public int getNet() {
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

    public int getWalletAmount() {
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
package com.tomoeats.restaurant.model;

/**
 * Created by Tamil on 3/16/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("paid")
    @Expose
    private Integer paid;
    @SerializedName("gross")
    @Expose
    private Double gross;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("delivery_charge")
    @Expose
    private Double deliveryCharge;
    @SerializedName("wallet_amount")
    @Expose
    private Integer walletAmount;
    @SerializedName("payable")
    @Expose
    private Integer payable;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("net")
    @Expose
    private Double net;
    @SerializedName("total_pay")
    @Expose
    private Integer totalPay;
    @SerializedName("tender_pay")
    @Expose
    private Integer tenderPay;
    @SerializedName("ripple_price")
    @Expose
    private String ripplePrice;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("CGST")
    @Expose
    private String cGST;
    @SerializedName("SGST")
    @Expose
    private String sGST;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Double getGross() {
        return gross;
    }

    public void setGross(Double gross) {
        this.gross = gross;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Integer getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Integer walletAmount) {
        this.walletAmount = walletAmount;
    }

    public Integer getPayable() {
        return payable;
    }

    public void setPayable(Integer payable) {
        this.payable = payable;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getNet() {
        return net;
    }

    public void setNet(Double net) {
        this.net = net;
    }

    public Integer getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Integer totalPay) {
        this.totalPay = totalPay;
    }

    public Integer getTenderPay() {
        return tenderPay;
    }

    public void setTenderPay(Integer tenderPay) {
        this.tenderPay = tenderPay;
    }

    public String getRipplePrice() {
        return ripplePrice;
    }

    public void setRipplePrice(String ripplePrice) {
        this.ripplePrice = ripplePrice;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCGST() {
        return cGST;
    }

    public void setCGST(String cGST) {
        this.cGST = cGST;
    }

    public String getSGST() {
        return sGST;
    }

    public void setSGST(String sGST) {
        this.sGST = sGST;
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

}
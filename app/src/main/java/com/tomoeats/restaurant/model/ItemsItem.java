package com.tomoeats.restaurant.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ItemsItem{

	@SerializedName("note")
	private Object note;

	@SerializedName("product")
	private Product product;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("savedforlater")
	private int savedforlater;

	@SerializedName("price")
	private int price;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("promocode_id")
	private Object promocodeId;

	@SerializedName("cart_addons")
	private List<Object> cartAddons;

	public void setNote(Object note){
		this.note = note;
	}

	public Object getNote(){
		return note;
	}

	public void setProduct(Product product){
		this.product = product;
	}

	public Product getProduct(){
		return product;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setSavedforlater(int savedforlater){
		this.savedforlater = savedforlater;
	}

	public int getSavedforlater(){
		return savedforlater;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	public void setPromocodeId(Object promocodeId){
		this.promocodeId = promocodeId;
	}

	public Object getPromocodeId(){
		return promocodeId;
	}

	public void setCartAddons(List<Object> cartAddons){
		this.cartAddons = cartAddons;
	}

	public List<Object> getCartAddons(){
		return cartAddons;
	}
}
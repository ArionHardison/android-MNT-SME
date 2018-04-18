package com.tomoeats.restaurant.model.ordernew;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class OrderResponse{

	@SerializedName("Order")
	private Order order;

	@SerializedName("Cart")
	private List<CartItem> cart;

	public void setOrder(Order order){
		this.order = order;
	}

	public Order getOrder(){
		return order;
	}

	public void setCart(List<CartItem> cart){
		this.cart = cart;
	}

	public List<CartItem> getCart(){
		return cart;
	}
}
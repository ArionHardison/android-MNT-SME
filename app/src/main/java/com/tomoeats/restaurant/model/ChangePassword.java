package com.tomoeats.restaurant.model;

import com.google.gson.annotations.SerializedName;

public class ChangePassword {

	@SerializedName("message")
	private String message;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

}
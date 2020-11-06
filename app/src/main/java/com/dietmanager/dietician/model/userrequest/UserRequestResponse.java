package com.dietmanager.dietician.model.userrequest;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserRequestResponse implements Serializable {

	@SerializedName("userRequest")
	private List<UserRequestItem> userRequest;

	public List<UserRequestItem> getUserRequest(){
		return userRequest;
	}
}
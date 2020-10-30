package com.dietmanager.dietician.model.subscribe;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SubscribelistResponse{

	@SerializedName("subscribe")
	private List<SubscribeItem> subscribe;

	public List<SubscribeItem> getSubscribe(){
		return subscribe;
	}
}
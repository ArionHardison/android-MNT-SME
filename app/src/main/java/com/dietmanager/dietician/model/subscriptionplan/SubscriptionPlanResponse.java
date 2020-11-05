package com.dietmanager.dietician.model.subscriptionplan;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SubscriptionPlanResponse{

	@SerializedName("SubscriptionPlan")
	private List<SubscriptionPlanItem> subscriptionPlan;

	public List<SubscriptionPlanItem> getSubscriptionPlan(){
		return subscriptionPlan;
	}
}
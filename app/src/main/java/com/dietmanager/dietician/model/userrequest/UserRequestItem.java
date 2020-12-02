package com.dietmanager.dietician.model.userrequest;

import java.io.Serializable;
import java.util.List;

import com.dietmanager.dietician.model.CustomerAddress;
import com.google.gson.annotations.SerializedName;

public class UserRequestItem implements Serializable {

	@SerializedName("schedule_at")
	private String  scheduleAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("payment_mode")
	private String payment_mode;

	public String getPaymentMode() {
		return payment_mode;
	}

	@SerializedName("discount")
	private String discount;
	@SerializedName("customer_address")
	private CustomerAddress customerAddress;

	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}
	@SerializedName("orderingredient")
	private List<OrderingredientItem> orderingredient;

	@SerializedName("dietitian")
	private Dietitian dietitian;

	@SerializedName("food_id")
	private int foodId;

	@SerializedName("food")
	private Food food;

	@SerializedName("user")
	private User user;

	@SerializedName("dietitian_rating")
	private int dietitianRating;

	@SerializedName("dietitian_id")
	private int dietitianId;

	@SerializedName("chef_rating")
	private int chefRating;

	@SerializedName("total")
	private String total;

	@SerializedName("chef_id")
	private Object chefId;

	@SerializedName("payable")
	private String payable;
	@SerializedName("tax")
	private String tax;

	public String getTax() {
		return tax;
	}

	@SerializedName("user_id")
	private int userId;

	@SerializedName("id")
	private int id;

	@SerializedName("is_scheduled")
	private int isScheduled;

	@SerializedName("status")
	private String status;

	public String getCreatedAt() {
		return createdAt;
	}

	public String getScheduleAt(){
		return scheduleAt;
	}

	public String getDiscount(){
		return discount;
	}

	public List<OrderingredientItem> getOrderingredient(){
		return orderingredient;
	}

	public Dietitian getDietitian(){
		return dietitian;
	}

	public int getFoodId(){
		return foodId;
	}

	public Food getFood(){
		return food;
	}

	public int getDietitianRating(){
		return dietitianRating;
	}

	public int getDietitianId(){
		return dietitianId;
	}

	public int getChefRating(){
		return chefRating;
	}

	public String getTotal(){
		return total;
	}

	public Object getChefId(){
		return chefId;
	}

	public String getPayable(){
		return payable;
	}

	public int getUserId(){
		return userId;
	}

	public int getId(){
		return id;
	}

	public int getIsScheduled(){
		return isScheduled;
	}

	public User getUser() {
		return user;
	}

	public String getStatus(){
		return status;
	}
}
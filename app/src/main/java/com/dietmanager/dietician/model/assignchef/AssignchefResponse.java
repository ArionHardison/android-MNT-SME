package com.dietmanager.dietician.model.assignchef;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AssignchefResponse{

	@SerializedName("assignChef")
	private List<AssignChefItem> assignChef;

	public void setAssignChef(List<AssignChefItem> assignChef){
		this.assignChef = assignChef;
	}

	public List<AssignChefItem> getAssignChef(){
		return assignChef;
	}
}
package com.dietmanager.dietician.model.initeduser;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class InvitedUserResponse{

	@SerializedName("invitedUser")
	private List<InvitedUserItem> invitedUser;

	public void setInvitedUser(List<InvitedUserItem> invitedUser){
		this.invitedUser = invitedUser;
	}

	public List<InvitedUserItem> getInvitedUser(){
		return invitedUser;
	}
}
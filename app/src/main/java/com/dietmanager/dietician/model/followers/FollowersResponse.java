package com.dietmanager.dietician.model.followers;

import com.google.gson.annotations.SerializedName;

public class FollowersResponse{

	@SerializedName("followers")
	private Followers followers;

	public void setFollowers(Followers followers){
		this.followers = followers;
	}

	public Followers getFollowers(){
		return followers;
	}
}
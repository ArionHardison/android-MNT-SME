package com.dietmanager.restaurant.controller;

import com.dietmanager.restaurant.model.Profile;

/**
 * Created by Tamil on 3/16/2018.
 */

public interface ProfileListener {

    void onSuccess(Profile profile);

    void onFailure(String error);
}

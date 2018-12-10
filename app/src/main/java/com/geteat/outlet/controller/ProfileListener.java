package com.geteat.outlet.controller;

import com.geteat.outlet.model.Profile;

/**
 * Created by Tamil on 3/16/2018.
 */

public interface ProfileListener {

    void onSuccess(Profile profile);

    void onFailure(String error);
}

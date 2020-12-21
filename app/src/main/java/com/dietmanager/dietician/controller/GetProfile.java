package com.dietmanager.dietician.controller;

import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.dietmanager.dietician.adapter.AppConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.dietician.application.MyApplication;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Constants;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tamil on 3/16/2018.
 */

public class GetProfile {

    public GetProfile(ApiInterface api, final ProfileListener profileListener) {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);;
        String device_id = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        String device_type = "android";
        String device_token = SharedHelper.getKey(MyApplication.getInstance(), "device_token");

        HashMap<String, String> params = new HashMap<>();
        params.put("device_id", device_id);
        params.put("device_type", device_type);
        params.put("device_token", device_token);
        Call<Profile> call = apiInterface.getProfile(params);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.isSuccessful()) {
                    String stripeUrl = response.body() != null && !TextUtils.isEmpty(response.body().getStripeConnectUrl()) ? response.body().getStripeConnectUrl() : "";
                    SharedHelper.putKey(MyApplication.getInstance(), AppConstants.STRIPE_URL, stripeUrl);
                    SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.PROFILE_ID, "" + response.body().getId());
                    SharedHelper.putKey(MyApplication.getInstance(), Constants.PREF.CURRENCY, "" + response.body().getCurrency());
                    profileListener.onSuccess(response.body());
                } else try {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    profileListener.onFailure(serverError.getError());
                } catch (JsonSyntaxException e) {
                    profileListener.onFailure("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                profileListener.onFailure("");
            }
        });
    }
}

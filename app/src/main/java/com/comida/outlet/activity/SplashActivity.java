package com.comida.outlet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.comida.outlet.R;
import com.comida.outlet.controller.GetProfile;
import com.comida.outlet.controller.ProfileListener;
import com.comida.outlet.helper.ConnectionHelper;
import com.comida.outlet.helper.CustomDialog;
import com.comida.outlet.helper.GlobalData;
import com.comida.outlet.helper.SharedHelper;
import com.comida.outlet.model.Profile;
import com.comida.outlet.network.ApiClient;
import com.comida.outlet.network.ApiInterface;
import com.comida.outlet.utils.Utils;

public class SplashActivity extends AppCompatActivity implements ProfileListener {

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        context = SplashActivity.this;
        activity = SplashActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        GlobalData.accessToken = SharedHelper.getKey(context, "access_token");

        getDeviceToken();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedHelper.getKey(context, "logged").equalsIgnoreCase("true")
                        && SharedHelper.getKey(context, "logged") != null) {
                    if (connectionHelper.isConnectingToInternet())
                        new GetProfile(apiInterface, SplashActivity.this);
                    else
                        Utils.displayMessage(SplashActivity.this, getString(R.string.oops_no_internet));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }, 3000);

    }


    public void getDeviceToken() {
        try {
            if (!SharedHelper.getKey(context, "device_token").equals("")
                    && !SharedHelper.getKey(context, "device_token").equals("null")) {
                device_token = SharedHelper.getKey(context, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = "" + FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(context, "device_token", "" + device_token);
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    @Override
    public void onSuccess(Profile profile) {
        GlobalData.profile = profile;
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        if (error.isEmpty())
            Utils.displayMessage(activity, getString(R.string.something_went_wrong));
        else
            Utils.displayMessage(activity, getString(R.string.something_went_wrong));

        SharedHelper.putKey(context, "logged", "false");
        startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}

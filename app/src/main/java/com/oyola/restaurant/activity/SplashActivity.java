package com.oyola.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.oyola.restaurant.R;
import com.oyola.restaurant.controller.GetProfile;
import com.oyola.restaurant.controller.ProfileListener;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.helper.SharedHelper;
import com.oyola.restaurant.model.Profile;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.LocaleUtils;
import com.oyola.restaurant.utils.Utils;

import java.util.Objects;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        context = SplashActivity.this;
        activity = SplashActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        GlobalData.accessToken = SharedHelper.getKey(context, "access_token");

        String dd = SharedHelper.getKey(context, "language");
        switch (dd) {
            case "English":
                LocaleUtils.setLocale(context, "en");
                break;
            case "Japanese":
                LocaleUtils.setLocale(context, "ja");
                break;
            default:
                LocaleUtils.setLocale(context, "en");
                break;
        }


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
                        Utils.displayMessage(SplashActivity.this,
                                getString(R.string.oops_no_internet));
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
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        return;
                    }
                    device_token = ""+ Objects.requireNonNull(task.getResult()).getToken();
                    SharedHelper.putKey(context, "device_token",  device_token);
                });
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
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
        if (profile.getBank()!=null) {
            startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }else {
            startActivity(new Intent(context, BankDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
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
        startActivity(new Intent(SplashActivity.this, RegisterActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}

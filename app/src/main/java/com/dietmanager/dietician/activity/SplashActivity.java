package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.controller.GetProfile;
import com.dietmanager.dietician.controller.ProfileListener;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.LocaleUtils;
import com.dietmanager.dietician.utils.Utils;

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

    private void checkActivity() {
        Intent intent = getIntent();
        if (intent.getStringExtra("page") == null || intent.getStringExtra("page").equalsIgnoreCase("main")) {
            startActivity(new Intent(context, DietitianMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            String page=intent.getStringExtra("page");
            if (page.equalsIgnoreCase("order")) {
                        startActivity(new Intent(context, UserRequestActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else if (page.equalsIgnoreCase("follow")||page.equalsIgnoreCase("unfollow")) {
                startActivity(new Intent(context, FollowersActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }  else if (page.equalsIgnoreCase("subscription")) {
                startActivity(new Intent(context, SubscribedMembersActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }  else if (page.equalsIgnoreCase("wallet")) {
                startActivity(new Intent(context, PaymentActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else if (page.equalsIgnoreCase("invite")) {
                startActivity(new Intent(context, InvitedUserActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                startActivity(new Intent(context, DietitianMainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }

        finishAffinity();
    }

    @Override
    public void onSuccess(Profile profile) {
        GlobalData.profile = profile;
/*        //if (profile.getBank()!=null) {
            startActivity(new Intent(context, DietitianMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        //}else {
            //startActivity(new Intent(context, BankDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        //}
        finish();*/
        checkActivity();
    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        if (error!=null&&error.isEmpty())
            Utils.displayMessage(activity, getString(R.string.something_went_wrong));
        else
            Utils.displayMessage(activity, getString(R.string.something_went_wrong));

        SharedHelper.putKey(context, "logged", "false");
        startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}

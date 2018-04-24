package com.tomoeats.restaurant.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import com.crashlytics.android.Crashlytics;
import com.tomoeats.restaurant.helper.SharedHelper;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Tamil on 3/17/2018.
 */


public class MyApplication extends MultiDexApplication {

    private static MyApplication mAppController;
    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;

    public static MyApplication getInstance() {
        return mAppController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mAppController = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static NumberFormat getNumberFormat() {
        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", "INR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        numberFormat.setCurrency(Currency.getInstance(currencyCode));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }
}

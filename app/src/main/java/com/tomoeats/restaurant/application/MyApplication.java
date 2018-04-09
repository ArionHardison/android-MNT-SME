package com.tomoeats.restaurant.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.tomoeats.restaurant.helper.SharedHelper;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Tamil on 3/17/2018.
 */


public class MyApplication extends Application {

    private static MyApplication mAppController;
    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;

    public static MyApplication getInstance() {
        return mAppController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
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

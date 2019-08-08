package com.dalejoak.restaurant.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.dalejoak.restaurant.helper.GlobalData;
import com.dalejoak.restaurant.helper.SharedHelper;
import com.facebook.stetho.Stetho;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
//import com.facebook.stetho.Stetho;

/**
 * Created by Tamil on 3/17/2018.
 */


public class MyApplication extends MultiDexApplication {

    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;
    private static MyApplication mAppController;

    public static MyApplication getInstance() {
        return mAppController;
    }

    public static NumberFormat getNumberFormat() {
//        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", "INR");
        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", GlobalData.profile.getCurrency());

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        numberFormat.setCurrency(Currency.getInstance(currencyCode));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Stetho.initializeWithDefaults(this);

        mAppController = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}

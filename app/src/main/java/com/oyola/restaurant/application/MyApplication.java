package com.oyola.restaurant.application;

import android.content.Context;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.helper.SharedHelper;
import com.facebook.stetho.Stetho;
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Stetho.initializeWithDefaults(this);

        mAppController = this;
        UnsplashPhotoPicker.INSTANCE.init(this,
                "0813811a510708005bed659afd6c652e6ef32ad72df534d37598dcd05f46af35",
                "42dc66500397d66972dea4952edb76699cf6f9c8824dba27df1354bc1bfdaa50",20);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static NumberFormat getNumberFormat() {
//        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", "INR");
        String currencyCode = SharedHelper.getKey(getInstance(), "currencyCode", GlobalData.profile.getCurrency());

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        numberFormat.setCurrency(Currency.getInstance(currencyCode));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }

    public static String getCurrencyFormat() {
        String currencyCode = SharedHelper.getKey(getInstance(), "currency_code", GlobalData.profile.getCurrency());
        return currencyCode;
    }

}

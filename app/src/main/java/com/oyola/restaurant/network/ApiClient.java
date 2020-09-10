package com.oyola.restaurant.network;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.oyola.restaurant.activity.RestaurantTimingActivity;
import com.oyola.restaurant.application.MyApplication;
import com.oyola.restaurant.config.AppConfigure;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.helper.SharedHelper;
import com.oyola.restaurant.utils.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tamil on 30-08-2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        OkHttpClient client = getClient();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfigure.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(new AddHeaderInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .build();
        client.connectionPool().evictAll();
        return client;
    }


    public static class AddHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("X-Requested-With", "XMLHttpRequest");
            String accessToken = SharedHelper.getKey(MyApplication.getInstance(), "access_token");
            if (!TextUtils.isEmpty(accessToken)) {
                builder.addHeader("Authorization", "Bearer " + accessToken);
                Log.e("access_token", accessToken);
            }

            return chain.proceed(builder.build());
        }
    }

}

package com.dietmanager.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.adapter.AppConstants;
import com.dietmanager.restaurant.application.MyApplication;
import com.dietmanager.restaurant.config.AppConfigure;
import com.dietmanager.restaurant.controller.GetProfile;
import com.dietmanager.restaurant.controller.ProfileListener;
import com.dietmanager.restaurant.helper.ConnectionHelper;
import com.dietmanager.restaurant.helper.CustomDialog;
import com.dietmanager.restaurant.helper.GlobalData;
import com.dietmanager.restaurant.helper.SharedHelper;
import com.dietmanager.restaurant.model.AuthToken;
import com.dietmanager.restaurant.model.Profile;
import com.dietmanager.restaurant.model.ServerError;
import com.dietmanager.restaurant.network.ApiClient;
import com.dietmanager.restaurant.network.ApiInterface;
import com.dietmanager.restaurant.utils.TextUtils;
import com.dietmanager.restaurant.utils.Utils;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dietmanager.restaurant.utils.TextUtils.isValidEmail;

public class LoginActivity extends AppCompatActivity implements ProfileListener {

    @BindView(R.id.txt_forgot_password)
    TextView txtForgotPassword;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_eye_img)
    ImageView etPasswordEyeImg;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.txt_register)
    TextView txtRegister;
    @BindView(R.id.bottom_lay)
    LinearLayout bottomLay;
    @BindView(R.id.tv_terms_policy)
    TextView tvTermsAndPolicy;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternetAvailable;
    ApiInterface apiInterface;
    String TAG = "LoginActivity";
    String email, password;
    private String deviceToken;
    private String deviceId;

    private void addLink(TextView textView, String patternToMatch,
                         final String link) {
        Linkify.TransformFilter filter = (match, url) -> link;
        Linkify.addLinks(textView, Pattern.compile(patternToMatch), null, null,
                filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tvTermsAndPolicy.setText(getString(R.string.login_terms_privacy_policy));
        addLink(tvTermsAndPolicy, getString(R.string.login_terms_and_conditions_label), getString(R.string.login_terms_and_conditions_url));
        addLink(tvTermsAndPolicy, getString(R.string.login_privacy_policy_label), getString(R.string.login_privacy_policy_url));

        MyApplication.getInstance().fetchDeviceToken();
        context = LoginActivity.this;
        activity = LoginActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        etPasswordEyeImg.setTag(1);
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedHelper.putKey(context, "access_token", "");
        GlobalData.accessToken = "";
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    }

    @OnClick({R.id.et_password_eye_img, R.id.login_btn, R.id.txt_register, R.id.txt_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_password_eye_img:
                if (etPasswordEyeImg.getTag().equals(1)) {
                    etPassword.setTransformationMethod(null);
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etPasswordEyeImg.setTag(0);
                } else {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etPasswordEyeImg.setTag(1);
                }
                break;
            case R.id.login_btn:
                validateLogin();
                break;
            case R.id.txt_register:
                startActivity(new Intent(context, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                finish();
                break;
            case R.id.txt_forgot_password:
                startActivity(new Intent(context, ForgotPassword.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }

    private void validateLogin() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        if (email.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_mail_id));
        else if (!isValidEmail(email))
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_valid_mail_id));
        else if (password.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_password));
        else {
            isInternetAvailable = connectionHelper.isConnectingToInternet();
            if (isInternetAvailable) {
                deviceToken = SharedHelper.getKey(context, "device_token");
                HashMap<String, String> map = new HashMap<>();
                map.put("device_id", deviceId);
                map.put("device_token", deviceToken);
                map.put("device_type", AppConstants.DEVICE_TYPE);
                map.put("username", email);
                map.put("password", password);
                map.put("grant_type", "password");
                map.put("client_id", AppConfigure.CLIENT_ID);
                map.put("client_secret", AppConfigure.CLIENT_SECRET);
                map.put("guard", "shops");
                login(map);

                //["username": "ios@demo.com", "guard": "shops", "password": "123456", "grant_type": "password",
                // "client_secret": "aCaCS0Kf2MArewwGDgHqfTz9q4U3GRqZwgK1LR70", "client_id": "2",
                // "device_id": "EC3BFD0D-2007-4BDF-99DC-817030ACEC48", "device_type": "ios",
                // "device_token": "0e72accd48569a70aeb131d178fc72d54a835434c57a070fdc4871eb78c72c50"]
            } else {
                Utils.displayMessage(activity, getResources().getString(R.string.oops_no_internet));
            }

        }
    }

    private void login(HashMap<String, String> map) {
        customDialog.show();
        Call<AuthToken> call = apiInterface.login(map);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful()) {
                    SharedHelper.putKey(context, "access_token", response.body().getAccessToken());
                    GlobalData.accessToken = SharedHelper.getKey(context, "access_token");
                    new GetProfile(apiInterface, LoginActivity.this);
                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        if (serverError.getError() != null && serverError.getError().toLowerCase().contains("invalid")) {
                            Utils.displayMessage(activity, getString(R.string.invalid_credentials));
                        } else {
                            Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                        }

                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }

                    customDialog.dismiss();
                }
                //customDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable throwable) {
                customDialog.dismiss();
                String message = !TextUtils.isEmpty(Objects.requireNonNull(throwable.getMessage())) ? throwable.getMessage() : getString(R.string.something_went_wrong);
                Utils.displayMessage(activity, message);
            }
        });

    }

    @Override
    public void onSuccess(Profile profile) {
        customDialog.dismiss();
        SharedHelper.putKey(context, "logged", "true");
        GlobalData.profile = profile;
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        String message = !TextUtils.isEmpty(error) ? error : getString(R.string.something_went_wrong);
        Utils.displayMessage(activity, message);
    }
}

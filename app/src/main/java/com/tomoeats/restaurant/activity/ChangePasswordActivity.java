package com.tomoeats.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.helper.ConnectionHelper;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends AppCompatActivity {


    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_current_password)
    EditText etCurrentPassword;
    @BindView(R.id.et_current_password_eye_img)
    ImageView etCurrentPasswordEyeImg;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_eye_img)
    ImageView etPasswordEyeImg;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.et_confirm_password_eye_img)
    ImageView etConfirmPasswordEyeImg;
    @BindView(R.id.save_btn)
    Button saveBtn;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG="ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        context = ChangePasswordActivity.this;
        activity = ChangePasswordActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        backImg.setVisibility(View.VISIBLE);
        title.setText(getString(R.string.change_password));

        etPasswordEyeImg.setTag(0);
        etCurrentPasswordEyeImg.setTag(0);
        etConfirmPasswordEyeImg.setTag(0);


    }

    @OnClick({R.id.back_img, R.id.et_current_password_eye_img, R.id.et_password_eye_img, R.id.et_confirm_password_eye_img, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.et_current_password_eye_img:
                if (etCurrentPasswordEyeImg.getTag().equals(1)) {
                    etConfirmPassword.setTransformationMethod(null);
                    etCurrentPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etCurrentPasswordEyeImg.setTag(0);
                } else {
                    etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etCurrentPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etCurrentPasswordEyeImg.setTag(1);
                }

                break;
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
            case R.id.et_confirm_password_eye_img:
                if (etConfirmPasswordEyeImg.getTag().equals(1)) {
                    etConfirmPassword.setTransformationMethod(null);
                    etConfirmPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etConfirmPasswordEyeImg.setTag(0);
                } else {
                    etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etConfirmPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etConfirmPasswordEyeImg.setTag(1);
                }
                break;
            case R.id.save_btn:
                onBackPressed();
                break;
        }
    }
}

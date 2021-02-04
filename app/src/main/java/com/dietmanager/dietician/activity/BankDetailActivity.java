package com.dietmanager.dietician.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.controller.GetProfile;
import com.dietmanager.dietician.controller.ProfileListener;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasanth on 14-11-2019.
 */
public class BankDetailActivity extends AppCompatActivity implements ProfileListener {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edit_bank_name)
    EditText mEdtBankName;
    @BindView(R.id.edit_acc_number)
    EditText mEdtAccountNo;
    @BindView(R.id.edit_holder_name)
    EditText mEdtHolderName;
    @BindView(R.id.edit_routing_no)
    EditText mEdtRoutingNumber;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private CustomDialog customDialog;
    Integer mId = 0;
    String strFrom = "Register";
    ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);
        connectionHelper = new ConnectionHelper(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("from")) {
            strFrom = bundle.getString("from");
        }
        if (strFrom.equalsIgnoreCase("Settings")) {
            title.setText(getResources().getString(R.string.edit_bank));
            backImg.setVisibility(View.VISIBLE);
            callProfile();
        } else {
            title.setText(getResources().getString(R.string.bank_details));
            backImg.setVisibility(View.GONE);
            if (GlobalData.profile != null) {
                mId = GlobalData.profile.getId();
            }
        }
    }

    @OnClick({R.id.btnSubmit, R.id.back_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.btnSubmit:
                validateDetails();
                break;
        }
    }

    private void callProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            showLoading();
            new GetProfile(apiInterface, this);
        } else {
            Utils.displayMessage(this, getResources().getString(R.string.oops_no_internet));
        }
    }

    private void validateDetails() {
        if (mEdtBankName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_bank_name), Toast.LENGTH_SHORT).show();
        } else if (mEdtAccountNo.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_account_number), Toast.LENGTH_SHORT).show();
        } else if (mEdtHolderName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_holder_name), Toast.LENGTH_SHORT).show();
        } else if (mEdtRoutingNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_routing_number), Toast.LENGTH_SHORT).show();
        } else {
            updateBankDetails();
        }
    }

    private void updateBankDetails() {
        showLoading();
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("bank_name", RequestBody.create(MediaType.parse("text/plain"), mEdtBankName.getText().toString()));
        map.put("account_number", RequestBody.create(MediaType.parse("text/plain"), mEdtAccountNo.getText().toString()));
        map.put("holder_name", RequestBody.create(MediaType.parse("text/plain"), mEdtHolderName.getText().toString()));
        map.put("routing_number", RequestBody.create(MediaType.parse("text/plain"), mEdtRoutingNumber.getText().toString()));

        Call<Profile> call = apiInterface.updateProfile(mId, map);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                hideLoading();
                if (response.body() != null) {
                    Utils.displayMessage(BankDetailActivity.this, getString(R.string.bank_details_updated));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            onBackPressed();
                            startActivity(new Intent(BankDetailActivity.this, DietitianMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    }, 1000);

                } else {
                    if (response.code() == 401) {
                        startActivity(new Intent(BankDetailActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                    Utils.displayMessage(BankDetailActivity.this, getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                hideLoading();
                Utils.displayMessage(BankDetailActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

    private void showLoading() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this);
        }
        customDialog.show();
    }

    private void hideLoading() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    @Override
    public void onSuccess(Profile profile) {
        hideLoading();
        mId = profile.getId();
        if (profile.getBank() != null) {
            mEdtBankName.setText(profile.getBank().getBankName());
            mEdtAccountNo.setText(profile.getBank().getAccountNumber());
            mEdtHolderName.setText(profile.getBank().getHolderName());
            mEdtRoutingNumber.setText(profile.getBank().getRoutingNumber());
        }
    }

    @Override
    public void onFailure(String error) {
        hideLoading();
        if (error!=null&&error.isEmpty())
            Utils.displayMessage(BankDetailActivity.this, getString(R.string.something_went_wrong));
        else Utils.displayMessage(BankDetailActivity.this, error);
    }
}

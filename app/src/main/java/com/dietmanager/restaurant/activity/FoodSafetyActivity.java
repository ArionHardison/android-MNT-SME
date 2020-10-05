package com.dietmanager.restaurant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.adapter.DocumentAdapter;
import com.dietmanager.restaurant.controller.GetProfile;
import com.dietmanager.restaurant.controller.ProfileListener;
import com.dietmanager.restaurant.helper.ConnectionHelper;
import com.dietmanager.restaurant.helper.CustomDialog;
import com.dietmanager.restaurant.model.Profile;
import com.dietmanager.restaurant.network.ApiClient;
import com.dietmanager.restaurant.network.ApiInterface;
import com.dietmanager.restaurant.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth on 21-01-2020
 */
public class FoodSafetyActivity extends AppCompatActivity implements ProfileListener {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.txt_error)
    TextView txtError;
    @BindView(R.id.rv_documents)
    RecyclerView mRVDocuments;

    CustomDialog customDialog;
    ConnectionHelper connectionHelper;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    DocumentAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsafety);
        ButterKnife.bind(this);
        connectionHelper = new ConnectionHelper(this);
        customDialog = new CustomDialog(this);
        title.setText(getString(R.string.food_safety));
        backImg.setVisibility(View.VISIBLE);
        getProfile();
    }

    @OnClick({R.id.back_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPressed();
            default:
                break;
        }
    }

    private void getProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            customDialog.show();
            new GetProfile(apiInterface, this);
        } else {
            Utils.displayMessage(this, getString(R.string.oops_no_internet));
        }
    }

    @Override
    public void onSuccess(Profile profile) {
        customDialog.dismiss();
        if (profile.getTrainingModules() != null && profile.getTrainingModules().size() > 0) {
            mAdapter = new DocumentAdapter(profile.getTrainingModules(), this);
            mRVDocuments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRVDocuments.setHasFixedSize(true);
            mRVDocuments.setAdapter(mAdapter);
        } else {
            txtError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        txtError.setVisibility(View.VISIBLE);
    }
}

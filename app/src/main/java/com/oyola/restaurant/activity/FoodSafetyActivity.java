package com.oyola.restaurant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.DocumentAdapter;
import com.oyola.restaurant.controller.GetProfile;
import com.oyola.restaurant.controller.ProfileListener;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.model.Profile;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Utils;

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
        title.setText(getString(R.string.documents));
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

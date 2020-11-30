package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.UserRequestAdapter;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRequestActivity extends AppCompatActivity implements UserRequestAdapter.IUserRequestListener {

    @BindView(R.id.user_request_rv)
    RecyclerView userRequestsRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<UserRequestItem> userRequestItems = new ArrayList<>();
    private UserRequestAdapter userRequestAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        ButterKnife.bind(this);
        customDialog = new CustomDialog(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.user_requests);
        findViewById(R.id.toolbar).findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupAdapter();
    }

    private void setupAdapter() {
        userRequestAdapter = new UserRequestAdapter(userRequestItems, this,this);
        userRequestsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        userRequestsRv.setHasFixedSize(true);
        userRequestsRv.setAdapter(userRequestAdapter);
    }

    @Override
    public void onUserRequestItemClicked(UserRequestItem userRequestItem) {
        Intent intent = new Intent(UserRequestActivity.this, OrderRequestDetailActivity.class);
        intent.putExtra("userRequestItem", (Serializable)userRequestItem);
        startActivity(intent);
    }

    CustomDialog customDialog;
    private void getUserRequestList() {
        customDialog.show();
        Call<List<UserRequestItem>> call = apiInterface.getUserRequests();
        call.enqueue(new Callback<List<UserRequestItem>>() {
            @Override
            public void onResponse(Call<List<UserRequestItem>> call, Response<List<UserRequestItem>> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    userRequestItems.clear();
                    List<UserRequestItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            userRequestsRv.setVisibility(View.VISIBLE);
                            userRequestItems.addAll(subscribedModel);
                            userRequestAdapter.setList(userRequestItems);
                            userRequestAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            userRequestsRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserRequestItem>> call, Throwable t) {
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                customDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserRequestList();
    }
}
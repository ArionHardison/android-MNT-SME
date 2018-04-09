package com.tomoeats.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.adapter.DeliveriesAdapter;
import com.tomoeats.restaurant.fragment.FilterDialogFragment;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.model.HistoryModel;
import com.tomoeats.restaurant.model.Order;
import com.tomoeats.restaurant.model.ServerError;
import com.tomoeats.restaurant.model.Transporter;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;
import com.tomoeats.restaurant.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveriesActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.filter_img)
    ImageView filterImg;
    @BindView(R.id.deliveries_rv)
    RecyclerView deliveriesRv;

    Context context = DeliveriesActivity.this;
    DeliveriesAdapter deliveriesAdapter;

    List<Order> orderList = new ArrayList<>();
    List<Transporter> transporters = new ArrayList<>();
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveries);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.deliveries));
        setupAdapter();
        customDialog = new CustomDialog(context);
        getTransporterList();
    }

    private void setupAdapter() {
        deliveriesAdapter = new DeliveriesAdapter(orderList, context);
        deliveriesRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        deliveriesRv.setHasFixedSize(true);
        deliveriesRv.setAdapter(deliveriesAdapter);
    }

    @OnClick({R.id.back_img, R.id.filter_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.filter_img:
                FilterDialogFragment filterDialogFragment =FilterDialogFragment.newInstance(transporters);
                filterDialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistory();
    }

    private void getHistory() {
        showDialog();
        Call<HistoryModel> call = apiInterface.getHistory();
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(@NonNull Call<HistoryModel> call, @NonNull Response<HistoryModel> response) {
                dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    HistoryModel historyModel = response.body();
                    if (historyModel != null) {
                        if (historyModel.getCOMPLETED() != null && historyModel.getCOMPLETED().size() > 0)
                            orderList.addAll(historyModel.getCOMPLETED());
                        if (historyModel.getCANCELLED() != null && historyModel.getCANCELLED().size() > 0)
                            orderList.addAll(historyModel.getCANCELLED());
                        if (orderList != null && orderList.size() > 0) {
                            deliveriesAdapter.setList(orderList);
                            deliveriesAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(DeliveriesActivity.this, serverError.getError());
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                dismissDialog();
                Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

    public void showDialog() {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.setCancelable(false);
            customDialog.show();
        }
    }

    public void dismissDialog() {
        if (customDialog != null & customDialog.isShowing())
            customDialog.dismiss();
    }

    public void getTransporterList() {
        showDialog();
        Call<List<Transporter>> call = apiInterface.getTransporter();
        call.enqueue(new Callback<List<Transporter>>() {
            @Override
            public void onResponse(@NonNull Call<List<Transporter>> call, @NonNull Response<List<Transporter>> response) {
                dismissDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        transporters = response.body();
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(DeliveriesActivity.this, serverError.getError());
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Transporter>> call, Throwable t) {
                dismissDialog();
                Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

}

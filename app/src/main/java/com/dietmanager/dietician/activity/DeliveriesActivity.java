package com.dietmanager.dietician.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.DeliveriesAdapter;
import com.dietmanager.dietician.fragment.FilterDialogFragment;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.messages.FilterDialogFragmentMessage;
import com.dietmanager.dietician.messages.communicator.DataMessage;
import com.dietmanager.dietician.model.HistoryOrder;
import com.dietmanager.dietician.model.Order;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.Transporter;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveriesActivity extends AppCompatActivity implements DataMessage {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.filter_img)
    ImageView filterImg;
    @BindView(R.id.deliveries_rv)
    RecyclerView deliveriesRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private DeliveriesAdapter deliveriesAdapter;

    private List<Order> orderList = new ArrayList<>();
    private List<Transporter> transporters = new ArrayList<>();
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private CustomDialog customDialog;
    private FilterDialogFragmentMessage message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveries);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.deliveries));
        customDialog = new CustomDialog(this);
        deliveriesAdapter = new DeliveriesAdapter();
        deliveriesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        deliveriesRv.setHasFixedSize(true);
        deliveriesRv.setAdapter(deliveriesAdapter);

        getTransporterList();
        getHistory();
    }

    @OnClick({R.id.back_img, R.id.filter_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.filter_img:
                FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(transporters);
                if (message != null) {
                    filterDialogFragment.onReceiveData(message);
                }
                filterDialogFragment.setCancelable(true);
                filterDialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
                break;
        }
    }

    private void getHistory() {
        Map<String, String> params = new HashMap<>();
        params.put("list", "true");
        params.put("status", "COMPLETED");

        showDialog();
        Call<HistoryOrder> call = apiInterface.getHistoryOrders(params);
        call.enqueue(new Callback<HistoryOrder>() {
            @Override
            public void onResponse(@NonNull Call<HistoryOrder> call, @NonNull Response<HistoryOrder> response) {
                dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    HistoryOrder historyModel = response.body();
                    if (historyModel != null && !Utils.isNullOrEmpty(historyModel.getOrders())) {
                        orderList.addAll(historyModel.getOrders());
                        llNoRecords.setVisibility(View.GONE);
                        deliveriesRv.setVisibility(View.VISIBLE);
                        deliveriesAdapter.setList(orderList);
                    } else {
                        llNoRecords.setVisibility(View.VISIBLE);
                        deliveriesRv.setVisibility(View.GONE);
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(DeliveriesActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(DeliveriesActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryOrder> call, Throwable t) {
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
                        if (response.code() == 401) {
                            startActivity(new Intent(DeliveriesActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
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

    @Override
    public void onReceiveData(Object receivedData) {
        if (receivedData instanceof FilterDialogFragmentMessage) {
            message = (FilterDialogFragmentMessage) receivedData;
            if (message == null) {
                getHistory();
            } else {
                handleFilterMessage(message);
            }
        }
    }


    private void handleFilterMessage(FilterDialogFragmentMessage message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "true");
        params.put("status", (message != null && !TextUtils.isEmpty(message.getOrderStatus())) ? message.getOrderStatus().toUpperCase() : "COMPLETED");
        if (message != null) {
            if (message.getDelieveryPersonId() > 0)
                params.put("dp", "" + message.getDelieveryPersonId());
            if (!TextUtils.isEmpty(message.getFromDate()))
                params.put("start_date", message.getFromDate());
            if (!TextUtils.isEmpty(message.getToDate()))
                params.put("end_date", message.getToDate());
            if (!TextUtils.isEmpty(message.getOrderType()))
                params.put("order_type", message.getOrderType());
        }
        getFilterResults(params);
    }

    private void getFilterResults(HashMap<String, String> map) {
        showDialog();
        Call<HistoryOrder> call = apiInterface.getHistoryOrders(map);
        call.enqueue(new Callback<HistoryOrder>() {
            @Override
            public void onResponse(@NonNull Call<HistoryOrder> call, @NonNull Response<HistoryOrder> response) {
                dismissDialog();
                if (response.isSuccessful()) {
                    orderList.clear();
                    HistoryOrder historyModel = response.body();
                    if (historyModel != null && !Utils.isNullOrEmpty(historyModel.getOrders())) {
                        orderList.addAll(historyModel.getOrders());
                        llNoRecords.setVisibility(View.GONE);
                        deliveriesRv.setVisibility(View.VISIBLE);
                        deliveriesAdapter.setList(orderList);
                    } else {
                        llNoRecords.setVisibility(View.VISIBLE);
                        deliveriesRv.setVisibility(View.GONE);
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(DeliveriesActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(DeliveriesActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryOrder> call, Throwable t) {
                dismissDialog();
                Utils.displayMessage(DeliveriesActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }
}

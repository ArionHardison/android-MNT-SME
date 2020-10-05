package com.dietmanager.restaurant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.activity.LoginActivity;
import com.dietmanager.restaurant.adapter.TakeAwayAdapter;
import com.dietmanager.restaurant.helper.ConnectionHelper;
import com.dietmanager.restaurant.helper.CustomDialog;
import com.dietmanager.restaurant.model.IncomingOrders;
import com.dietmanager.restaurant.model.Order;
import com.dietmanager.restaurant.model.SectionHeaderItem;
import com.dietmanager.restaurant.model.ServerError;
import com.dietmanager.restaurant.network.ApiClient;
import com.dietmanager.restaurant.network.ApiInterface;
import com.dietmanager.restaurant.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasanth on 25-10-2019.
 */
public class TakeAwayFragment extends Fragment {


    @BindView(R.id.incoming_ta)
    RecyclerView takeawayRv;
    @BindView(R.id.title)
    TextView title;
    private TakeAwayAdapter mAdapter;
    List<Order> orderList;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternet;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "TakeAwayFragment";
    String email, password;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;
    private Handler homeHandler = new Handler();
    private boolean isVisible = true;

    public TakeAwayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_takeaway, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        prepareAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderList = new ArrayList<>();
        connectionHelper = new ConnectionHelper(getContext());
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(getContext());
        title.setText(getString(R.string.manage_order));
        getIncomingOrders();
       /* isVisible = true;
        getProfile();
        getIncomingOrders();
        homeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isInternet) {
                    if (isVisible && incomingRv != null) {
                        getIncomingOrders();
                        homeHandler.postDelayed(this, 3000);
                    }
                }
            }
        }, 3000);*/


      /*  LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.BROADCAST.UPDATE_ORDERS));*/

    }

    private void prepareAdapter() {
        if (takeawayRv != null) {
            mAdapter = new TakeAwayAdapter(getContext());
            takeawayRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            takeawayRv.setHasFixedSize(true);
            takeawayRv.setAdapter(mAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        getIncomingOrders();
    }


    private void getIncomingOrders() {
        customDialog.show();
        Call<IncomingOrders> call = apiInterface.getIncomingOrders("takeaway");
        call.enqueue(new Callback<IncomingOrders>() {
            @Override
            public void onResponse(Call<IncomingOrders> call, Response<IncomingOrders> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getOrders() != null &&
                            !response.body().getOrders().isEmpty() && response.body().getOrders().size() > 0) {
                        if (takeawayRv != null && llNoRecords != null) {
                            takeawayRv.setVisibility(View.VISIBLE);
                            llNoRecords.setVisibility(View.GONE);
                        }
                        orderList.clear();
                        orderList.addAll(response.body().getOrders());
                        splitUpList(orderList);
                    } else {
                        if (takeawayRv != null && llNoRecords != null) {
                            takeawayRv.setVisibility(View.GONE);
                            llNoRecords.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(getActivity(), serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            getActivity().finish();
                        }
                    } catch (JsonSyntaxException e) {
//                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<IncomingOrders> call, Throwable t) {
                customDialog.dismiss();
            }
        });
    }

    private void splitUpList(List<Order> orderList) {
        List<SectionHeaderItem> sectionHeaderItemList = new ArrayList<>();

        List<Order> scheduledOrders = new ArrayList<>();
        List<Order> onGoingOrders = new ArrayList<>();

        for (int i = 0, size = orderList.size(); i < size; i++) {
            Order order = orderList.get(i);
            if (order != null) {
                if (order.getScheduleStatus() != null && order.getScheduleStatus() == 1) {
                    scheduledOrders.add(order);
                } else {
                    onGoingOrders.add(order);
                }
            }
        }

        if (scheduledOrders.size() > 0) {
            sectionHeaderItemList.add(new SectionHeaderItem("SCHEDULED ORDERS", scheduledOrders));
        }
        if (onGoingOrders.size() > 0) {
            sectionHeaderItemList.add(new SectionHeaderItem("ASAP ORDERS", onGoingOrders));
        }
        mAdapter.setRequestItemList(sectionHeaderItemList);
    }
}

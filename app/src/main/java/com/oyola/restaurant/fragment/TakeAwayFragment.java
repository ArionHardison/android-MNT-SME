package com.oyola.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oyola.restaurant.R;
import com.oyola.restaurant.activity.LoginActivity;
import com.oyola.restaurant.adapter.RequestAdapter;
import com.oyola.restaurant.adapter.TakeAwayAdapter;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.model.IncomingOrders;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.ServerError;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasanth on 25-10-2019.
 */
public class TakeAwayFragment  extends Fragment {


    @BindView(R.id.incoming_ta)
    RecyclerView takeawayRv;
    @BindView(R.id.title)
    TextView title;
    Unbinder unbinder;
    TakeAwayAdapter mAdapter;
    List<Order> orderList;
    Context context;
    Activity activity;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_takeaway, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderList = new ArrayList<>();
        context = getContext();
        activity = getActivity();
        connectionHelper = new ConnectionHelper(context);
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
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
            mAdapter = new TakeAwayAdapter(orderList, context);
            takeawayRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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
                        if (mAdapter == null) {
                            prepareAdapter();
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
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
                        Utils.displayMessage(activity, serverError.getError());
                        if (response.code() == 401) {
                            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            activity.finish();
                        }
                    } catch (JsonSyntaxException e) {
//                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<IncomingOrders> call, Throwable t) {
                customDialog.dismiss();
//                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
